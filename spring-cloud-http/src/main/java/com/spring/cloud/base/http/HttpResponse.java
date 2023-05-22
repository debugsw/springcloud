package com.spring.cloud.base.http;

import com.spring.cloud.base.http.exception.HttpException;
import com.spring.cloud.base.http.utils.HttpUtil;
import com.spring.cloud.base.utils.*;
import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.exception.IORuntimeException;
import com.spring.cloud.base.utils.interf.StreamProgress;
import com.spring.cloud.base.utils.str.StrUtil;
import com.spring.cloud.base.utils.utils.FileUtil;
import com.spring.cloud.base.utils.utils.IoUtil;
import com.spring.cloud.base.utils.utils.ReUtil;
import com.spring.cloud.base.utils.utils.URLUtil;

import java.io.*;
import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map.Entry;

/**
 * @Author: ls
 * @Description: http响应类
 * @Date: 2023/4/26 15:00
 */
public class HttpResponse extends HttpBase<HttpResponse> implements Closeable {

    /**
     * Http配置
     */
    protected HttpConfig config;
    /**
     * 持有连接对象
     */
    protected HttpConnection httpConnection;
    /**
     * Http请求原始流
     */
    protected InputStream in;
    /**
     * 是否异步，异步下只持有流，否则将在初始化时直接读取body内容
     */
    private volatile boolean isAsync;
    /**
     * 响应状态码
     */
    protected int status;
    /**
     * 是否忽略读取Http响应体
     */
    private final boolean ignoreBody;
    /**
     * 从响应中获取的编码
     */
    private Charset charsetFromResponse;

    /**
     * 构造
     *
     * @param httpConnection {@link HttpConnection}
     * @param config         Http配置
     * @param charset        编码，从请求编码中获取默认编码
     * @param isAsync        是否异步
     * @param isIgnoreBody   是否忽略读取响应体
     */
    protected HttpResponse(HttpConnection httpConnection, HttpConfig config, Charset charset, boolean isAsync, boolean isIgnoreBody) {
        this.httpConnection = httpConnection;
        this.config = config;
        this.charset = charset;
        this.isAsync = isAsync;
        this.ignoreBody = isIgnoreBody;
        initWithDisconnect();
    }

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    public int getStatus() {
        return this.status;
    }

    /**
     * 请求是否成功，判断依据为：状态码范围在200~299内。
     *
     * @return 是否成功请求
     *
     */
    public boolean isOk() {
        return this.status >= 200 && this.status < 300;
    }

    /**
     * 同步<br>
     * 如果为异步状态，则暂时不读取服务器中响应的内容，而是持有Http链接的{@link InputStream}。<br>
     * 当调用此方法时，异步状态转为同步状态，此时从Http链接流中读取body内容并暂存在内容中。如果已经是同步状态，则不进行任何操作。
     *
     * @return this
     */
    public HttpResponse sync() {
        return this.isAsync ? forceSync() : this;
    }

    /**
     * 获取内容编码
     *
     * @return String
     */
    public String contentEncoding() {
        return header(Header.CONTENT_ENCODING);
    }

    /**
     * 获取内容长度，以下情况长度无效：
     * <ul>
     *     <li>Transfer-Encoding: Chunked</li>
     *     <li>Content-Encoding: XXX</li>
     * </ul>
     *
     * @return 长度，-1表示服务端未返回或长度无效
     *
     */
    public long contentLength() {
        long contentLength = Convert.toLong(header(Header.CONTENT_LENGTH), -1L);
        if (contentLength > 0 && (isChunked() || StrUtil.isNotBlank(contentEncoding()))) {
            //按照HTTP协议规范，在 Transfer-Encoding和Content-Encoding设置后 Content-Length 无效。
            contentLength = -1;
        }
        return contentLength;
    }

    /**
     * 是否为gzip压缩过的内容
     *
     * @return 是否为gzip压缩过的内容
     */
    public boolean isGzip() {
        final String contentEncoding = contentEncoding();
        return "gzip".equalsIgnoreCase(contentEncoding);
    }

    /**
     * 是否为zlib(Deflate)压缩过的内容
     *
     * @return 是否为zlib(Deflate)压缩过的内容
     *
     */
    public boolean isDeflate() {
        final String contentEncoding = contentEncoding();
        return "deflate".equalsIgnoreCase(contentEncoding);
    }

    /**
     * 是否为Transfer-Encoding:Chunked的内容
     *
     * @return 是否为Transfer-Encoding:Chunked的内容
     *
     */
    public boolean isChunked() {
        final String transferEncoding = header(Header.TRANSFER_ENCODING);
        return "Chunked".equalsIgnoreCase(transferEncoding);
    }

