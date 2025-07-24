package com.example.datahandlerapi.repository;

import com.example.datahandlerapi.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findGroupByGroupName(String groupName);

    boolean existsByGroupName(String groupName);

    Optional<Group> findGroupById(Long id);
}
