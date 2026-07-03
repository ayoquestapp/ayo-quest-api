package br.com.ayo_quest.ayo_quest.models;

import br.com.ayo_quest.ayo_quest.enuns.TipoUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
@Entity
@Table(name = "profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileEntity {
    @Id
    private UUID id;
    private String name;
    private String avatar_url;
    @Enumerated(EnumType.STRING)
    private TipoUsuario role;
    private String email;
    private Long xp;
    private Integer level;
    private Instant created_at;
    private Instant updated_at;
}
