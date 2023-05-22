package com.spring.cloud.base.utils.utils;

import com.spring.cloud.base.utils.LimitedInputStream;
import com.spring.cloud.base.utils.ZipCopyVisitor;
import com.spring.cloud.base.utils.ZipReader;
import com.spring.cloud.base.utils.ZipWriter;
import com.spring.cloud.base.utils.crypto.ObjectUtil;
import com.spring.cloud.base.utils.exception.IORuntimeException;
import com.spring.cloud.base.utils.exception.UtilException;
import com.spring.cloud.base.utils.map.EnumerationIter;
import com.spring.cloud.base.utils.map.Resource;
import com.spring.cloud.base.utils.str.StrUtil;
import com.spring.cloud.base.utils.utils.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @Author: ls
 * @Description: 压缩工具类
 * @Date: 2023/4/13 16:11
 */
public class ZipUtil {

	private static final int DEFAULT_BYTE_ARRAY_LENGTH = 32;

	/**
	 * 默认编码，使用平台相关编码
	 */
	private static final Charset DEFAULT_CHARSET = CharsetUtil.defaultCharset();

	/**
	 * 获取Zip文件中指定目录下的所有文件，只显示文件，不显示目录<br>
	 * 此方法并不会关闭{@link ZipFile}。
	 *
	 * @param zipFile Zip文件
	 * @param dir     目录前缀（目录前缀不包含开头的/）
	 * @return 文件列表
	 */
	public static List<String> listFileNames(ZipFile zipFile, String dir) {
		if (StrUtil.isNotBlank(dir)) {
			// 目录尾部添加"/"
			dir = StrUtil.addSuffixIfNot(dir, StrUtil.SLASH);
		}

		final List<String> fileNames = new ArrayList<>();
		String name;
		for (ZipEntry entry : new EnumerationIter<>(zipFile.entries())) {
			name = entry.getName();
			if (StrUtil.isEmpty(dir) || name.startsWith(dir)) {
				final String nameSuffix = StrUtil.removePrefix(name, dir);
				if (StrUtil.isNotEmpty(nameSuffix) && false == StrUtil.contains(nameSuffix, CharUtil.SLASH)) {
					fileNames.add(nameSuffix);
				}
			}
		}

		return fileNames;
	}

