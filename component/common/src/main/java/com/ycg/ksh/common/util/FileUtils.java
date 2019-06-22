package com.ycg.ksh.common.util;

import com.ycg.ksh.common.constant.Directory;
import com.ycg.ksh.common.system.SystemUtils;
import com.ycg.ksh.common.util.encrypt.DES;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * 文件操作工具类
 * <p>
 * 
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
 *            2017-09-15 10:57:26
 */
public class FileUtils {

	private static final String SEPARATOR = "/";

	private static final String[] IMAGE_SUFFIX = new String[] { "jpeg", "gif", "png", "jpg", "jgp", "bmp" };

	private static final char LINK = '.';

	public static final String PDF_SUFFIX = "pdf";

	public static final String ZIP_SUFFIX = "zip";

	public static final String XLS_SUFFIX = "xls";
    public static final String XLSX_SUFFIX = "xlsx";


	public static final String DOCX_SUFFIX = "docx";
	public static final String DOC_SUFFIX = "doc";

	/**
	 * 1KB的字节数
	 */
	public static final int ONE_KB = 1024;

	/**
	 * 1M的字节数
	 */
	public static final long ONE_MB = ONE_KB * ONE_KB;

	/**
	 * 30M的字节数
	 */
	private static final long FILE_COPY_BUFFER_SIZE = ONE_MB * 30;

	public static double size(long length, long unit) {
		return new Double(new DecimalFormat("0.0000").format(new Double(length) / unit));
	}

	public static String buildDownload(String filePath, String fileName) {
		return buildDownload(filePath, fileName, true);
	}

