package com.example.datahandlerapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "groups")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@FieldDefaults( level = AccessLevel.PRIVATE)
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String groupName;
    private String description;
    private String createdBy;
    @CreationTimestamp
    Timestamp createdAt;

    @ManyToOne
    @JoinColumn(name = "project_id")
    Project project;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Script> scripts = new ArrayList<>();

}
