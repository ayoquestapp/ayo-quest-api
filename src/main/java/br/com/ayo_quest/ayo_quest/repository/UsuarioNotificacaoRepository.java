package br.com.ayo_quest.ayo_quest.repository;

import br.com.ayo_quest.ayo_quest.models.UsuarioNotificacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UsuarioNotificacaoRepository extends JpaRepository<UsuarioNotificacaoEntity, Long> {

    List<UsuarioNotificacaoEntity> findByUsuarioIdOrderByNotificacaoDataCriacaoDesc(UUID usuarioId);

    long countByUsuarioIdAndLidaFalse(UUID usuarioId);

}