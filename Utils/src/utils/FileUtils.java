package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.LogManager;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtils {

	public static void copyFolder(String src, String dest) {
		try {

			File srcFolder = new File(src);
			File destFolder = new File(dest);

			// make sure source exists
			if (!srcFolder.exists()) {
				throw new RuntimeException("Directory does not exist.");
			} else {
				try {
					copyFolder(srcFolder, destFolder);
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void copyFolder(File src, File dest) throws IOException {
		try {
			if (src.isDirectory()) {

				// if directory not exists, create it
				if (!dest.exists()) {
					dest.mkdir();
				}

				// list all the directory contents
				String files[] = src.list();

				for (String file : files) {
					// construct the src and dest file structure
					File srcFile = new File(src, file);
					File destFile = new File(dest, file);
					// recursive copy
					copyFolder(srcFile, destFile);
				}

			} else {
				copyFile(src, dest);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void copyFile(File src, File dest) {
		try {
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void moveFile(String src, String dest) {
		try {

			File srcFile = new File(src);
			File destFile = new File(dest);

			// make sure source exists
			if (!srcFile.exists()) {
				throw new RuntimeException("File does not exist.");
			} else {
				moveFile(srcFile, destFile);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void moveFile(File src, File dest) {
		try {
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest);

			byte[] buffer = new byte[1024];

			int length;
			// copy the file content in bytes
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
			src.delete();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static void deleteFolder(String folder, boolean onlycontents) {
		try {

			File srcFolder = new File(folder);
			// make sure source exists
			if (srcFolder.exists()) {
				try {
					deleteFolder(srcFolder, onlycontents);
				} catch (IOException ex) {
					throw new RuntimeException(ex);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static void deleteFolder(File folder, boolean onlycontents) throws IOException {
		try {
			File[] files = folder.listFiles();
			if (files != null) {
				for (File f : files) {
					if (f.isDirectory()) {
						deleteFolder(f, onlycontents);
					} else {
						f.delete();
					}
				}
			}
			if (!onlycontents)
				folder.delete();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static boolean zipFiles(ArrayList<String> files, String zipFile) {
		boolean end = false;

		try {

			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (String file : files) {
				addToZipFile(file, zos);
			}

			zos.close();
			fos.close();

			end = true;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return end;
	}
	
	
	
	public static void unZip(String zipFile, String outputFolder) {

		byte[] buffer = new byte[1024];

		try {

			// create output directory is not exists
			File folder = new File(outputFolder);
			if (!folder.exists()) {
				folder.mkdir();
			}

			// get the zip file content
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);

				// create all non exists folders
				// else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);

				int len;
				while ((len = zis.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}

				fos.close();
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();

		} catch (IOException ex) {
			LogManager.error(ex.getMessage());
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}

	}
	
	
	
	private static void addToZipFile(String fileName, ZipOutputStream zos) throws FileNotFoundException, IOException {

		LogManager.debug("Writing '" + fileName + "' to zip file");

		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		// Path path = Paths.get(fileName);
		String zName = fileName.substring(fileName.lastIndexOf("/") + 1);

		if (zName.contains("tiff")) {
			zName = "ballots/" + zName;
		}

		if (zName.contains("pdf")) {
			zName = "reports/" + zName;
		}

		if (!zName.contains("jpeg")) {

			ZipEntry zipEntry = new ZipEntry(zName);
			zos.putNextEntry(zipEntry);

			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zos.write(bytes, 0, length);
			}

			zos.closeEntry();
			fis.close();
		}
	}

	public static boolean write2File(String fileName, String jsong) {

		BufferedWriter writer = null;
		try {

			File logFile = new File(fileName);

			writer = new BufferedWriter(new FileWriter(logFile));
			writer.write(jsong);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Close the writer regardless of what happens...
				writer.close();
			} catch (Exception e) {
			}
		}

		return false;

	}

	public static String getFileNameFromPath(String pPath) {
		try {
			String[] split = pPath.split("/");
			int lastIndex = split.length - 1;
			return split[lastIndex];
		} catch (Exception ex) {
			return ""; // Controlar el error
		}
	}

	public static void validateDirectory(String path)
	{

		File theDir = new File(path);
		if (!theDir.exists()) {

			LogManager.debug("creating directory: " + path);

			boolean result = false;

			try{
				theDir.mkdir();
				result = true;
			} catch(SecurityException se){
				//handle it
			}        
			if(result) {    
				LogManager.debug(path + " created");  
			}
		}
	}

	public static byte[] readAllBytes(String filexmlenc) {

		FileInputStream fileInputStream=null;

		File file = new File(filexmlenc);

		byte[] bFile = null;

		try {

			bFile = new byte[(int) file.length()];
			//convert file into array of bytes
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}

		return bFile;
	}

	public static String readAllText(File file) {

		StringBuilder text = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				text.append(line);
				//text.append('\n');
			}
			br.close();
		}
		catch (IOException e) {
			//You'll need to add proper error handling here
		}

		return text.toString();
	}

}
