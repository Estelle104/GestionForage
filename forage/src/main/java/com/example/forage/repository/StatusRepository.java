package com.example.forage.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.forage.entity.Status;

@Repository
public interface StatusRepository
        extends JpaRepository<Status, Integer> {

}