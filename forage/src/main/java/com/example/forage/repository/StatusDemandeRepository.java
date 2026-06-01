package com.example.forage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.forage.entity.StatusDemande;
import com.example.forage.entity.Demande;
import java.util.List;

public interface StatusDemandeRepository extends JpaRepository<StatusDemande, Integer> {
    List<StatusDemande> findByDemande(Demande demande);
}    
