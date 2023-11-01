package com.example.reuse_api.repository;

import com.example.reuse_api.controller.SatelliteData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DBRepository extends JpaRepository<SatelliteData, String> {
    List<SatelliteData> findByName(String satname);
}

