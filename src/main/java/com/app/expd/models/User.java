package com.app.expd.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LETS_TALK_USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;
    @NotBlank(message = "Username cannot be empty")
    @Column(unique = true)
    private String username;
    @NotBlank(message = "Password cannot be empty")
    private String password;
    @NotBlank(message = "name cannot be empty")
    private String name;
    @NotBlank(message = "role cannot be null")
    @Column(name = "role", length = 5)
    @Enumerated(EnumType.STRING)
    private Roles role;
    private Instant createdDate;
    private Boolean enabled;



    @Override
    public boolean equals(Object o) {

        if (o == this) return true;
        if (!(o instanceof User)) {
            return false;
        }
        User user = (User) o;
        return userID.equals(user.getUserID()) &&
                username.equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userID, username);
    }
}
