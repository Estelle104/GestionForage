package com.example.forage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.forage.entity.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
}