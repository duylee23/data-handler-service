package com.example.datahandlerapi.repository;

import com.example.datahandlerapi.entity.Script;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScriptRepository extends JpaRepository<Script, Long> {
    Optional<Script> findScriptByName(String name);

    Optional<Script>  findScriptsById(Long id);
    
    List<Script> findByGroupGroupName(String groupName);
}
