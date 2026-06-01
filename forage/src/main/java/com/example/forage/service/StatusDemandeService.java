package com.example.forage.service;

import java.util.List;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.example.forage.entity.StatusDemande;
import com.example.forage.entity.Demande;
import com.example.forage.repository.StatusDemandeRepository;

@Service
public class StatusDemandeService {
    
    private final StatusDemandeRepository statusDemandeRepository;

    public StatusDemandeService(StatusDemandeRepository statusDemandeRepository) {
        this.statusDemandeRepository = statusDemandeRepository;
    }
    
    public StatusDemande save(@NonNull StatusDemande statusDemande) {
        statusDemandeRepository.save(statusDemande);
        return statusDemande;
    }

    public List<StatusDemande> findAll() {
        return statusDemandeRepository.findAll();
    }

    public StatusDemande findById(Integer id) {
        return statusDemandeRepository.findById(id).orElseThrow(() -> new RuntimeException("StatusDemande non trouvé"));
    }

    public List<StatusDemande> findByDemande(Demande demande) {
        return statusDemandeRepository.findByDemande(demande);
    }
}
