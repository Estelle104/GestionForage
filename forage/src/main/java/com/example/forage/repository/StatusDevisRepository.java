package com.example.forage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.forage.entity.StatusDevis;

@Repository
public interface StatusDevisRepository extends JpaRepository<StatusDevis, Long> {
}
