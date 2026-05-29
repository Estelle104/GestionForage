package com.example.forage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.forage.entity.Type;

@Repository
public interface TypeRepository extends JpaRepository<Type, Integer> {

}
