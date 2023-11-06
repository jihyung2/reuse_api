package com.example.reuse_api.repository;

import com.example.reuse_api.entity.AllStoreData;
import com.example.reuse_api.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageData, String> {
}
