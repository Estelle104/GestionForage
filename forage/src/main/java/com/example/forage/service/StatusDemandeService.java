package com.example.forage.service;

import java.util.List;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.Duration;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.forage.entity.StatusDemande;
import com.example.forage.entity.Demande;
import com.example.forage.repository.StatusDemandeRepository;

@Service
public class StatusDemandeService {
    
    private final StatusDemandeRepository statusDemandeRepository;

    public StatusDemandeService(StatusDemandeRepository statusDemandeRepository) {
        this.statusDemandeRepository = statusDemandeRepository;
    }
    
    @Transactional
    public StatusDemande save(@NonNull StatusDemande statusDemande) {
        StatusDemande saved = statusDemandeRepository.save(statusDemande);
        recalculateDurees(saved.getDemande());
        return saved;
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

    @Transactional
    public void recalculateDurees(Demande demande) {
        if (demande == null) return;
        List<StatusDemande> history = statusDemandeRepository.findByDemande(demande);
        if (history == null || history.isEmpty()) return;
        
        // Sort chronologically by dateStatus. If dateStatus is same, sort by ID.
        history.sort((a, b) -> {
            int cmp = a.getDateStatus().compareTo(b.getDateStatus());
            if (cmp == 0) {
                if (a.getId() == null && b.getId() == null) return 0;
                if (a.getId() == null) return 1;
                if (b.getId() == null) return -1;
                return a.getId().compareTo(b.getId());
            }
            return cmp;
        });
        
        // Calculate durations
        for (int i = 0; i < history.size(); i++) {
            StatusDemande current = history.get(i);
            if (i == 0) {
                current.setDureeTravail(0.0);
            } else {
                StatusDemande previous = history.get(i - 1);
                double workingMin = calculateWorkingMinutes(previous.getDateStatus(), current.getDateStatus());
                current.setDureeTravail(workingMin);
            }
            statusDemandeRepository.save(current);
        }
    }

    public static double calculateWorkingMinutes(Timestamp startTs, Timestamp endTs) {
        if (startTs == null || endTs == null || startTs.after(endTs)) {
            return 0.0;
        }
        
        LocalDateTime start = startTs.toLocalDateTime();
        LocalDateTime end = endTs.toLocalDateTime();
        
        long totalSeconds = 0;
        LocalDate startDate = start.toLocalDate();
        LocalDate endDate = end.toLocalDate();
        
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            // Check if weekend
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                continue;
            }
            
            LocalDateTime workStart = date.atTime(8, 0);
            LocalDateTime workEnd = date.atTime(16, 0);
            
            LocalDateTime actualStart = start.isAfter(workStart) ? start : workStart;
            LocalDateTime actualEnd = end.isBefore(workEnd) ? end : workEnd;
            
            if (actualStart.isBefore(actualEnd)) {
                totalSeconds += Duration.between(actualStart, actualEnd).toSeconds();
            }
        }
        
        return totalSeconds / 60.0;
    }
}
