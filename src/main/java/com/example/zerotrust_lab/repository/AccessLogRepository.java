package com.example.zerotrust_lab.repository;

import com.example.zerotrust_lab.model.AccessLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessLogRepository
        extends JpaRepository<AccessLog, Long> {

    List<AccessLog> findAllByOrderByTimestampDesc();
}