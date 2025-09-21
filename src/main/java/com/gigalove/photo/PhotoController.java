package com.gigalove.photo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    private final PhotoRepository photoRepository;

    @Value("${gigalove.upload.dir:uploads}")
    private String uploadDir;

    public PhotoController(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    private Long currentUserId() {
        try { return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()); } catch (Exception e) { return 1L; }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Photo> upload(@PathVariable Long userId, @RequestParam("file") MultipartFile file,
                                        @RequestParam(value = "sortOrder", required = false) Integer sortOrder) throws IOException {
        if (!userId.equals(currentUserId())) return ResponseEntity.status(403).build();
        String filename = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        Path dir = Paths.get(uploadDir);
        Files.createDirectories(dir);
        Path dest = dir.resolve(filename);
        Files.write(dest, file.getBytes());

        Photo p = new Photo();
        p.setUserId(userId);
        p.setUrl("/uploads/" + filename);
        p.setSortOrder(sortOrder == null ? 0 : sortOrder);
        photoRepository.save(p);
        return ResponseEntity.ok(p);
    }

    @GetMapping("/{userId}")
    public List<Photo> list(@PathVariable Long userId) {
        return photoRepository.findByUserIdOrderBySortOrderAsc(userId);
    }

    @DeleteMapping("/item/{photoId}")
    public ResponseEntity<Void> delete(@PathVariable Long photoId) throws IOException {
        var p = photoRepository.findById(photoId);
        if (p.isEmpty()) return ResponseEntity.notFound().build();
        if (!p.get().getUserId().equals(currentUserId())) return ResponseEntity.status(403).build();
        // удаляем файл если существует
        try {
            if (p.get().getUrl() != null && p.get().getUrl().startsWith("/uploads/")) {
                Path path = Paths.get(uploadDir).resolve(p.get().getUrl().substring("/uploads/".length()));
                Files.deleteIfExists(path);
            }
        } catch (Exception ignored) {}
        photoRepository.deleteById(photoId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/item/{photoId}/order")
    public ResponseEntity<Photo> updateOrder(@PathVariable Long photoId, @RequestParam("sortOrder") Integer sortOrder) {
        var p = photoRepository.findById(photoId);
        if (p.isEmpty()) return ResponseEntity.notFound().build();
        if (!p.get().getUserId().equals(currentUserId())) return ResponseEntity.status(403).build();
        var photo = p.get();
        photo.setSortOrder(sortOrder);
        photoRepository.save(photo);
        return ResponseEntity.ok(photo);
    }
}


