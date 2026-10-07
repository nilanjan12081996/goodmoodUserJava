package resume.miles.userregister.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
public class FileService {

    private final Path fileStorageLocation;

    public FileService(@Value("${file.upload-dir:./uploads}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        return storeFile(file, null, null, null, null);
    }

    public String storeFile(MultipartFile file, Integer cropX, Integer cropY, Integer cropWidth, Integer cropHeight) {
        // Normalize file name
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileName = "";

        try {
            // Check if the file's name contains invalid characters
            if (originalFileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + originalFileName);
            }

            // Generate a unique file name
            String extension = "";
            int i = originalFileName.lastIndexOf('.');
            if (i > 0) {
                extension = originalFileName.substring(i);
            }
            fileName = UUID.randomUUID().toString() + extension;

            Path targetLocation = this.fileStorageLocation.resolve(fileName);

            // Perform 1:1 aspect ratio crop if coordinates are provided
            if (cropX != null && cropY != null && cropWidth != null && cropHeight != null && cropWidth > 0 && cropHeight > 0) {
                try {
                    java.awt.image.BufferedImage originalImg = javax.imageio.ImageIO.read(file.getInputStream());
                    if (originalImg != null) {
                        int imgW = originalImg.getWidth();
                        int imgH = originalImg.getHeight();
                        int x = Math.max(0, Math.min(cropX, imgW - 1));
                        int y = Math.max(0, Math.min(cropY, imgH - 1));
                        int w = Math.min(cropWidth, imgW - x);
                        int h = Math.min(cropHeight, imgH - y);

                        if (w > 10 && h > 10) {
                            java.awt.image.BufferedImage croppedImg = originalImg.getSubimage(x, y, w, h);
                            int targetDim = Math.min(Math.max(Math.max(w, h), 400), 1080);
                            java.awt.image.BufferedImage squareImg = new java.awt.image.BufferedImage(targetDim, targetDim, java.awt.image.BufferedImage.TYPE_INT_RGB);
                            java.awt.Graphics2D g = squareImg.createGraphics();
                            g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                            g.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
                            g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                            g.drawImage(croppedImg, 0, 0, targetDim, targetDim, null);
                            g.dispose();

                            String formatName = extension.toLowerCase().contains("png") ? "png" : "jpg";
                            javax.imageio.ImageIO.write(squareImg, formatName, targetLocation.toFile());
                            return "/uploads/" + fileName;
                        }
                    }
                } catch (Exception ex) {
                    System.err.println("Warning: Multipart cropping failed, falling back to full image: " + ex.getMessage());
                }
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/" + fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public String storeBase64(String base64Data, String originalFilename, Integer cropX, Integer cropY, Integer cropWidth, Integer cropHeight) {
        if (base64Data == null || base64Data.trim().isEmpty()) {
            throw new RuntimeException("Cannot save empty image data.");
        }

        if (base64Data.contains(",")) {
            base64Data = base64Data.substring(base64Data.indexOf(",") + 1);
        }

        byte[] decodedBytes;
        try {
            decodedBytes = java.util.Base64.getDecoder().decode(base64Data.trim());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid Base64 image data.");
        }

        String extension = ".jpg";
        if (originalFilename != null) {
            int i = originalFilename.lastIndexOf('.');
            if (i > 0) {
                extension = originalFilename.substring(i);
            }
        }

        String fileName = UUID.randomUUID().toString() + extension;
        Path targetLocation = this.fileStorageLocation.resolve(fileName);

        // Perform 1:1 aspect ratio crop if coordinates are provided
        if (cropX != null && cropY != null && cropWidth != null && cropHeight != null && cropWidth > 0 && cropHeight > 0) {
            try {
                java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(decodedBytes);
                java.awt.image.BufferedImage originalImg = javax.imageio.ImageIO.read(bis);
                if (originalImg != null) {
                    int imgW = originalImg.getWidth();
                    int imgH = originalImg.getHeight();
                    int x = Math.max(0, Math.min(cropX, imgW - 1));
                    int y = Math.max(0, Math.min(cropY, imgH - 1));
                    int w = Math.min(cropWidth, imgW - x);
                    int h = Math.min(cropHeight, imgH - y);

                    if (w > 10 && h > 10) {
                        java.awt.image.BufferedImage croppedImg = originalImg.getSubimage(x, y, w, h);
                        int targetDim = Math.min(Math.max(Math.max(w, h), 400), 1080);
                        java.awt.image.BufferedImage squareImg = new java.awt.image.BufferedImage(targetDim, targetDim, java.awt.image.BufferedImage.TYPE_INT_RGB);
                        java.awt.Graphics2D g = squareImg.createGraphics();
                        g.setRenderingHint(java.awt.RenderingHints.KEY_INTERPOLATION, java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                        g.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);
                        g.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                        g.drawImage(croppedImg, 0, 0, targetDim, targetDim, null);
                        g.dispose();

                        String formatName = extension.toLowerCase().contains("png") ? "png" : "jpg";
                        javax.imageio.ImageIO.write(squareImg, formatName, targetLocation.toFile());
                        return "/uploads/" + fileName;
                    }
                }
            } catch (Exception ex) {
                System.err.println("Warning: Cropping failed, saving original decoded bytes: " + ex.getMessage());
            }
        }

        try {
            Files.write(targetLocation, decodedBytes);
            return "/uploads/" + fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }
}
