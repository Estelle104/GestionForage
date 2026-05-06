package com.example.forage.service;

import com.example.forage.entity.Demande;
import com.example.forage.entity.Status;
import com.example.forage.entity.StatusDemande;
import com.example.forage.repository.DemandeRepository;
import com.example.forage.repository.StatusDemandeRepository;
import com.example.forage.repository.StatusRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemandeService {

    private final DemandeRepository repo;
    private final StatusRepository statusRepo;
    private final StatusDemandeRepository statusDemandeRepo;

    public DemandeService(DemandeRepository repo, StatusRepository statusRepo,
            StatusDemandeRepository statusDemandeRepo) {
        this.repo = repo;
        this.statusRepo = statusRepo;
        this.statusDemandeRepo = statusDemandeRepo;
    }

    public void save(Demande d) {
        repo.save(d);
    }

    public List<Demande> findAll() {
        return repo.findAll();
    }

    @Transactional
    public void creerDemande(Demande d, Long idStatus) {

        if (d == null)
            return;

        if (idStatus == null) {
            idStatus = 1L;
        }

        Status status = statusRepo.findById(idStatus).orElseThrow(() -> new RuntimeException("Status not found"));
        
        repo.save(d);

        StatusDemande sm = new StatusDemande();
        sm.setDemande(d);
        sm.setStatus(status);
        sm.setDateStatus(d.getDateDemande());

        statusDemandeRepo.save(sm);
    }

}