package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.http.Part;

/**
 * Utility class for file upload operations
 */
public class FileUploadUtil {

  private static final Logger LOGGER = Logger.getLogger(FileUploadUtil.class.getName());

  // Private constructor to prevent instantiation
  private FileUploadUtil() {
    throw new IllegalStateException("Utility class");
  }

  /**
   * Saves an uploaded file to the specified directory
   * 
   * @param filePart  The uploaded file part
   * @param uploadDir The directory to save the file to
   * @return The relative path to the saved file
   * @throws IOException If an I/O error occurs
   */
  public static String saveFile(Part filePart, String uploadDir) throws IOException {
    // Create directory if it doesn't exist
    File uploadDirFile = new File(uploadDir);
    if (!uploadDirFile.exists()) {
      uploadDirFile.mkdirs();
    }

    // Generate unique filename to prevent overwriting
    String originalFilename = getSubmittedFileName(filePart);
    String fileExtension = originalFilename.substring(originalFilename.lastIndexOf('.'));
    String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

    // Save file
    Path filePath = Paths.get(uploadDir, uniqueFilename);
    Files.copy(filePart.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

    // Return the relative path for storing in DB
    return "/images/blogs/" + uniqueFilename;
  }

  /**
   * Get the submitted filename from a Part
   * 
   * @param part The uploaded file part
   * @return The original filename
   */
  private static String getSubmittedFileName(Part part) {
    String contentDisp = part.getHeader("content-disposition");
    String[] items = contentDisp.split(";");

    for (String item : items) {
      if (item.trim().startsWith("filename")) {
        return item.substring(item.indexOf('=') + 2, item.length() - 1);
      }
    }

    return "";
  }

  /**
   * Deletes a file from the specified path
   * 
   * @param filePath The path to the file
   * @return true if deletion was successful, false otherwise
   */
  public static boolean deleteFile(String filePath) {
    try {
      Path path = Paths.get(filePath);
      Files.delete(path);
      return true;
    } catch (IOException | SecurityException e) {
      LOGGER.log(Level.WARNING, "Error deleting file: {0}", e.getMessage());
      return false;
    }
  }
}