    /**
     * 获取本次请求服务器返回的Cookie信息
     *
     * @return Cookie字符串
     *
     */
    public String getCookieStr() {
        return header(Header.SET_COOKIE);
    }

    /**
     * 获取Cookie
     *
     * @return Cookie列表
     *
     */
    public List<HttpCookie> getCookies() {
        return GlobalCookieManager.getCookies(this.httpConnection);
    }

    /**
     * 获取Cookie
     *
     * @param name Cookie名
     * @return {@link HttpCookie}
     *
     */
    public HttpCookie getCookie(String name) {
        List<HttpCookie> cookie = getCookies();
        if (null != cookie) {
            for (HttpCookie httpCookie : cookie) {
                if (httpCookie.getName().equals(name)) {
                    return httpCookie;
                }
            }
        }
        return null;
    }

    /**
     * 获取Cookie值
     *
     * @param name Cookie名
     * @return Cookie值
     *
     */
    public String getCookieValue(String name) {
        final HttpCookie cookie = getCookie(name);
        return (null == cookie) ? null : cookie.getValue();
    }

    /**
     * 获得服务区响应流<br>
     * 异步模式下获取Http原生流，同步模式下获取获取到的在内存中的副本<br>
     * 如果想在同步模式下获取流，请先调用{@link #sync()}方法强制同步<br>
     * 流获取后处理完毕需关闭此类
     *
     * @return 响应流
     */
    public InputStream bodyStream() {
        if (isAsync) {
            return this.in;
        }
        return null == this.body ? null : this.body.getStream();
    }

    /**
     * 获取响应流字节码<br>
     * 此方法会转为同步模式
     *
     * @return byte[]
     */
    @Override
    public byte[] bodyBytes() {
        sync();
        return super.bodyBytes();
    }

    /**
     * 设置主体字节码<br>
     * 需在此方法调用前使用charset方法设置编码，否则使用默认编码UTF-8
     *
     * @param bodyBytes 主体
     * @return this
     */
    public HttpResponse body(byte[] bodyBytes) {
        sync();
        if (null != bodyBytes) {
            this.body = new BytesResource(bodyBytes);
        }
        return this;
    }

    /**
     * 获取响应主体
     *
     * @return String
     * @throws HttpException 包装IO异常
     */
    public String body() throws HttpException {
        return HttpUtil.getString(bodyBytes(), this.charset,
                null == this.charsetFromResponse);
    }

    /**
     * 将响应内容写出到{@link OutputStream}<br>
     * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
     * 写出后会关闭Http流（异步模式）
     *
     * @param out            写出的流
     * @param isCloseOut     是否关闭输出流
     * @param streamProgress 进度显示接口，通过实现此接口显示下载进度
     * @return 写出bytes数
     *
     */
    public long writeBody(OutputStream out, boolean isCloseOut, StreamProgress streamProgress) {
        Assert.notNull(out, "[out] must be not null!");
        final long contentLength = contentLength();
        try {
            return copyBody(bodyStream(), out, contentLength, streamProgress, this.config.ignoreEOFError);
        } finally {
            IoUtil.close(this);
            if (isCloseOut) {
                IoUtil.close(out);
            }
        }
    }

    /**
     * 将响应内容写出到文件<br>
     * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
     * 写出后会关闭Http流（异步模式）
     *
     * @param targetFileOrDir 写出到的文件或目录
     * @param streamProgress  进度显示接口，通过实现此接口显示下载进度
     * @return 写出bytes数
     *
     */
    public long writeBody(File targetFileOrDir, StreamProgress streamProgress) {
        Assert.notNull(targetFileOrDir, "[targetFileOrDir] must be not null!");

        final File outFile = completeFileNameFromHeader(targetFileOrDir);
        return writeBody(FileUtil.getOutputStream(outFile), true, streamProgress);
    }

