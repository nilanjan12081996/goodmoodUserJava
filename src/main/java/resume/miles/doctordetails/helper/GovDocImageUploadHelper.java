package resume.miles.doctordetails.helper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import resume.miles.customeAnnotation.Helper;

@Helper
public class GovDocImageUploadHelper {
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/image/doctor/govmentdoc";
    
    // 10 MB in bytes (10 * 1024 * 1024)
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; 
    
    // Allowed MIME types
    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png");

    public String saveFile(MultipartFile file) throws IOException {
        
        // --- VALIDATION START ---

        // 1. Check if file is empty
        if (file.isEmpty()) {
            throw new IOException("Cannot save empty file.");
        }

        // 2. Validate File Size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("File size exceeds the maximum limit of 10MB.");
        }

        // 3. Validate File Type (MIME type)
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new IOException("Invalid file type. Only JPEG and PNG are allowed.");
        }

        // --- VALIDATION END ---


        // Create directory path
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Long timestamp = System.currentTimeMillis();
        
        // Generate unique filename
        String originalFilename = file.getOriginalFilename();
        
        // Safety check for null filename
        if (originalFilename == null) {
            throw new IOException("Original filename is null.");
        }
        
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String uniqueFilename = "govid" + timestamp + fileExtension;

        // Save file
        Path filePath = uploadPath.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // Return URL path (without 'static')
        return "/uploads/image/doctor/govmentdoc/" + uniqueFilename;
    }

    public void deleteFile(String filePath) {
        try {
            String projectRoot = System.getProperty("user.dir"); 
            if (filePath.startsWith("\\") || filePath.startsWith("/")) {
                filePath = filePath.substring(1);
            }
            Path fullPath = Paths.get(projectRoot, "src", "main", "resources", "static", filePath);

            System.out.println("Attempting to delete: " + fullPath.toAbsolutePath());

            boolean deleted = Files.deleteIfExists(fullPath);
            
            if (!deleted) {
                System.out.println("File not found or could not be deleted: " + fullPath);
            } else {
                System.out.println("Successfully deleted!");
            }

        } catch (IOException e) {
            throw new RuntimeException("Could not delete file: " + filePath, e);
        }
    }
}
