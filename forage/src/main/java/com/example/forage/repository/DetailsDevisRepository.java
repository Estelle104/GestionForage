package com.example.forage.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.forage.entity.DetailsDevis;

@Repository
public interface DetailsDevisRepository extends JpaRepository<DetailsDevis, Long> {
    List<DetailsDevis> findByDevisId(Long devisId);
}