    /**
     * 将响应内容写出到文件-避免未完成的文件<br>
     * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
     * 写出后会关闭Http流（异步模式）<br>
     * 此方法原理是先在目标文件同级目录下创建临时文件，下载之，等下载完毕后重命名，避免因下载错误导致的文件不完整。
     *
     * @param targetFileOrDir 写出到的文件或目录
     * @param tempFileSuffix  临时文件后缀，默认".temp"
     * @param streamProgress  进度显示接口，通过实现此接口显示下载进度
     * @return 写出bytes数
     *
     */
    public long writeBody(File targetFileOrDir, String tempFileSuffix, StreamProgress streamProgress) {
        Assert.notNull(targetFileOrDir, "[targetFileOrDir] must be not null!");

        File outFile = completeFileNameFromHeader(targetFileOrDir);

        if (StrUtil.isBlank(tempFileSuffix)) {
            tempFileSuffix = ".temp";
        } else {
            tempFileSuffix = StrUtil.addPrefixIfNot(tempFileSuffix, StrUtil.DOT);
        }

        // 目标文件真实名称
        final String fileName = outFile.getName();
        // 临时文件名称
        final String tempFileName = fileName + tempFileSuffix;

        // 临时文件
        outFile = new File(outFile.getParentFile(), tempFileName);

        long length;
        try {
            length = writeBody(outFile, streamProgress);
            // 重命名下载好的临时文件
            FileUtil.rename(outFile, fileName, true);
        } catch (Throwable e) {
            // 异常则删除临时文件
            FileUtil.del(outFile);
            throw new HttpException(e);
        }
        return length;
    }

    /**
     * 将响应内容写出到文件<br>
     * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
     * 写出后会关闭Http流（异步模式）
     *
     * @param targetFileOrDir 写出到的文件
     * @param streamProgress  进度显示接口，通过实现此接口显示下载进度
     * @return 写出的文件
     *
     */
    public File writeBodyForFile(File targetFileOrDir, StreamProgress streamProgress) {
        Assert.notNull(targetFileOrDir, "[targetFileOrDir] must be not null!");

        final File outFile = completeFileNameFromHeader(targetFileOrDir);
        writeBody(FileUtil.getOutputStream(outFile), true, streamProgress);

        return outFile;
    }

    /**
     * 将响应内容写出到文件<br>
     * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
     * 写出后会关闭Http流（异步模式）
     *
     * @param targetFileOrDir 写出到的文件或目录
     * @return 写出bytes数
     *
     */
    public long writeBody(File targetFileOrDir) {
        return writeBody(targetFileOrDir, null);
    }

    /**
     * 将响应内容写出到文件<br>
     * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
     * 写出后会关闭Http流（异步模式）
     *
     * @param targetFileOrDir 写出到的文件或目录的路径
     * @return 写出bytes数
     *
     */
    public long writeBody(String targetFileOrDir) {
        return writeBody(FileUtil.file(targetFileOrDir));
    }

    @Override
    public void close() {
        IoUtil.close(this.in);
        this.in = null;
        // 关闭连接
        this.httpConnection.disconnectQuietly();
    }

    @Override
    public String toString() {
        StringBuilder sb = StrUtil.builder();
        sb.append("Response Headers: ").append(StrUtil.CRLF);
        for (Entry<String, List<String>> entry : this.headers.entrySet()) {
            sb.append("    ").append(entry).append(StrUtil.CRLF);
        }

        sb.append("Response Body: ").append(StrUtil.CRLF);
        sb.append("    ").append(this.body()).append(StrUtil.CRLF);

        return sb.toString();
    }

    /**
     * 从响应头补全下载文件名
     *
     * @param targetFileOrDir 目标文件夹或者目标文件
     * @return File 保存的文件
     *
     */
    public File completeFileNameFromHeader(File targetFileOrDir) {
        if (false == targetFileOrDir.isDirectory()) {
            // 非目录直接返回
            return targetFileOrDir;
        }

        // 从头信息中获取文件名
        String fileName = getFileNameFromDisposition(null);
        if (StrUtil.isBlank(fileName)) {
            final String path = httpConnection.getUrl().getPath();
            // 从路径中获取文件名
            fileName = StrUtil.subSuf(path, path.lastIndexOf('/') + 1);
            if (StrUtil.isBlank(fileName)) {
                // 编码后的路径做为文件名
                fileName = URLUtil.encodeQuery(path, charset);
            } else {
                // issue#I4K0FS@Gitee
                fileName = URLUtil.decode(fileName, charset);
            }
        }
        return FileUtil.file(targetFileOrDir, fileName);
    }

    /**
     * 从Content-Disposition头中获取文件名
     *
     * @param paramName 文件参数名
     * @return 文件名，empty表示无
     */
    public String getFileNameFromDisposition(String paramName) {
        paramName = ObjectUtil.defaultIfNull(paramName, "filename");
        String fileName = null;
        final String disposition = header(Header.CONTENT_DISPOSITION);
        if (StrUtil.isNotBlank(disposition)) {
            fileName = ReUtil.get(paramName + "=\"(.*?)\"", disposition, 1);
            if (StrUtil.isBlank(fileName)) {
                fileName = StrUtil.subAfter(disposition, paramName + "=", true);
            }
        }
        return fileName;
    }

