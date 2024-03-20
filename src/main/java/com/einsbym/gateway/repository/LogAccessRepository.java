package com.einsbym.gateway.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.einsbym.gateway.entity.LogAccess;

public interface LogAccessRepository extends JpaRepository<LogAccess, UUID> {
    
}
