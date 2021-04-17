package info.moroff.tools.archivepatcher;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import info.moroff.base.logging.Utils;

public class JavaArchivePatcher {
	Logger logger;
	String tempFolder;
	
	
	public JavaArchivePatcher() {
		logger = Utils.setupAndCreateLogger(getClass());
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public FileSystem open(String earFileName) throws IOException {
		File tempFile = new File(tempFolder);
		Path earFilePath = Paths.get(earFileName);
		File earFile = earFilePath.toFile();

		if ( !earFile.exists() ) {
			throw new IllegalArgumentException(earFile.getAbsolutePath()+" does not exists");
		}
		
		if ( !tempFile.exists() ) {
			tempFile.mkdirs();
		}
		return open(URI.create("file:"+earFile.getAbsolutePath()));
	}

	public FileSystem open(URI earFileURI) throws IOException {
		logger.fine("open "+earFileURI.toString());
		Map<String, String> env = new HashMap<>();
		
		return FileSystems.newFileSystem(URI.create("jar:"+earFileURI.toString()), env);
	}
	
	public void update(String earFileName) {
		try {
			FileSystem earFS = open(earFileName);
			
			for (Path earElement : earFS.getRootDirectories()) {
				logger.fine(earElement.toString());
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			
		}
	}
	
	public void list(String earFileName) {
		try {
			FileSystem earFS = open(earFileName);
			
			list(earFS);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			
		}
	}

	public void list(FileSystem earFS) throws IOException {
		for (Path earElement : earFS.getRootDirectories()) {
			logger.fine(earElement.toUri().toString());
			Files.walk(earElement).forEach((p) -> {
				URI elementUri = p.toUri();
				String pathName = elementUri.toString();
				logger.fine(pathName);	
				if ( pathName.endsWith(".jar") || pathName.endsWith(".war")) {
					try {
						Path path = Paths.get(tempFolder, p.getFileName().toString());
						Files.copy(p, path, StandardCopyOption.REPLACE_EXISTING);
						list(open(path.toUri()));
					} catch (IOException e) {
						logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
			});
		}
	}

	public void setTempFolder(String tempFolder) {
		this.tempFolder = tempFolder;
	}	
	public String getTempFolder() {
		return tempFolder;
	}
}
