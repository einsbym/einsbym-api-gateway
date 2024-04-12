package com.einsbym.api.gateway.repository;

import com.einsbym.api.gateway.entity.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequestLogRepository extends JpaRepository<RequestLog, UUID> {

}
