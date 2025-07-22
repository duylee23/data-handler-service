package com.example.datahandlerapi.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Time;
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
    private Time createdAt;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    Project project;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    private List<Script> script = new ArrayList<>();

}
