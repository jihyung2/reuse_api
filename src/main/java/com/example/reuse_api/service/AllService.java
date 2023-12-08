package com.example.reuse_api.service;

import com.example.reuse_api.entity.AllStoreData;
import com.example.reuse_api.repository.AllRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AllService {

    private final AllRepository allRepository;

    @Autowired
    public AllService(AllRepository allRepository) {
        this.allRepository = allRepository;
    }

    public AllStoreData saveAllDB(AllStoreData storedata) {
        return allRepository.save(storedata);
    }

    public List<AllStoreData> getALLDB() {
        return allRepository.findAll();
    }

}
