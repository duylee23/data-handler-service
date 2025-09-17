package com.example.datahandlerapi.repository;

import com.example.datahandlerapi.entity.RunScriptHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RunScriptHistoryRepository extends JpaRepository<RunScriptHistory, Long>, JpaSpecificationExecutor<RunScriptHistory> {
}
