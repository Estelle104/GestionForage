package com.example.forage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.forage.entity.Parametre;
import java.util.Optional;

@Repository
public interface ParametreRepository extends JpaRepository<Parametre, Integer> {
    Optional<Parametre> findByIdStatus1AndIdStatus2(Integer idStatus1, Integer idStatus2);
}