    /**
     * 初始化Http响应，并在报错时关闭连接。<br>
     * 初始化包括：
     *
     * <pre>
     * 1、读取Http状态
     * 2、读取头信息
     * 3、持有Http流，并不关闭流
     * </pre>
     *
     * @return this
     * @throws HttpException IO异常
     */
    private HttpResponse initWithDisconnect() throws HttpException {
        try {
            init();
        } catch (HttpException e) {
            this.httpConnection.disconnectQuietly();
            throw e;
        }
        return this;
    }

    /**
     * 初始化Http响应<br>
     * 初始化包括：
     *
     * <pre>
     * 1、读取Http状态
     * 2、读取头信息
     * 3、持有Http流，并不关闭流
     * </pre>
     *
     * @return this
     * @throws HttpException IO异常
     */
    private HttpResponse init() throws HttpException {
        // 获取响应状态码
        try {
            this.status = httpConnection.responseCode();
        } catch (IOException e) {
            if (false == (e instanceof FileNotFoundException)) {
                throw new HttpException(e);
            }
            // 服务器无返回内容，忽略之
        }


        // 读取响应头信息
        try {
            this.headers = httpConnection.headers();
        } catch (IllegalArgumentException e) {
            // ignore
        }

        // 存储服务端设置的Cookie信息
        GlobalCookieManager.store(httpConnection);

        // 获取响应编码
        final Charset charset = httpConnection.getCharset();
        this.charsetFromResponse = charset;
        if (null != charset) {
            this.charset = charset;
        }

        // 获取响应内容流
        this.in = new HttpInputStream(this);

        // 同步情况下强制同步
        return this.isAsync ? this : forceSync();
    }

    /**
     * 强制同步，用于初始化<br>
     * 强制同步后变化如下：
     *
     * <pre>
     * 1、读取body内容到内存
     * 2、异步状态设为false（变为同步状态）
     * 3、关闭Http流
     * 4、断开与服务器连接
     * </pre>
     *
     * @return this
     */
    private HttpResponse forceSync() {
        // 非同步状态转为同步状态
        try {
            this.readBody(this.in);
        } catch (IORuntimeException e) {
            //noinspection StatementWithEmptyBody
            if (e.getCause() instanceof FileNotFoundException) {
                // 服务器无返回内容，忽略之
            } else {
                throw new HttpException(e);
            }
        } finally {
            if (this.isAsync) {
                this.isAsync = false;
            }
            this.close();
        }
        return this;
    }

    /**
     * 读取主体，忽略EOFException异常
     *
     * @param in 输入流
     * @throws IORuntimeException IO异常
     */
    private void readBody(InputStream in) throws IORuntimeException {
        if (ignoreBody) {
            return;
        }

        final long contentLength = contentLength();
        final FastByteArrayOutputStream out = new FastByteArrayOutputStream((int) contentLength);
        copyBody(in, out, contentLength, null, this.config.ignoreEOFError);
        this.body = new BytesResource(out.toByteArray());
    }

    /**
     * 将响应内容写出到{@link OutputStream}<br>
     * 异步模式下直接读取Http流写出，同步模式下将存储在内存中的响应内容写出<br>
     * 写出后会关闭Http流（异步模式）
     *
     * @param in               输入流
     * @param out              写出的流
     * @param contentLength    总长度，-1表示未知
     * @param streamProgress   进度显示接口，通过实现此接口显示下载进度
     * @param isIgnoreEOFError 是否忽略响应读取时可能的EOF异常
     * @return 拷贝长度
     */
    private static long copyBody(InputStream in, OutputStream out, long contentLength, StreamProgress streamProgress, boolean isIgnoreEOFError) {
        if (null == out) {
            throw new NullPointerException("[out] is null!");
        }

        long copyLength = -1;
        try {
            copyLength = IoUtil.copy(in, out, IoUtil.DEFAULT_BUFFER_SIZE, contentLength, streamProgress);
        } catch (IORuntimeException e) {
            //noinspection StatementWithEmptyBody
            if (isIgnoreEOFError && (e.getCause() instanceof EOFException || StrUtil.containsIgnoreCase(e.getMessage(), "Premature EOF"))) {
                // 忽略读取HTTP流中的EOF错误
            } else {
                throw e;
            }
        }
        return copyLength;
    }
}
