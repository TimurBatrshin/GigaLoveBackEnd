package com.gigalove.photo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByUserIdOrderBySortOrderAsc(Long userId);
}


