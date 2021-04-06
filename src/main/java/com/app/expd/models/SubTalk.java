package com.app.expd.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "subtalk")
public class SubTalk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subtalkID;

    @NotBlank(message = "Community Name is required")
    @Column(unique = true)
    private String commName;

    @NotBlank(message = "Description is required")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userID", referencedColumnName = "userID")
    private User user;

    @OneToMany(mappedBy = "subtalk", fetch = FetchType.LAZY, cascade = CascadeType.ALL,
    orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    private Instant createdDate;

}
