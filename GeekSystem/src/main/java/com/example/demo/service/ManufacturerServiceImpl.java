package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Manufacturer;
import com.example.demo.repository.ManufacturerRepository;

@Service
public class ManufacturerServiceImpl implements ManufacturerService {
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    
    @Override
    public List<Manufacturer> findAll() {
        return manufacturerRepository.findAll();
    }
    
    @Override
    public Optional<Manufacturer> findById(Long id) {
        // IDでメーカを検索し、Optionalで返す
        return manufacturerRepository.findById(id);
    }
    
    @Override
    public void save(Manufacturer manufacturer) {
    	manufacturerRepository.save(manufacturer);
    }
    
    @Override
    public void deleteById(Long id) {
    	manufacturerRepository.deleteById(id);
    }
}
