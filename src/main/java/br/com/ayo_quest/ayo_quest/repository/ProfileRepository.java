package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.enuns.TipoUsuario;
import br.com.ayo_quest.ayo_quest.models.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<ProfileEntity, UUID> {


    List<ProfileEntity> findByRole(
            TipoUsuario role
    );

    Optional<ProfileEntity> findByEmail(String email);

}
