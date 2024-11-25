package ru.patron55mm.task.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import ru.patron55mm.task.utils.enums.RoleEnum;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Email is not valid")
    @Column(unique = true, nullable = false, length = 300)
    private String email;

    @JsonIgnore
    @Column(nullable = false, length = 60)
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;

    @CreationTimestamp
    private LocalDateTime registration;
}