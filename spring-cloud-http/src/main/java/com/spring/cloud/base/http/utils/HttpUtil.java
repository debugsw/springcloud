package com.spring.cloud.base.http.utils;

import com.spring.cloud.base.http.*;
import com.spring.cloud.base.utils.*;
import com.spring.cloud.base.utils.base.Base64;
import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.interf.StreamProgress;
import com.spring.cloud.base.utils.map.MapUtil;
import com.spring.cloud.base.utils.str.StrBuilder;
import com.spring.cloud.base.utils.str.StrUtil;
import com.spring.cloud.base.utils.utils.CharsetUtil;
import com.spring.cloud.base.utils.utils.FileUtil;
import com.spring.cloud.base.utils.utils.IoUtil;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @Author: ls
 * @Description: Http请求工具类
 * @Date: 2023/4/26 15:00
 */
public class HttpUtil {

    /**
     * 正则：Content-Type中的编码信息
     */
    public static final Pattern CHARSET_PATTERN = Pattern.compile("charset\\s*=\\s*([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);
    /**
     * 正则：匹配meta标签的编码信息
     */
    public static final Pattern META_CHARSET_PATTERN = Pattern.compile("<meta[^>]*?charset\\s*=\\s*['\"]?([a-z0-9-]*)", Pattern.CASE_INSENSITIVE);

    /**
     * 检测是否https
     *
     * @param url URL
     * @return 是否https
     */
    public static boolean isHttps(String url) {
        return StrUtil.startWithIgnoreCase(url, "https:");
    }

    /**
     * 检测是否http
     *
     * @param url URL
     * @return 是否http
     */
    public static boolean isHttp(String url) {
        return StrUtil.startWithIgnoreCase(url, "http:");
    }

    /**
     * 创建Http请求对象
     *
     * @param method 方法枚举{@link Method}
     * @param url    请求的URL，可以使HTTP或者HTTPS
     * @return {@link HttpRequest}
     *
     */
    public static HttpRequest createRequest(Method method, String url) {
        return HttpRequest.of(url).method(method);
    }

    /**
     * 创建Http GET请求对象
     *
     * @param url 请求的URL，可以使HTTP或者HTTPS
     * @return {@link HttpRequest}
     *
     */
    public static HttpRequest createGet(String url) {
        return createGet(url, false);
    }

    /**
     * 创建Http GET请求对象
     *
     * @param url               请求的URL，可以使HTTP或者HTTPS
     * @param isFollowRedirects 是否打开重定向
     * @return {@link HttpRequest}
     *
     */
    public static HttpRequest createGet(String url, boolean isFollowRedirects) {
        return HttpRequest.get(url).setFollowRedirects(isFollowRedirects);
    }

    /**
     * 创建Http POST请求对象
     *
     * @param url 请求的URL，可以使HTTP或者HTTPS
     * @return {@link HttpRequest}
     *
     */
    public static HttpRequest createPost(String url) {
        return HttpRequest.post(url);
    }

    /**
     * 发送get请求
     *
     * @param urlString     网址
     * @param customCharset 自定义请求字符集，如果字符集获取不到，使用此字符集
     * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
     */
    public static String get(String urlString, Charset customCharset) {
        return HttpRequest.get(urlString).charset(customCharset).execute().body();
    }

    /**
     * 发送get请求
     *
     * @param urlString 网址
     * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
     */
    public static String get(String urlString) {
        return get(urlString, HttpGlobalConfig.getTimeout());
    }

    /**
     * 发送get请求
     *
     * @param urlString 网址
     * @param timeout   超时时长，-1表示默认超时，单位毫秒
     * @return 返回内容，如果只检查状态码，正常只返回 ""，不正常返回 null
     *
     */
    public static String get(String urlString, int timeout) {
        return HttpRequest.get(urlString).timeout(timeout).execute().body();
    }

    /**
     * 发送get请求
     *
     * @param urlString 网址
     * @param paramMap  post表单数据
     * @return 返回数据
     */
    public static String get(String urlString, Map<String, Object> paramMap) {
        return HttpRequest.get(urlString).form(paramMap).execute().body();
    }

    /**
     * 发送get请求
     *
     * @param urlString 网址
     * @param paramMap  post表单数据
     * @param timeout   超时时长，-1表示默认超时，单位毫秒
     * @return 返回数据
     *
     */
    public static String get(String urlString, Map<String, Object> paramMap, int timeout) {
        return HttpRequest.get(urlString).form(paramMap).timeout(timeout).execute().body();
    }

    /**
     * 发送post请求
     *
     * @param urlString 网址
     * @param paramMap  post表单数据
     * @return 返回数据
     */
    public static String post(String urlString, Map<String, Object> paramMap) {
        return post(urlString, paramMap, HttpGlobalConfig.getTimeout());
    }

    /**
     * 发送post请求
     *
     * @param urlString 网址
     * @param paramMap  post表单数据
     * @param timeout   超时时长，-1表示默认超时，单位毫秒
     * @return 返回数据
     *
     */
    public static String post(String urlString, Map<String, Object> paramMap, int timeout) {
        return HttpRequest.post(urlString).form(paramMap).timeout(timeout).execute().body();
    }

    /**
     * 发送post请求<br>
     * 请求体body参数支持两种类型：
     *
     * <pre>
     * 1. 标准参数，例如 a=1&amp;b=2 这种格式
     * 2. Rest模式，此时body需要传入一个JSON或者XML字符串，会自动绑定其对应的Content-Type
     * </pre>
     *
     * @param urlString 网址
     * @param body      post表单数据
     * @return 返回数据
     */
    public static String post(String urlString, String body) {
        return post(urlString, body, HttpGlobalConfig.getTimeout());
    }

    /**
     * 发送post请求<br>
     * 请求体body参数支持两种类型：
     *
     * <pre>
     * 1. 标准参数，例如 a=1&amp;b=2 这种格式
     * 2. Rest模式，此时body需要传入一个JSON或者XML字符串，会自动绑定其对应的Content-Type
     * </pre>
     *
     * @param urlString 网址
     * @param body      post表单数据
     * @param timeout   超时时长，-1表示默认超时，单位毫秒
     * @return 返回数据
     */
    public static String post(String urlString, String body, int timeout) {
        return HttpRequest.post(urlString).timeout(timeout).body(body).execute().body();
    }

    /**
     * 下载远程文本
     *
     * @param url               请求的url
     * @param customCharsetName 自定义的字符集
     * @return 文本
     */
    public static String downloadString(String url, String customCharsetName) {
        return downloadString(url, CharsetUtil.charset(customCharsetName), null);
    }

    /**
     * 下载远程文本
     *
     * @param url           请求的url
     * @param customCharset 自定义的字符集，可以使用{@link CharsetUtil#charset} 方法转换
     * @return 文本
     */
    public static String downloadString(String url, Charset customCharset) {
        return downloadString(url, customCharset, null);
    }

    /**
     * 下载远程文本
     *
     * @param url           请求的url
     * @param customCharset 自定义的字符集，可以使用{@link CharsetUtil#charset} 方法转换
     * @param streamPress   进度条 {@link StreamProgress}
     * @return 文本
     */
    public static String downloadString(String url, Charset customCharset, StreamProgress streamPress) {
        return HttpDownloader.downloadString(url, customCharset, streamPress);
    }

    /**
     * 下载远程文件
     *
     * @param url  请求的url
     * @param dest 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
     * @return 文件大小
     */
    public static long downloadFile(String url, String dest) {
        return downloadFile(url, FileUtil.file(dest));
    }

    /**
     * 下载远程文件
     *
     * @param url      请求的url
     * @param destFile 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
     * @return 文件大小
     */
    public static long downloadFile(String url, File destFile) {
        return downloadFile(url, destFile, null);
    }

    /**
     * 下载远程文件
     *
     * @param url      请求的url
     * @param destFile 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
     * @param timeout  超时，单位毫秒，-1表示默认超时
     * @return 文件大小
     *
     */
    public static long downloadFile(String url, File destFile, int timeout) {
        return downloadFile(url, destFile, timeout, null);
    }

    /**
     * 下载远程文件
     *
     * @param url            请求的url
     * @param destFile       目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
     * @param streamProgress 进度条
     * @return 文件大小
     */
    public static long downloadFile(String url, File destFile, StreamProgress streamProgress) {
        return downloadFile(url, destFile, -1, streamProgress);
    }

    /**
     * 下载远程文件
     *
     * @param url            请求的url
     * @param destFile       目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
     * @param timeout        超时，单位毫秒，-1表示默认超时
     * @param streamProgress 进度条
     * @return 文件大小
     *
     */
    public static long downloadFile(String url, File destFile, int timeout, StreamProgress streamProgress) {
        return HttpDownloader.downloadFile(url, destFile, timeout, streamProgress);
    }

    /**
     * 下载远程文件
     *
     * @param url  请求的url
     * @param dest 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
     * @return 下载的文件对象
     */
    public static File downloadFileFromUrl(String url, String dest) {
        return downloadFileFromUrl(url, FileUtil.file(dest));
    }

    /**
     * 下载远程文件
     *
     * @param url      请求的url
     * @param destFile 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
     * @return 下载的文件对象
     *
     */
    public static File downloadFileFromUrl(String url, File destFile) {
        return downloadFileFromUrl(url, destFile, null);
    }

    /**
     * 下载远程文件
     *
     * @param url      请求的url
     * @param destFile 目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
     * @param timeout  超时，单位毫秒，-1表示默认超时
     * @return 下载的文件对象
     *
     */
    public static File downloadFileFromUrl(String url, File destFile, int timeout) {
        return downloadFileFromUrl(url, destFile, timeout, null);
    }

    /**
     * 下载远程文件
     *
     * @param url            请求的url
     * @param destFile       目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
     * @param streamProgress 进度条
     * @return 下载的文件对象
     *
     */
    public static File downloadFileFromUrl(String url, File destFile, StreamProgress streamProgress) {
        return downloadFileFromUrl(url, destFile, -1, streamProgress);
    }

    /**
     * 下载远程文件
     *
     * @param url            请求的url
     * @param destFile       目标文件或目录，当为目录时，取URL中的文件名，取不到使用编码后的URL做为文件名
     * @param timeout        超时，单位毫秒，-1表示默认超时
     * @param streamProgress 进度条
     * @return 下载的文件对象
     *
     */
    public static File downloadFileFromUrl(String url, File destFile, int timeout, StreamProgress streamProgress) {
        return HttpDownloader.downloadForFile(url, destFile, timeout, streamProgress);
    }

    /**
     * 下载远程文件
     *
     * @param url        请求的url
     * @param out        将下载内容写到输出流中 {@link OutputStream}
     * @param isCloseOut 是否关闭输出流
     * @return 文件大小
     */
    public static long download(String url, OutputStream out, boolean isCloseOut) {
        return download(url, out, isCloseOut, null);
    }

    /**
     * 下载远程文件
     *
     * @param url            请求的url
     * @param out            将下载内容写到输出流中 {@link OutputStream}
     * @param isCloseOut     是否关闭输出流
     * @param streamProgress 进度条
     * @return 文件大小
     */
    public static long download(String url, OutputStream out, boolean isCloseOut, StreamProgress streamProgress) {
        return HttpDownloader.download(url, out, isCloseOut, streamProgress);
    }

    /**
     * 下载远程文件数据，支持30x跳转
     *
     * @param url 请求的url
     * @return 文件数据
     *
     */
    public static byte[] downloadBytes(String url) {
        return HttpDownloader.downloadBytes(url);
    }

    /**
     * 将Map形式的Form表单数据转换为Url参数形式，会自动url编码键和值
     *
     * @param paramMap 表单数据
     * @return url参数
     */
    public static String toParams(Map<String, ?> paramMap) {
        return toParams(paramMap, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 将Map形式的Form表单数据转换为Url参数形式<br>
     * 编码键和值对
     *
     * @param paramMap    表单数据
     * @param charsetName 编码
     * @return url参数
     * @deprecated 请使用 {@link #toParams(Map, Charset)}
     */
    @Deprecated
    public static String toParams(Map<String, Object> paramMap, String charsetName) {
        return toParams(paramMap, CharsetUtil.charset(charsetName));
    }

    /**
     * 将Map形式的Form表单数据转换为Url参数形式<br>
     * paramMap中如果key为空（null和""）会被忽略，如果value为null，会被做为空白符（""）<br>
     * 会自动url编码键和值<br>
     * 此方法用于拼接URL中的Query部分，并不适用于POST请求中的表单
     *
     * <pre>
     * key1=v1&amp;key2=&amp;key3=v3
     * </pre>
     *
     * @param paramMap 表单数据
     * @param charset  编码，{@code null} 表示不encode键值对
     * @return url参数
     * @see #toParams(Map, Charset, boolean)
     */
    public static String toParams(Map<String, ?> paramMap, Charset charset) {
        return toParams(paramMap, charset, false);
    }

    /**
     * 将Map形式的Form表单数据转换为Url参数形式<br>
     * paramMap中如果key为空（null和""）会被忽略，如果value为null，会被做为空白符（""）<br>
     * 会自动url编码键和值
     *
     * <pre>
     * key1=v1&amp;key2=&amp;key3=v3
     * </pre>
     *
     * @param paramMap         表单数据
     * @param charset          编码，null表示不encode键值对
     * @param isFormUrlEncoded 是否为x-www-form-urlencoded模式，此模式下空格会编码为'+'
     * @return url参数
     *
     */
    public static String toParams(Map<String, ?> paramMap, Charset charset, boolean isFormUrlEncoded) {
        return UrlQuery.of(paramMap, isFormUrlEncoded).build(charset);
    }

    /**
     * 对URL参数做编码，只编码键和值<br>
     * 提供的值可以是url附带参数，但是不能只是url
     *
     * <p>注意，此方法只能标准化整个URL，并不适合于单独编码参数值</p>
     *
     * @param urlWithParams url和参数，可以包含url本身，也可以单独参数
     * @param charset       编码
     * @return 编码后的url和参数
     *
     */
    public static String encodeParams(String urlWithParams, Charset charset) {
        if (StrUtil.isBlank(urlWithParams)) {
            return StrUtil.EMPTY;
        }

        String urlPart = null;
        String paramPart;
        final int pathEndPos = urlWithParams.indexOf('?');
        if (pathEndPos > -1) {

            urlPart = StrUtil.subPre(urlWithParams, pathEndPos);
            paramPart = StrUtil.subSuf(urlWithParams, pathEndPos + 1);
            if (StrUtil.isBlank(paramPart)) {

                return urlPart;
            }
        } else if (false == StrUtil.contains(urlWithParams, '=')) {

            return urlWithParams;
        } else {

            paramPart = urlWithParams;
        }

        paramPart = normalizeParams(paramPart, charset);

        return StrUtil.isBlank(urlPart) ? paramPart : urlPart + "?" + paramPart;
    }

    /**
     * 标准化参数字符串，即URL中？后的部分
     *
     * <p>注意，此方法只能标准化整个URL，并不适合于单独编码参数值</p>
     *
     * @param paramPart 参数字符串
     * @param charset   编码
     * @return 标准化的参数字符串
     *
     */
    public static String normalizeParams(String paramPart, Charset charset) {
        if (StrUtil.isEmpty(paramPart)) {
            return paramPart;
        }
        final StrBuilder builder = StrBuilder.create(paramPart.length() + 16);
        final int len = paramPart.length();
        String name = null;
        int pos = 0;
        char c;
        int i;
        for (i = 0; i < len; i++) {
            c = paramPart.charAt(i);
            if (c == '=') {
                if (null == name) {

                    name = (pos == i) ? StrUtil.EMPTY : paramPart.substring(pos, i);
                    pos = i + 1;
                }
            } else if (c == '&') {
                if (pos != i) {
                    if (null == name) {

                        name = paramPart.substring(pos, i);
                        builder.append(RFC3986.QUERY_PARAM_NAME.encode(name, charset)).append('=');
                    } else {
                        builder.append(RFC3986.QUERY_PARAM_NAME.encode(name, charset)).append('=').append(RFC3986.QUERY_PARAM_VALUE.encode(paramPart.substring(pos, i), charset)).append('&');
                    }
                    name = null;
                }
                pos = i + 1;
            }
        }


        if (null != name) {
            builder.append(URLUtil.encodeQuery(name, charset)).append('=');
        }
        if (pos != i) {
            if (null == name && pos > 0) {
                builder.append('=');
            }
            builder.append(URLUtil.encodeQuery(paramPart.substring(pos, i), charset));
        }


        int lastIndex = builder.length() - 1;
        if ('&' == builder.charAt(lastIndex)) {
            builder.delTo(lastIndex);
        }
        return builder.toString();
    }

    /**
     * 将URL参数解析为Map（也可以解析Post中的键值对参数）
     *
     * @param paramsStr 参数字符串（或者带参数的Path）
     * @param charset   字符集
     * @return 参数Map
     *
     */
    public static Map<String, String> decodeParamMap(String paramsStr, Charset charset) {
        final Map<CharSequence, CharSequence> queryMap = UrlQuery.of(paramsStr, charset).getQueryMap();
        if (MapUtil.isEmpty(queryMap)) {
            return MapUtil.empty();
        }
        return Convert.toMap(String.class, String.class, queryMap);
    }

    /**
     * 将URL参数解析为Map（也可以解析Post中的键值对参数）
     *
     * @param paramsStr 参数字符串（或者带参数的Path）
     * @param charset   字符集
     * @return 参数Map
     */
    public static Map<String, List<String>> decodeParams(String paramsStr, String charset) {
        return decodeParams(paramsStr, charset, false);
    }

    /**
     * 将URL参数解析为Map（也可以解析Post中的键值对参数）
     *
     * @param paramsStr        参数字符串（或者带参数的Path）
     * @param charset          字符集
     * @param isFormUrlEncoded 是否为x-www-form-urlencoded模式，此模式下空格会编码为'+'
     * @return 参数Map
     *
     */
    public static Map<String, List<String>> decodeParams(String paramsStr, String charset, boolean isFormUrlEncoded) {
        return decodeParams(paramsStr, CharsetUtil.charset(charset), isFormUrlEncoded);
    }

    /**
     * 将URL QueryString参数解析为Map
     *
     * @param paramsStr 参数字符串（或者带参数的Path）
     * @param charset   字符集
     * @return 参数Map
     *
     */
    public static Map<String, List<String>> decodeParams(String paramsStr, Charset charset) {
        return decodeParams(paramsStr, charset, false);
    }

    /**
     * 将URL参数解析为Map（也可以解析Post中的键值对参数）
     *
     * @param paramsStr        参数字符串（或者带参数的Path）
     * @param charset          字符集
     * @param isFormUrlEncoded 是否为x-www-form-urlencoded模式，此模式下空格会编码为'+'
     * @return 参数Map
     */
    public static Map<String, List<String>> decodeParams(String paramsStr, Charset charset, boolean isFormUrlEncoded) {
        final Map<CharSequence, CharSequence> queryMap =
                UrlQuery.of(paramsStr, charset, true, isFormUrlEncoded).getQueryMap();
        if (MapUtil.isEmpty(queryMap)) {
            return MapUtil.empty();
        }

        final Map<String, List<String>> params = new LinkedHashMap<>();
        queryMap.forEach((key, value) -> {
            final List<String> values = params.computeIfAbsent(StrUtil.str(key), k -> new ArrayList<>(1));

            values.add(StrUtil.str(value));
        });
        return params;
    }

    /**
     * 将表单数据加到URL中（用于GET表单提交）<br>
     * 表单的键值对会被url编码，但是url中原参数不会被编码
     *
     * @param url            URL
     * @param form           表单数据
     * @param charset        编码
     * @param isEncodeParams 是否对键和值做转义处理
     * @return 合成后的URL
     */
    public static String urlWithForm(String url, Map<String, Object> form, Charset charset, boolean isEncodeParams) {
        if (isEncodeParams && StrUtil.contains(url, '?')) {

            url = encodeParams(url, charset);
        }


        return urlWithForm(url, toParams(form, charset), charset, false);
    }

    /**
     * 将表单数据字符串加到URL中（用于GET表单提交）
     *
     * @param url         URL
     * @param queryString 表单数据字符串
     * @param charset     编码
     * @param isEncode    是否对键和值做转义处理
     * @return 拼接后的字符串
     */
    public static String urlWithForm(String url, String queryString, Charset charset, boolean isEncode) {
        if (StrUtil.isBlank(queryString)) {

            if (StrUtil.contains(url, '?')) {

                return isEncode ? encodeParams(url, charset) : url;
            }
            return url;
        }


        final StrBuilder urlBuilder = StrBuilder.create(url.length() + queryString.length() + 16);
        int qmIndex = url.indexOf('?');
        if (qmIndex > 0) {

            urlBuilder.append(isEncode ? encodeParams(url, charset) : url);
            if (false == StrUtil.endWith(url, '&')) {

                urlBuilder.append('&');
            }
        } else {

            urlBuilder.append(url);
            if (qmIndex < 0) {

                urlBuilder.append('?');
            }
        }
        urlBuilder.append(isEncode ? encodeParams(queryString, charset) : queryString);
        return urlBuilder.toString();
    }

    /**
     * 从Http连接的头信息中获得字符集<br>
     * 从ContentType中获取
     *
     * @param conn HTTP连接对象
     * @return 字符集
     */
    public static String getCharset(HttpURLConnection conn) {
        if (conn == null) {
            return null;
        }
        return getCharset(conn.getContentType());
    }

    /**
     * 从Http连接的头信息中获得字符集<br>
     * 从ContentType中获取
     *
     * @param contentType Content-Type
     * @return 字符集
     *
     */
    public static String getCharset(String contentType) {
        if (StrUtil.isBlank(contentType)) {
            return null;
        }
        return ReUtil.get(CHARSET_PATTERN, contentType, 1);
    }

    /**
     * 从流中读取内容<br>
     * 首先尝试使用charset编码读取内容（如果为空默认UTF-8），如果isGetCharsetFromContent为true，则通过正则在正文中获取编码信息，转换为指定编码；
     *
     * @param in                      输入流
     * @param charset                 字符集
     * @param isGetCharsetFromContent 是否从返回内容中获得编码信息
     * @return 内容
     */
    public static String getString(InputStream in, Charset charset, boolean isGetCharsetFromContent) {
        final byte[] contentBytes = IoUtil.readBytes(in);
        return getString(contentBytes, charset, isGetCharsetFromContent);
    }

    /**
     * 从流中读取内容<br>
     * 首先尝试使用charset编码读取内容（如果为空默认UTF-8），如果isGetCharsetFromContent为true，则通过正则在正文中获取编码信息，转换为指定编码；
     *
     * @param contentBytes            内容byte数组
     * @param charset                 字符集
     * @param isGetCharsetFromContent 是否从返回内容中获得编码信息
     * @return 内容
     */
    public static String getString(byte[] contentBytes, Charset charset, boolean isGetCharsetFromContent) {
        if (null == contentBytes) {
            return null;
        }

        if (null == charset) {
            charset = CharsetUtil.CHARSET_UTF_8;
        }
        String content = new String(contentBytes, charset);
        if (isGetCharsetFromContent) {
            final String charsetInContentStr = ReUtil.get(META_CHARSET_PATTERN, content, 1);
            if (StrUtil.isNotBlank(charsetInContentStr)) {
                Charset charsetInContent = null;
                try {
                    charsetInContent = Charset.forName(charsetInContentStr);
                } catch (Exception e) {
                    if (StrUtil.containsIgnoreCase(charsetInContentStr, "utf-8") || StrUtil.containsIgnoreCase(charsetInContentStr, "utf8")) {
                        charsetInContent = CharsetUtil.CHARSET_UTF_8;
                    } else if (StrUtil.containsIgnoreCase(charsetInContentStr, "gbk")) {
                        charsetInContent = CharsetUtil.CHARSET_GBK;
                    }

                }
                if (null != charsetInContent && false == charset.equals(charsetInContent)) {
                    content = new String(contentBytes, charsetInContent);
                }
            }
        }
        return content;
    }

    /**
     * 根据文件扩展名获得MimeType
     *
     * @param filePath     文件路径或文件名
     * @param defaultValue 当获取MimeType为null时的默认值
     * @return MimeType
     * @see FileUtil#getMimeType(String)
     *
     */
    public static String getMimeType(String filePath, String defaultValue) {
        return ObjectUtil.defaultIfNull(getMimeType(filePath), defaultValue);
    }

    /**
     * 根据文件扩展名获得MimeType
     *
     * @param filePath 文件路径或文件名
     * @return MimeType
     * @see FileUtil#getMimeType(String)
     */
    public static String getMimeType(String filePath) {
        return FileUtil.getMimeType(filePath);
    }

    /**
     * 从请求参数的body中判断请求的Content-Type类型，支持的类型有：
     *
     * <pre>
     * 1. application/json
     * 1. application/xml
     * </pre>
     *
     * @param body 请求参数体
     * @return Content-Type类型，如果无法判断返回null
     * @see ContentType#get(String)
     *
     */
    public static String getContentTypeByRequestBody(String body) {
        final ContentType contentType = ContentType.get(body);
        return (null == contentType) ? null : contentType.toString();
    }

    /**
     * 创建简易的Http服务器
     *
     * @param port 端口
     * @return {@link SimpleServer}
     *
     */
    public static SimpleServer createServer(int port) {
        return new SimpleServer(port);
    }

    /**
     * 构建简单的账号秘密验证信息，构建后类似于：
     * <pre>
     *     Basic YWxhZGRpbjpvcGVuc2VzYW1l
     * </pre>
     *
     * @param username 账号
     * @param password 密码
     * @param charset  编码（如果账号或密码中有非ASCII字符适用）
     * @return 密码验证信息
     *
     */
    public static String buildBasicAuth(String username, String password, Charset charset) {
        final String data = username.concat(":").concat(password);
        return "Basic " + Base64.encode(data, charset);
    }

    /**
     * 关闭Cookie
     *
     * @see GlobalCookieManager#setCookieManager(CookieManager)
     *
     */
    public static void closeCookie() {
        GlobalCookieManager.setCookieManager(null);
    }
}