	/**
	 * 将Zip文件转换为{@link ZipFile}
	 *
	 * @param file    zip文件
	 * @param charset 解析zip文件的编码，null表示{@link CharsetUtil#CHARSET_UTF_8}
	 * @return {@link ZipFile}
	 */
	public static ZipFile toZipFile(File file, Charset charset) {
		try {
			return new ZipFile(file, ObjectUtil.defaultIfNull(charset, CharsetUtil.CHARSET_UTF_8));
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取指定{@link ZipEntry}的流，用于读取这个entry的内容<br>
	 * 此处使用{@link LimitedInputStream} 限制最大写出大小，避免ZIP bomb漏洞
	 *
	 * @param zipFile  {@link ZipFile}
	 * @param zipEntry {@link ZipEntry}
	 * @return 流
	 * 
	 */
	public static InputStream getStream(ZipFile zipFile, ZipEntry zipEntry) {
		try {
			return new LimitedInputStream(zipFile.getInputStream(zipEntry), zipEntry.getSize());
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获得 {@link ZipOutputStream}
	 *
	 * @param out     压缩文件流
	 * @param charset 编码
	 * @return {@link ZipOutputStream}
	 * 
	 */
	public static ZipOutputStream getZipOutputStream(OutputStream out, Charset charset) {
		if (out instanceof ZipOutputStream) {
			return (ZipOutputStream) out;
		}
		return new ZipOutputStream(out, charset);
	}

	/**
	 * 在zip文件中添加新文件或目录<br>
	 * 新文件添加在zip根目录，文件夹包括其本身和内容<br>
	 * 如果待添加文件夹是系统根路径（如/或c:/），则只复制文件夹下的内容
	 *
	 * @param zipPath        zip文件的Path
	 * @param appendFilePath 待添加文件Path(可以是文件夹)
	 * @param options        拷贝选项，可选是否覆盖等
	 * @throws IORuntimeException IO异常
	 * 
	 */
	public static void append(Path zipPath, Path appendFilePath, CopyOption... options) throws IORuntimeException {
		try (FileSystem zipFileSystem = FileSystemUtil.createZip(zipPath.toString())) {
			if (Files.isDirectory(appendFilePath)) {
				Path source = appendFilePath.getParent();
				if (null == source) {
					// 如果用户提供的是根路径，则不复制目录，直接复制目录下的内容
					source = appendFilePath;
				}
				Files.walkFileTree(appendFilePath, new ZipCopyVisitor(source, zipFileSystem, options));
			} else {
				Files.copy(appendFilePath, zipFileSystem.getPath(PathUtil.getName(appendFilePath)), options);
			}
		} catch (FileAlreadyExistsException ignored) {
			// 不覆盖情况下，文件已存在, 跳过
		} catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 打包到当前目录，使用默认编码UTF-8
	 *
	 * @param srcPath 源文件路径
	 * @return 打包好的压缩文件
	 * @throws UtilException IO异常
	 */
	public static File zip(String srcPath) throws UtilException {
		return zip(srcPath, DEFAULT_CHARSET);
	}

	/**
	 * 打包到当前目录
	 *
	 * @param srcPath 源文件路径
	 * @param charset 编码
	 * @return 打包好的压缩文件
	 * @throws UtilException IO异常
	 */
	public static File zip(String srcPath, Charset charset) throws UtilException {
		return zip(FileUtil.file(srcPath), charset);
	}

	/**
	 * 打包到当前目录，使用默认编码UTF-8
	 *
	 * @param srcFile 源文件或目录
	 * @return 打包好的压缩文件
	 * @throws UtilException IO异常
	 */
	public static File zip(File srcFile) throws UtilException {
		return zip(srcFile, DEFAULT_CHARSET);
	}

	/**
	 * 打包到当前目录
	 *
	 * @param srcFile 源文件或目录
	 * @param charset 编码
	 * @return 打包好的压缩文件
	 * @throws UtilException IO异常
	 */
	public static File zip(File srcFile, Charset charset) throws UtilException {
		final File zipFile = FileUtil.file(srcFile.getParentFile(), FileUtil.mainName(srcFile) + ".zip");
		zip(zipFile, charset, false, srcFile);
		return zipFile;
	}

	/**
	 * 对文件或文件目录进行压缩<br>
	 * 不包含被打包目录
	 *
	 * @param srcPath 要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @param zipPath 压缩文件保存的路径，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @return 压缩好的Zip文件
	 * @throws UtilException IO异常
	 */
	public static File zip(String srcPath, String zipPath) throws UtilException {
		return zip(srcPath, zipPath, false);
	}

	/**
	 * 对文件或文件目录进行压缩<br>
	 *
	 * @param srcPath    要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @param zipPath    压缩文件保存的路径，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param withSrcDir 是否包含被打包目录
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 */
	public static File zip(String srcPath, String zipPath, boolean withSrcDir) throws UtilException {
		return zip(srcPath, zipPath, DEFAULT_CHARSET, withSrcDir);
	}

	/**
	 * 对文件或文件目录进行压缩<br>
	 *
	 * @param srcPath    要压缩的源文件路径。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @param zipPath    压缩文件保存的路径，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param charset    编码
	 * @param withSrcDir 是否包含被打包目录
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 */
	public static File zip(String srcPath, String zipPath, Charset charset, boolean withSrcDir) throws UtilException {
		final File srcFile = FileUtil.file(srcPath);
		final File zipFile = FileUtil.file(zipPath);
		zip(zipFile, charset, withSrcDir, srcFile);
		return zipFile;
	}

	/**
	 * 对文件或文件目录进行压缩<br>
	 * 使用默认UTF-8编码
	 *
	 * @param zipFile    生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param withSrcDir 是否包含被打包目录，只针对压缩目录有效。若为false，则只压缩目录下的文件或目录，为true则将本目录也压缩
	 * @param srcFiles   要压缩的源文件或目录。
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 */
	public static File zip(File zipFile, boolean withSrcDir, File... srcFiles) throws UtilException {
		return zip(zipFile, DEFAULT_CHARSET, withSrcDir, srcFiles);
	}

	/**
	 * 对文件或文件目录进行压缩
	 *
	 * @param zipFile    生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param charset    编码
	 * @param withSrcDir 是否包含被打包目录，只针对压缩目录有效。若为false，则只压缩目录下的文件或目录，为true则将本目录也压缩
	 * @param srcFiles   要压缩的源文件或目录。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 */
	public static File zip(File zipFile, Charset charset, boolean withSrcDir, File... srcFiles) throws UtilException {
		return zip(zipFile, charset, withSrcDir, null, srcFiles);
	}

	/**
	 * 对文件或文件目录进行压缩
	 *
	 * @param zipFile    生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param charset    编码
	 * @param withSrcDir 是否包含被打包目录，只针对压缩目录有效。若为false，则只压缩目录下的文件或目录，为true则将本目录也压缩
	 * @param filter     文件过滤器，通过实现此接口，自定义要过滤的文件（过滤掉哪些文件或文件夹不加入压缩）
	 * @param srcFiles   要压缩的源文件或目录。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @return 压缩文件
	 * @throws IORuntimeException IO异常
	 * 
	 */
	public static File zip(File zipFile, Charset charset, boolean withSrcDir, FileFilter filter, File... srcFiles) throws IORuntimeException {
		validateFiles(zipFile, srcFiles);
		//noinspection resource
		ZipWriter.of(zipFile, charset).add(withSrcDir, filter, srcFiles).close();
		return zipFile;
	}

	/**
	 * 对文件或文件目录进行压缩
	 *
	 * @param out        生成的Zip到的目标流，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param charset    编码
	 * @param withSrcDir 是否包含被打包目录，只针对压缩目录有效。若为false，则只压缩目录下的文件或目录，为true则将本目录也压缩
	 * @param filter     文件过滤器，通过实现此接口，自定义要过滤的文件（过滤掉哪些文件或文件夹不加入压缩）
	 * @param srcFiles   要压缩的源文件或目录。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @throws IORuntimeException IO异常
	 * 
	 */
	public static void zip(OutputStream out, Charset charset, boolean withSrcDir, FileFilter filter, File... srcFiles) throws IORuntimeException {
		ZipWriter.of(out, charset).add(withSrcDir, filter, srcFiles).close();
	}

	/**
	 * 对文件或文件目录进行压缩
	 *
	 * @param zipOutputStream 生成的Zip到的目标流，自动关闭此流
	 * @param withSrcDir      是否包含被打包目录，只针对压缩目录有效。若为false，则只压缩目录下的文件或目录，为true则将本目录也压缩
	 * @param filter          文件过滤器，通过实现此接口，自定义要过滤的文件（过滤掉哪些文件或文件夹不加入压缩）
	 * @param srcFiles        要压缩的源文件或目录。如果压缩一个文件，则为该文件的全路径；如果压缩一个目录，则为该目录的顶层目录路径
	 * @throws IORuntimeException IO异常
	 *
	 * @deprecated 请使用 {@link #zip(OutputStream, Charset, boolean, FileFilter, File...)}
	 */
	@Deprecated
	public static void zip(ZipOutputStream zipOutputStream, boolean withSrcDir, FileFilter filter, File... srcFiles) throws IORuntimeException {
		try (final ZipWriter zipWriter = new ZipWriter(zipOutputStream)) {
			zipWriter.add(withSrcDir, filter, srcFiles);
		}
	}

	/**
	 * 对流中的数据加入到压缩文件，使用默认UTF-8编码
	 *
	 * @param zipFile 生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param path    流数据在压缩文件中的路径或文件名
	 * @param data    要压缩的数据
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 * 
	 */
	public static File zip(File zipFile, String path, String data) throws UtilException {
		return zip(zipFile, path, data, DEFAULT_CHARSET);
	}

	/**
	 * 对流中的数据加入到压缩文件<br>
	 *
	 * @param zipFile 生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param path    流数据在压缩文件中的路径或文件名
	 * @param data    要压缩的数据
	 * @param charset 编码
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 * 
	 */
	public static File zip(File zipFile, String path, String data, Charset charset) throws UtilException {
		return zip(zipFile, path, IoUtil.toStream(data, charset), charset);
	}

	/**
	 * 对流中的数据加入到压缩文件<br>
	 * 使用默认编码UTF-8
	 *
	 * @param zipFile 生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param path    流数据在压缩文件中的路径或文件名
	 * @param in      要压缩的源
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 * 
	 */
	public static File zip(File zipFile, String path, InputStream in) throws UtilException {
		return zip(zipFile, path, in, DEFAULT_CHARSET);
	}

	/**
	 * 对流中的数据加入到压缩文件
	 *
	 * @param zipFile 生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param path    流数据在压缩文件中的路径或文件名
	 * @param in      要压缩的源，默认关闭
	 * @param charset 编码
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 * 
	 */
	public static File zip(File zipFile, String path, InputStream in, Charset charset) throws UtilException {
		return zip(zipFile, new String[]{path}, new InputStream[]{in}, charset);
	}

	/**
	 * 对流中的数据加入到压缩文件<br>
	 * 路径列表和流列表长度必须一致
	 *
	 * @param zipFile 生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param paths   流数据在压缩文件中的路径或文件名
	 * @param ins     要压缩的源，添加完成后自动关闭流
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 * 
	 */
	public static File zip(File zipFile, String[] paths, InputStream[] ins) throws UtilException {
		return zip(zipFile, paths, ins, DEFAULT_CHARSET);
	}

	/**
	 * 对流中的数据加入到压缩文件<br>
	 * 路径列表和流列表长度必须一致
	 *
	 * @param zipFile 生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param paths   流数据在压缩文件中的路径或文件名
	 * @param ins     要压缩的源，添加完成后自动关闭流
	 * @param charset 编码
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 * 
	 */
	public static File zip(File zipFile, String[] paths, InputStream[] ins, Charset charset) throws UtilException {
		try (final ZipWriter zipWriter = ZipWriter.of(zipFile, charset)) {
			zipWriter.add(paths, ins);
		}

		return zipFile;
	}

	/**
	 * 将文件流压缩到目标流中
	 *
	 * @param out   目标流，压缩完成自动关闭
	 * @param paths 流数据在压缩文件中的路径或文件名
	 * @param ins   要压缩的源，添加完成后自动关闭流
	 * 
	 */
	public static void zip(OutputStream out, String[] paths, InputStream[] ins) {
		zip(getZipOutputStream(out, DEFAULT_CHARSET), paths, ins);
	}

	/**
	 * 将文件流压缩到目标流中
	 *
	 * @param zipOutputStream 目标流，压缩完成自动关闭
	 * @param paths           流数据在压缩文件中的路径或文件名
	 * @param ins             要压缩的源，添加完成后自动关闭流
	 * @throws IORuntimeException IO异常
	 * 
	 */
	public static void zip(ZipOutputStream zipOutputStream, String[] paths, InputStream[] ins) throws IORuntimeException {
		try (final ZipWriter zipWriter = new ZipWriter(zipOutputStream)) {
			zipWriter.add(paths, ins);
		}
	}

	/**
	 * 对流中的数据加入到压缩文件<br>
	 * 路径列表和流列表长度必须一致
	 *
	 * @param zipFile   生成的Zip文件，包括文件名。注意：zipPath不能是srcPath路径下的子文件夹
	 * @param charset   编码
	 * @param resources 需要压缩的资源，资源的路径为{@link Resource#getName()}
	 * @return 压缩文件
	 * @throws UtilException IO异常
	 * 
	 */
	public static File zip(File zipFile, Charset charset, Resource... resources) throws UtilException {
		//noinspection resource
		ZipWriter.of(zipFile, charset).add(resources).close();
		return zipFile;
	}

	// ---------------------------------------------------------------------------------------------- Unzip

	/**
	 * 解压到文件名相同的目录中，默认编码UTF-8
	 *
	 * @param zipFilePath 压缩文件路径
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 */
	public static File unzip(String zipFilePath) throws UtilException {
		return unzip(zipFilePath, DEFAULT_CHARSET);
	}

	/**
	 * 解压到文件名相同的目录中
	 *
	 * @param zipFilePath 压缩文件路径
	 * @param charset     编码
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 * 
	 */
	public static File unzip(String zipFilePath, Charset charset) throws UtilException {
		return unzip(FileUtil.file(zipFilePath), charset);
	}

	/**
	 * 解压到文件名相同的目录中，使用UTF-8编码
	 *
	 * @param zipFile 压缩文件
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 * 
	 */
	public static File unzip(File zipFile) throws UtilException {
		return unzip(zipFile, DEFAULT_CHARSET);
	}

	/**
	 * 解压到文件名相同的目录中
	 *
	 * @param zipFile 压缩文件
	 * @param charset 编码
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 * 
	 */
	public static File unzip(File zipFile, Charset charset) throws UtilException {
		final File destDir = FileUtil.file(zipFile.getParentFile(), FileUtil.mainName(zipFile));
		return unzip(zipFile, destDir, charset);
	}

	/**
	 * 解压，默认UTF-8编码
	 *
	 * @param zipFilePath 压缩文件的路径
	 * @param outFileDir  解压到的目录
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 */
	public static File unzip(String zipFilePath, String outFileDir) throws UtilException {
		return unzip(zipFilePath, outFileDir, DEFAULT_CHARSET);
	}

	/**
	 * 解压
	 *
	 * @param zipFilePath 压缩文件的路径
	 * @param outFileDir  解压到的目录
	 * @param charset     编码
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 */
	public static File unzip(String zipFilePath, String outFileDir, Charset charset) throws UtilException {
		return unzip(FileUtil.file(zipFilePath), FileUtil.mkdir(outFileDir), charset);
	}

	/**
	 * 解压，默认使用UTF-8编码
	 *
	 * @param zipFile zip文件
	 * @param outFile 解压到的目录
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 */
	public static File unzip(File zipFile, File outFile) throws UtilException {
		return unzip(zipFile, outFile, DEFAULT_CHARSET);
	}

	/**
	 * 解压
	 *
	 * @param zipFile zip文件
	 * @param outFile 解压到的目录
	 * @param charset 编码
	 * @return 解压的目录
	 * 
	 */
	public static File unzip(File zipFile, File outFile, Charset charset) {
		return unzip(toZipFile(zipFile, charset), outFile);
	}

	/**
	 * 解压
	 *
	 * @param zipFile zip文件，附带编码信息，使用完毕自动关闭
	 * @param outFile 解压到的目录
	 * @return 解压的目录
	 * @throws IORuntimeException IO异常
	 * 
	 */
	public static File unzip(ZipFile zipFile, File outFile) throws IORuntimeException {
		return unzip(zipFile, outFile, -1);
	}

	/**
	 * 限制解压后文件大小
	 *
	 * @param zipFile zip文件，附带编码信息，使用完毕自动关闭
	 * @param outFile 解压到的目录
	 * @param limit   限制解压文件大小(单位B)
	 * @return 解压的目录
	 * @throws IORuntimeException IO异常
	 * 
	 */
	public static File unzip(ZipFile zipFile, File outFile, long limit) throws IORuntimeException {
		if (outFile.exists() && outFile.isFile()) {
			throw new IllegalArgumentException(
					StrUtil.format("Target path [{}] exist!", outFile.getAbsolutePath()));
		}

		// pr#726@Gitee
		if (limit > 0) {
			final Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
			long zipFileSize = 0L;
			while (zipEntries.hasMoreElements()) {
				final ZipEntry zipEntry = zipEntries.nextElement();
				zipFileSize += zipEntry.getSize();
				if (zipFileSize > limit) {
					throw new IllegalArgumentException("The file size exceeds the limit");
				}
			}
		}

		try (final ZipReader reader = new ZipReader(zipFile)) {
			reader.readTo(outFile);
		}
		return outFile;
	}

	/**
	 * 获取压缩包中的指定文件流
	 *
	 * @param zipFile 压缩文件
	 * @param charset 编码
	 * @param path    需要提取文件的文件名或路径
	 * @return 压缩文件流，如果未找到返回{@code null}
	 * 
	 */
	public static InputStream get(File zipFile, Charset charset, String path) {
		return get(toZipFile(zipFile, charset), path);
	}

	/**
	 * 获取压缩包中的指定文件流
	 *
	 * @param zipFile 压缩文件
	 * @param path    需要提取文件的文件名或路径
	 * @return 压缩文件流，如果未找到返回{@code null}
	 * 
	 */
	public static InputStream get(ZipFile zipFile, String path) {
		final ZipEntry entry = zipFile.getEntry(path);
		if (null != entry) {
			return getStream(zipFile, entry);
		}
		return null;
	}

	/**
	 * 读取并处理Zip文件中的每一个{@link ZipEntry}
	 *
	 * @param zipFile  Zip文件
	 * @param consumer {@link ZipEntry}处理器
	 * 
	 */
	public static void read(ZipFile zipFile, Consumer<ZipEntry> consumer) {
		try (final ZipReader reader = new ZipReader(zipFile)) {
			reader.read(consumer);
		}
	}

	/**
	 * 解压<br>
	 * ZIP条目不使用高速缓冲。
	 *
	 * @param in      zip文件流，使用完毕自动关闭
	 * @param outFile 解压到的目录
	 * @param charset 编码
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 * 
	 */
	public static File unzip(InputStream in, File outFile, Charset charset) throws UtilException {
		if (null == charset) {
			charset = DEFAULT_CHARSET;
		}
		return unzip(new ZipInputStream(in, charset), outFile);
	}

	/**
	 * 解压<br>
	 * ZIP条目不使用高速缓冲。
	 *
	 * @param zipStream zip文件流，包含编码信息
	 * @param outFile   解压到的目录
	 * @return 解压的目录
	 * @throws UtilException IO异常
	 * 
	 */
	public static File unzip(ZipInputStream zipStream, File outFile) throws UtilException {
		try (final ZipReader reader = new ZipReader(zipStream)) {
			reader.readTo(outFile);
		}
		return outFile;
	}

	/**
	 * 读取并处理Zip流中的每一个{@link ZipEntry}
	 *
	 * @param zipStream zip文件流，包含编码信息
	 * @param consumer  {@link ZipEntry}处理器
	 * 
	 */
	public static void read(ZipInputStream zipStream, Consumer<ZipEntry> consumer) {
		try (final ZipReader reader = new ZipReader(zipStream)) {
			reader.read(consumer);
		}
	}

	/**
	 * 从Zip文件中提取指定的文件为bytes
	 *
	 * @param zipFilePath Zip文件
	 * @param name        文件名，如果存在于子文件夹中，此文件名必须包含目录名，例如images/aaa.txt
	 * @return 文件内容bytes
	 * 
	 */
	public static byte[] unzipFileBytes(String zipFilePath, String name) {
		return unzipFileBytes(zipFilePath, DEFAULT_CHARSET, name);
	}

	/**
	 * 从Zip文件中提取指定的文件为bytes
	 *
	 * @param zipFilePath Zip文件
	 * @param charset     编码
	 * @param name        文件名，如果存在于子文件夹中，此文件名必须包含目录名，例如images/aaa.txt
	 * @return 文件内容bytes
	 * 
	 */
	public static byte[] unzipFileBytes(String zipFilePath, Charset charset, String name) {
		return unzipFileBytes(FileUtil.file(zipFilePath), charset, name);
	}

	/**
	 * 从Zip文件中提取指定的文件为bytes
	 *
	 * @param zipFile Zip文件
	 * @param name    文件名，如果存在于子文件夹中，此文件名必须包含目录名，例如images/aaa.txt
	 * @return 文件内容bytes
	 * 
	 */
	public static byte[] unzipFileBytes(File zipFile, String name) {
		return unzipFileBytes(zipFile, DEFAULT_CHARSET, name);
	}

	/**
	 * 从Zip文件中提取指定的文件为bytes
	 *
	 * @param zipFile Zip文件
	 * @param charset 编码
	 * @param name    文件名，如果存在于子文件夹中，此文件名必须包含目录名，例如images/aaa.txt
	 * @return 文件内容bytes
	 * 
	 */
	public static byte[] unzipFileBytes(File zipFile, Charset charset, String name) {
		try (final ZipReader reader = ZipReader.of(zipFile, charset)) {
			return IoUtil.readBytes(reader.get(name));
		}
	}

	// ---------------------------------------------------------------------------------------------- Private method start

	/**
	 * 判断压缩文件保存的路径是否为源文件路径的子文件夹，如果是，则抛出异常（防止无限递归压缩的发生）
	 *
	 * @param zipFile  压缩后的产生的文件路径
	 * @param srcFiles 被压缩的文件或目录
	 */
	private static void validateFiles(File zipFile, File... srcFiles) throws UtilException {
		if (zipFile.isDirectory()) {
			throw new UtilException("Zip file [{}] must not be a directory !", zipFile.getAbsoluteFile());
		}

		for (File srcFile : srcFiles) {
			if (null == srcFile) {
				continue;
			}
			if (false == srcFile.exists()) {
				throw new UtilException(StrUtil.format("File [{}] not exist!", srcFile.getAbsolutePath()));
			}

			// issue#1961@Github
			// 当 zipFile =  new File("temp.zip") 时, zipFile.getParentFile() == null
			File parentFile;
			try {
				parentFile = zipFile.getCanonicalFile().getParentFile();
			} catch (IOException e) {
				parentFile = zipFile.getParentFile();
			}

			// 压缩文件不能位于被压缩的目录内
			if (srcFile.isDirectory() && FileUtil.isSub(srcFile, parentFile)) {
				throw new UtilException("Zip file path [{}] must not be the child directory of [{}] !", zipFile.getPath(), srcFile.getPath());
			}
		}
	}
	// ---------------------------------------------------------------------------------------------- Private method end

}
