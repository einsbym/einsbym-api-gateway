package com.einsbym.gateway.repository;

import com.einsbym.gateway.entity.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequestLogRepository extends JpaRepository<RequestLog, UUID> {

}
