package com.example.forage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.forage.entity.Commune;

@Repository
public interface CommuneRepository extends JpaRepository<Commune, Integer> {
}