	public static String buildDownload(String filePath, String fileName, boolean delete) {
		try {
			String request = SystemUtils.buildUrl(SystemUtils.getCallBackDomain(), "/backstage/special/download");
			return request + "?p=" + DES.encrypt(filePath +"&"+ fileName+"&"+ delete, SystemUtils.secretKey());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String mobileDownload(String filePath, String fileName, boolean delete) {
		try {
			String request = SystemUtils.buildUrl(SystemUtils.getCallBackDomain(), "/mobile/system/special/download");
			return request + "?p=" + DES.encrypt(filePath +"&"+ fileName+"&"+ delete, SystemUtils.secretKey());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String[] download(String desdata) {
		if (StringUtils.isNotBlank(desdata)) {
			try {
				String source = DES.decrypt(desdata, SystemUtils.secretKey());
				if (StringUtils.isNotBlank(source)) {
					return source.split("&");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String encode(String source) {
		try {
			return URLEncoder.encode(source, "utf8");
		} catch (UnsupportedEncodingException e) {
		}
		return source;
	}

	public static String decode(String source) {
		try {
			return URLDecoder.decode(source, "utf8");
		} catch (UnsupportedEncodingException e) {
		}
		return source;
	}

	/**
	 * 去掉路径的前后 /
	 */
	public static String normalize(String path) {
		return after(before(path));
	}

	public static String before(String path) {
		String temp = path;
		if (path.startsWith("/") || path.startsWith("\\")) {
			temp = temp.substring(1);
			return normalize(temp);
		}
		return temp;
	}

	public static String after(String path) {
		String temp = path;
		if (path.endsWith("/") || path.endsWith("\\")) {
			temp = temp.substring(temp.length() - 1);
			return normalize(temp);
		}
		return temp;
	}

	/**
	 * 将两个路径合并
	 * <p>
	 * 
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
	 *            2017-09-15 11:11:39
	 * @param parent
	 * @param sub
	 * @return
	 */
	public static String path(String parent, String sub) {
		return after(parent) + SEPARATOR + normalize(sub);
	}

	/**
	 * 获取文件后缀
	 * <p>
	 * 
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
	 *            2017-09-15 11:13:59
	 * @param filename
	 * @return
	 */
	public static String suffix(String filename) {
		int s = filename.lastIndexOf(LINK);
		if (s > -1) {
			return filename.substring(s + 1);
		}
		return null;
	}

	public static String name(String filename) {
		int s = filename.indexOf(LINK);
		if (s > -1) {
			return filename.substring(0, s);
		}
		return filename;
	}

	public static String appendSuffix(String filename, String suffix) {
		return filename + LINK + suffix;
	}

	/**
	 * 根据文件路径获取文件，如果文件不存在则创建一个
	 * <p>
	 * 
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
	 *            2017-09-15 10:57:41
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static File file(String filePath) throws IOException {
		return file(new File(filePath));
	}

	/**
	 * 根据文件路径获取文件
	 * <p>
	 * 
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
	 *            2017-09-15 10:58:13
	 * @param filePath
	 *            文件路径
	 * @param create
	 *            true:如果文件不存在就创建一个， false:如果文件不存在不做操作
	 * @return
	 * @throws IOException
	 */
	public static File file(String filePath, boolean create) throws IOException {
		File file = new File(filePath);
		if (create) {
			return file(file);
		}
		return file;
	}
	
	public static File file(String parent, String filePath) throws IOException {
		return file(new File(parent, filePath));
	}
	
	public static File file(File parent, String filePath) throws IOException {
		return file(new File(parent, filePath));
	}
	
	/**
	 * 获取文件信息，如果文件不存在就创建一个
	 * <p>
	 * 
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
	 *            2017-09-15 10:58:59
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static File file(File file) throws IOException {
		if (file != null && !file.exists()) {
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			file.createNewFile();
		}
		return file;
	}

	public static File newFile(File file) throws IOException {
		if (file != null) {
			File parent = file.getParentFile();
			if (!parent.exists()) {
				parent.mkdirs();
			}
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
		}
		return file;
	}

	public static File newFile(String filePath) throws IOException {
		return newFile(new File(filePath));
	}

	public static File newFile(String parent, String fileName) throws IOException {
		return newFile(new File(parent, fileName));
	}

	/**
	 * 根据文件夹路径获取文件夹，如果文件夹不存在则创建一个
	 * <p>
	 * 
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
	 *            2017-09-15 10:57:41
	 * @param directoryPath
	 * @return
	 * @throws IOException
	 */
	public static File directory(String directoryPath) throws IOException {
		return directory(new File(directoryPath));
	}

	/**
	 * 根据文件夹路径获取文件夹
	 * <p>
	 * 
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
	 *            2017-09-15 10:58:13
	 * @param directoryPath
	 *            文件夹路径
	 * @param create
	 *            true:如果文件夹不存在就创建一个， false:如果文件夹不存在不做操作
	 * @return
	 * @throws IOException
	 */
	public static File directory(String directoryPath, boolean create) throws IOException {
		File file = new File(directoryPath);
		if (create) {
			return directory(file);
		}
		return file;
	}

	/**
	 * 获取文件夹信息，如果文件夹不存在就创建一个
	 * <p>
	 * 
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
	 *            2017-09-15 10:58:59
	 * @param directory
	 * @return
	 * @throws IOException
	 */
	public static File directory(File directory) throws IOException {
		if (directory != null) {
			if (!directory.exists() || !directory.isDirectory()) {
				directory.mkdirs();
			}
		}
		return directory;
	}

	public static void forceDelete(File file) throws IOException {
		if (file.isDirectory()) {
			deleteDirectory(file);
		} else {
			file.delete();
		}
	}

	public static void cleanDirectory(File directory) throws IOException {
		if (!directory.exists() || !directory.isDirectory()) {
			return;
		}
		File[] files = directory.listFiles();
		if (files != null) { // null if security restricted
			for (File file : files) {
				forceDelete(file);
			}
		}
	}

	public static void deleteDirectory(String directory) throws IOException {
        deleteDirectory(new File(directory));
    }
	
	public static void deleteDirectory(File directory) throws IOException {
		if (!directory.exists()) {
			return;
		}
		cleanDirectory(directory);
		directory.delete();
	}

	public static int copyDirectoryToDirectory(File srcDir, File destDir, boolean override) throws IOException {
		if (destDir.exists() && !destDir.isDirectory()) {
			throw new IOException("目标文件 '" + destDir + "' 不是文件夹");
		}
		if (!srcDir.exists()) {
			throw new IOException("源文件 '" + srcDir + "' 不存在");
		}
		if (srcDir.getPath().equals(destDir.getPath())) {
			throw new IOException("源文件 '" + srcDir + "' 和 目标文件 '" + destDir + "' 相同");
		}
		int count = 0;
		destDir = file(destDir);
		if (srcDir.isDirectory()) {
			File[] files = srcDir.listFiles();
			if (files != null && files.length > 0) {
				for (File file : files) {
					if (file.isDirectory()) {
						count += copyDirectoryToDirectory(file, directory(new File(destDir, srcDir.getName())),
								override);
					} else {
						if (copyFileToDirectory(file, destDir, override) != null) {
							count++;
						}
					}
				}
			}
		} else {
			if (copyFileToDirectory(srcDir, destDir, override) != null) {
				count++;
			}
		}
		return count;
	}

	public static File copyFileToDirectory(File srcFile, File destDir, boolean override) throws IOException {
		File destFile = null;
		if (override) {
			destFile = new File(destDir, srcFile.getName());
			if (destFile.exists()) {
				destFile.delete();
			}
			destFile.createNewFile();
		} else {
			destFile = heavyName(destDir, srcFile.getName());
		}
		copyFile(srcFile, destFile);
		return destFile;
	}

	public static File heavyName(File destDir, String fileName) {
		File file = new File(destDir, fileName);
		if (file.exists()) {
			int count = 0;
			do {
				++count;
				file = new File(destDir, appendSuffix(name(fileName) + "_" + count, suffix(fileName)));
			} while (file.exists());
		}
		return file;
	}

	public static void copyFile(File srcFile, File destFile) throws IOException {
		if (destFile.exists() && destFile.isDirectory()) {
			throw new IOException("目标文件 '" + destFile + "' 不是文件而是文件夹");
		}
		if (srcFile.getPath().equals(destFile.getPath())) {
			throw new IOException("源文件 '" + srcFile + "' 和 目标文件 '" + destFile + "' 相同");
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		FileChannel input = null;
		FileChannel output = null;
		try {
			fis = new FileInputStream(srcFile);
			fos = new FileOutputStream(destFile);
			input = fis.getChannel();
			output = fos.getChannel();
			long size = input.size(), pos = 0, count = 0;
			while (pos < size) {
				count = Math.min(size - pos, FILE_COPY_BUFFER_SIZE);
				pos += output.transferFrom(input, pos, count);
			}
		} finally {
			IOUtils.closeQuietly(output, fos, input, fis);
		}
		if (srcFile.length() != destFile.length()) {
			throw new IOException("文件没有复制完整:'" + srcFile + "'[" + srcFile.length() + "] => '" + destFile + "'["
					+ destFile.length() + "]");
		}
		destFile.setLastModified(srcFile.lastModified());
	}

	public static File zip(File srcFile, File destDir) {
		return zip(srcFile, destDir, false);
	}

	public static File zip(File srcFile, File destFile, boolean override) {
		if (srcFile.exists()) {
			File[] files = srcFile.isDirectory() ? srcFile.listFiles() : new File[]{srcFile};
			FileOutputStream fos = null;
			ZipOutputStream zos = null;
			try {
				if (destFile.isDirectory()) {
					String zipName = appendSuffix(name(srcFile.getName()), ZIP_SUFFIX);
					if (override) {
						destFile = newFile(destFile.getPath(), zipName);
					} else {
						destFile = file(path(destFile.getPath(), zipName), true);
					}
				}
				fos = new FileOutputStream(destFile);
				zos = new ZipOutputStream(new BufferedOutputStream(fos));
				for (File file : files) {
					addEntry("/", file, zos);// 添加对应的文件Entry
				}
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(zos, fos);
			}
		}
		return destFile;
	}




	public static File zip(String srcPath, String destDir) {
		return zip(new File(srcPath), new File(destDir));
	}

	/**
	 * 压缩文件
	 * <p>
	 * 
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
	 *            2017-09-15 14:47:32
	 * @param filePath
	 *            待压缩的文件路径
	 * @return 压缩后的文件
	 */
	public static File zip(String filePath) {
		File source = new File(filePath);
		return zip(source, source.getParentFile());
	}

	/**
	 * 扫描添加文件Entry
	 * <p>
	 * 
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
	 *            2017-09-15 14:55:18
	 * @param base
	 *            基路径
	 * @param source
	 *            源文件
	 * @param zos
	 *            Zip文件输出流
	 * @throws IOException
	 */
	private static void addEntry(String base, File source, ZipOutputStream zos) throws IOException {
		// 按目录分级，形如：/aaa/bbb.txt
		String entry = base + source.getName();
		if (source.isDirectory()) {
			for (File file : source.listFiles()) {
				// 递归列出目录下的所有文件，添加文件Entry
				addEntry(entry + "/", file, zos);
			}
		} else {
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				byte[] buffer = new byte[1024 * 10];
				fis = new FileInputStream(source);
				bis = new BufferedInputStream(fis, buffer.length);
				int read = 0;
				zos.putNextEntry(new ZipEntry(entry));
				while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
					zos.write(buffer, 0, read);
				}
				zos.closeEntry();
			} finally {
				IOUtils.closeQuietly(bis, fis);
			}
		}
	}

	public static void unzip(File srcFile, File destDir) {
		if (srcFile.exists()) {
			ZipInputStream zis = null;
			BufferedOutputStream bos = null;
			try {
				zis = new ZipInputStream(new FileInputStream(srcFile));
				ZipEntry entry = null;
				while ((entry = zis.getNextEntry()) != null && !entry.isDirectory()) {
					File target = file(path(destDir.getPath(), entry.getName()), true);
					// 写入文件
					bos = new BufferedOutputStream(new FileOutputStream(target));
					int read = 0;
					byte[] buffer = new byte[ONE_KB * 10];
					while ((read = zis.read(buffer, 0, buffer.length)) != -1) {
						bos.write(buffer, 0, read);
					}
					bos.flush();
				}
				zis.closeEntry();
			} catch (IOException e) {
				throw new RuntimeException(e);
			} finally {
				IOUtils.closeQuietly(zis, bos);
			}
		}
	}

	public static void unzip(String srcPath, String destDir) {
		unzip(new File(srcPath), new File(destDir));
	}

	/**
	 * 解压文件
	 * <p>
	 * 
	 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at
	 *            2017-09-15 14:55:07
	 * @param filePath
	 *            压缩文件路径
	 */
	public static void unzip(String filePath) {
		File source = new File(filePath);
		unzip(source, source.getParentFile());
	}

	public static boolean isImageByPath(String path) {
		return isImage(suffix(path));
	}

	public static boolean isImage(String suffix) {
		if (StringUtils.isBlank(suffix)) {
			return false;
		}
		for (String _suffix : IMAGE_SUFFIX) {
			if (StringUtils.equalsIgnoreCase(suffix, _suffix)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isExcel(String suffix) {
        return XLS_SUFFIX.equalsIgnoreCase(suffix) || XLSX_SUFFIX.equalsIgnoreCase(suffix);
    }

}
