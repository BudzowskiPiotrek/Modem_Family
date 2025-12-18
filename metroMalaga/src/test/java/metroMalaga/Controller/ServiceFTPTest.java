package metroMalaga.Controller;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.net.ftp.FTPFile;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class ServiceFTPTest {

	private ServiceFTP service;
	private static final String TEST_USER_ROLE = "admin"; // Based on your ConnectionFTP logic
	private static final String TEST_FOLDER = "JUnit_Test_Folder";
	private File tempFile;

	@BeforeEach
	public void setUp() throws IOException {
		// 1. Initialize service (Ensure FileZilla is running!)
		service = new ServiceFTP(TEST_USER_ROLE);

		// 2. Create a local temporary file for upload tests
		tempFile = File.createTempFile("test_upload", ".txt");
		try (FileWriter writer = new FileWriter(tempFile)) {
			writer.write("Hello Metro Malaga - JUnit Test Content");
		}
	}

	@AfterEach
	public void tearDown() {
		// Cleanup: Delete the test folder and close connection
		if (service != null) {
			service.deleteFile(TEST_FOLDER);
			service.close();
		}
		if (tempFile != null && tempFile.exists()) {
			tempFile.delete();
		}
	}

	@Test
	public void testConnectionEstablished() {
		assertNotNull(service, "Service should be initialized");
		// Indirectly check connection by listing files
		assertDoesNotThrow(() -> service.listAllFiles());
	}

	@Test
	public void testMakeDirectory() throws IOException {
		boolean created = service.makeDirectory(TEST_FOLDER);
		assertTrue(created, "Folder should be created successfully");

		// Verify folder exists in the list
		FTPFile[] files = service.listAllFiles();
		boolean found = false;
		for (FTPFile f : files) {
			if (f.getName().equals(TEST_FOLDER) && f.isDirectory()) {
				found = true;
				break;
			}
		}
		assertTrue(found, "The created folder was not found on the server");
	}

	@Test
	public void testMakeDirectoryDuplicateThrowsException() throws IOException {
		service.makeDirectory(TEST_FOLDER);

		// Your ServiceFTP throws an IOException with "DIRECTORY_EXISTS"
		IOException exception = assertThrows(IOException.class, () -> {
			service.makeDirectory(TEST_FOLDER);
		});

		assertEquals("DIRECTORY_EXISTS", exception.getMessage());
	}

	@Test
	public void testUploadAndDownloadFile() throws IOException {
		String remoteName = "junit_upload.txt";

		// 1. Test Upload
		boolean uploaded = service.uploadFile(tempFile.getAbsolutePath(), remoteName);
		assertTrue(uploaded, "File should upload successfully");

		// 2. Test Download
		File downloadDest = new File("downloaded_junit.txt");
		boolean downloaded = service.downloadFile(remoteName, downloadDest.getAbsolutePath());

		assertTrue(downloaded, "File should download successfully");
		assertTrue(downloadDest.exists(), "Downloaded file should exist locally");

		// Cleanup local download
		Files.deleteIfExists(downloadDest.toPath());
		// Cleanup remote file
		service.deleteFile(remoteName);
	}

	@Test
	public void testChangeDirectory() throws IOException {
		service.makeDirectory(TEST_FOLDER);

		boolean moved = service.changeDirectory(TEST_FOLDER);
		assertTrue(moved, "Should be able to enter the created directory");

		String current = service.getCurrentDirectory();
		assertTrue(current.contains(TEST_FOLDER), "Current directory path should contain the test folder name");

		// Go back up
		boolean movedUp = service.changeDirectoryUp();
		assertTrue(movedUp, "Should be able to return to parent directory");
	}

	@Test
	public void testRenameFile() throws IOException {
		String oldName = "to_rename.txt";
		String newName = "renamed_ok.txt";

		service.uploadFile(tempFile.getAbsolutePath(), oldName);

		boolean renamed = service.renameFile(oldName, newName);
		assertTrue(renamed, "Rename operation failed");

		// Verify deletion of old and existence of new
		FTPFile[] files = service.listAllFiles();
		boolean foundOld = false;
		boolean foundNew = false;

		for (FTPFile f : files) {
			if (f.getName().equals(oldName))
				foundOld = true;
			if (f.getName().equals(newName))
				foundNew = true;
		}

		assertFalse(foundOld, "Old file name should not exist");
		assertTrue(foundNew, "New file name should exist");

		// Cleanup
		service.deleteFile(newName);
	}

	@Test
	public void testCalculateDirectorySize() throws IOException {
		service.makeDirectory(TEST_FOLDER);
		service.changeDirectory(TEST_FOLDER);

		// Upload same file twice inside folder
		service.uploadFile(tempFile.getAbsolutePath(), "file1.txt");
		service.uploadFile(tempFile.getAbsolutePath(), "file2.txt");

		service.changeDirectoryUp();

		long size = service.calculateDirectorySize(TEST_FOLDER);
		assertTrue(size > 0, "Directory size should be greater than 0");
		assertEquals(tempFile.length() * 2, size, "Size should be exactly the sum of the two files");
	}
}
