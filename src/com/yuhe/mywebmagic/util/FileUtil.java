package com.yuhe.mywebmagic.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 读写文件的工具类
 * 
 * @author heyuxing
 */
public class FileUtil {
	private static Log LOG = LogFactory.getLog(FileUtil.class);
	public static final String ENTER_NEWLINE = System.getProperty("line.separator");
	public static final String PATH_SEPARATOR = "/";
	private final static int BUFFER_SIZE = 4096;

	/**
	 * 获得配置文件（*.properties）中指定属性的值，默认值为""
	 * 
	 * @param fileName
	 *            类目下的文件名，如com.ablesky.payment.test.SystemConfigResources
	 * @param key
	 * @return
	 */
	public static String getConfigInfomation(String fileName, String key) {
		try {
			ResourceBundle resource = ResourceBundle.getBundle(fileName);
			return resource.getString(key);
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获得配置文件（*.properties）中指定属性的值
	 * 
	 * @param filePath
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getConfigInfomation(String filePath, String key, String defaultValue) {
		InputStream input = null;
		try {
			input = new BufferedInputStream(new FileInputStream(filePath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return getConfigInfomation(input, key, defaultValue);
	}

	/**
	 * 获得配置文件（*.properties）中指定属性的值
	 * 
	 * @param inputStream
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getConfigInfomation(InputStream inputStream, String key, String defaultValue) {
		Properties props = new Properties();
		try {
			props.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return props.getProperty(key, defaultValue);
	}

	/**
	 * 通过文件名获得源文件根目录下或tomcat的conf目录下的配置文件的InputStream
	 * 
	 * @param fileNmae
	 * @return
	 */
	public static InputStream getInputStream(String fileNmae) {
		InputStream inputStream = null;
		try {
			File file = new File(System.getProperty("catalina.home") + "/conf/" + fileNmae);
			if (file.exists()) {
				inputStream = new FileInputStream(file);
			} else {
				/**
				 * use default config
				 */
				inputStream = FileUtil.class.getClassLoader().getResourceAsStream(fileNmae);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;
	}

	/**
	 * 获得递归查找目录（路径为baseUrl）下的名称为fileName的所有文件，返回文件路径pathList
	 * 
	 * @autor: hyx Aug 5, 2013 3:40:51 PM
	 * @param baseUrl
	 * @return
	 * @return String[]
	 */
	public static void findFilePath(File file, final String fileName, List<String> pathList) {
		if (file.exists()) {
			if (file.isFile()) {
				if (file.getName().endsWith(fileName)) { // 找到跳出
					pathList.add(file.getPath());
				}
			} else {
				File[] files = file.listFiles();
				if (files != null) {
					for (File childFile : files) {
						if (childFile.exists()) {
							if (childFile.isFile()) {
								if (childFile.getName().endsWith(fileName)) {
									pathList.add(childFile.getPath());
								}
							} else {
								findFilePath(childFile, fileName, pathList);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 获得递归查找目录（路径为baseUrl）下的名称为fileName的所有文件，返回文件路径pathList
	 * 
	 * @autor: hyx Aug 5, 2013 3:40:51 PM
	 * @param baseUrl
	 * @return
	 * @return String[]
	 */
	public static void findLikeFilePath(File file, final String likeFileName, List<String> pathList) { // TODO
																										// 改正则
		if (file.exists()) {
			if (file.isFile()) {
				if (file.getName().contains(likeFileName) && file.getName().endsWith("gz")) { // 找到跳出
					pathList.add(file.getPath());
				}
			} else {
				File[] files = file.listFiles();
				if (files != null) {
					for (File childFile : files) {
						if (childFile.exists()) {
							if (childFile.isFile()) {
								if (childFile.getName().contains(likeFileName) && childFile.getName().endsWith("gz")) {
									pathList.add(childFile.getPath());
								}
							} else {
								findLikeFilePath(childFile, likeFileName, pathList);
							}
						}
					}
				}
			}
		}
	}

	public static byte[] load(String url) throws IOException, URISyntaxException {
		File file = new File(url);
		if (!file.exists()) {
			return null;
		}
		FileInputStream reader = new FileInputStream(file);
		byte[] buffer = new byte[reader.available()];
		reader.read(buffer);
		reader.close();
		return buffer;
	}

	public static String loadFile(String path) {
		String str = "";
		try {
			byte[] b = load(path);
			str = new String(b, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 创建文件夹.
	 * 
	 * @param directoryPath
	 *            文件夹路径.
	 */
	public static void makeDirectory(String directoryPath) {
		File directory = new File(directoryPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}
	}

	/**
	 * 读取二进制文件中的字节.
	 * 
	 * @param filePath
	 *            文件路径.
	 * @return 字节数组.
	 * @throws Exception
	 */
	public static byte[] getBytesFromFile(File file) throws Exception {
		if (file != null && file.exists()) {
			FileInputStream in = new FileInputStream(file);
			int fileSize = Integer.valueOf(String.valueOf(file.length()));
			ByteArrayOutputStream bout = new ByteArrayOutputStream(fileSize);
			copy(in, bout);
			return bout.toByteArray();
		} else {
			LOG.info(file + " does not exist");
			return null;
		}
	}

	/**
	 * 读取二进制文件中的字节.
	 * 
	 * @param filePath
	 *            文件路径.
	 * @return 字节数组.
	 * @throws Exception
	 */
	public static byte[] getBytesFromFilePath(String filePath) throws Exception {
		if (filePath != null && !"".equals(filePath)) {
			File file = new File(filePath);
			return getBytesFromFile(file);
		} else {
			return null;
		}
	}

	/**
	 * 生成二进制文件.
	 * 
	 * @param bytes
	 *            字节数组.
	 * @param filePath
	 *            生成文件的路径.
	 * @throws Exception
	 */
	public static File writeBytesToFile(byte[] bytes, String filePath) throws Exception {
		if (filePath != null && !"".equals(filePath)) {
			File file = new File(filePath);
			if (!file.exists()) {
				FileOutputStream out = new FileOutputStream(file);
				out.write(bytes);
				out.flush();
				out.close();
			}
			return file;
		} else {
			return null;
		}
	}

	public static int copy(InputStream in, OutputStream out) throws IOException {
		try {
			int byteCount = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}

	public static void ZipFiles(java.io.File[] srcfile, String zipFilePath) {
		byte[] buf = new byte[1024];
		try {
			// Create the ZIP file
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFilePath));
			// Compress the files
			for (int i = 0; i < srcfile.length; i++) {
				FileInputStream in = new FileInputStream(srcfile[i]);
				// Add ZIP entry to output stream.
				out.putNextEntry(new ZipEntry(srcfile[i].getName()));
				// Transfer bytes from the file to the ZIP file
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				// Complete the entry
				out.closeEntry();
				in.close();
			}
			// Complete the ZIP file
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 复制文件
	public static void copyFile(File sourceFile, File targetFile) throws IOException {
		BufferedInputStream inBuff = null;
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输入流并对它进行缓冲
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			// 关闭流
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	/**
	 * 解压缩文件.
	 * 
	 * @param filePath
	 *            gz文件路径.
	 */
	public static void gunzipFile(String filePath) throws Exception {
		File file = new File(filePath);
		if (file.exists()) {
			String os = System.getProperty("os.name");
			if (os.toLowerCase().startsWith("win")) {
				LOG.info(os + " can't gunzip " + filePath);
			} else {
				Runtime.getRuntime().exec("gunzip " + filePath);
			}
		} else {
			LOG.info(filePath + " does not exist");
		}
	}

	public ByteArrayOutputStream parse(InputStream in) throws Exception {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		int ch;
		while ((ch = in.read()) != -1) {
			swapStream.write(ch);
		}
		return swapStream;
	}

	public ByteArrayInputStream parse(OutputStream out) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		baos = (ByteArrayOutputStream) out;
		ByteArrayInputStream swapStream = new ByteArrayInputStream(baos.toByteArray());
		return swapStream;
	}

	public static void writeFile(String fileName, String filePath, String content) {
		try {
			// 判断文件是否存在，不存在创建
			File f = new File(filePath + fileName);
			if (!f.exists()) {
				File f2 = new File(filePath);
				f2.mkdirs();
				f.createNewFile();
			}
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(f), "UTF-8");
			outputStreamWriter.write(content);
			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void appendFileContent(String fileName, String content) {
		try {
			// 打开文件读写器，构建参数true以追加形式写文件
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 文件删除原来的目录.并创建一个新的目录.
	 * 
	 * @param filePath
	 * @throws Exception
	 */
	public static void deleteDirectory(String filePath) throws Exception {
		File dirfile = new File(filePath);
		File[] files = dirfile.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				// 删除子文件
				if (files[i].isFile() && files[i].exists()) {
					files[i].delete();
				}
			}
		}
		// 删除子目录
		if (dirfile.exists() & dirfile.isDirectory()) {
			FileUtils.deleteDirectory(dirfile);
		}
		File f = new File(filePath);
		f.mkdirs();
	}

	/**
	 * 将正文写入指定文件(.txt,.xml等),如果有存在的目录，先进行目录删除.
	 * 
	 * @param fileaName
	 * @param filePath
	 * @param content
	 */
	public static void writeNewFile(String fileName, String filePath, String content) throws Exception {
		File file = new File(filePath + File.separator + fileName);
		OutputStreamWriter outWrite = null;
		try {
			outWrite = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			outWrite.write(content);
			outWrite.flush();
			outWrite.close();
		} catch (Exception e) {
			throw e;
		} finally {
			if (outWrite != null) {
				outWrite.close();
			}
		}
	}

	/**
	 * 文件内容是否为空
	 * 
	 * @param path
	 * @return
	 */
	public static boolean isExistContentForFile(String path) throws IOException {
		File f = new File(path);
		if (f.exists()) {
			if (f.length() > 0) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static File inputstreamToFile(InputStream ins, File file) {
		OutputStream os = null;
		try {
			os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				os.close();
				ins.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(loadFile("C:/webmagiclog/edu.ablesky.com/11c488ac86bf9d01ee6aed5c72c03b79.html"));
	}

}
