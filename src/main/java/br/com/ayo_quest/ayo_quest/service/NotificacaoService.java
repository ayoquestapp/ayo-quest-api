package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.enuns.PrioridadeNotificacao;
import br.com.ayo_quest.ayo_quest.enuns.TipoNotificacao;
import br.com.ayo_quest.ayo_quest.models.NotificacaoEntity;
import br.com.ayo_quest.ayo_quest.models.ProfileEntity;
import br.com.ayo_quest.ayo_quest.models.UsuarioNotificacaoEntity;
import br.com.ayo_quest.ayo_quest.repository.NotificacaoRepository;
import br.com.ayo_quest.ayo_quest.repository.ProfileRepository;
import br.com.ayo_quest.ayo_quest.repository.UsuarioNotificacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificacaoService {
    private final NotificacaoRepository notificacaoRepository;
    private final UsuarioNotificacaoRepository usuarioNotificacaoRepository;
    private final ProfileRepository profileRepository;

    public void enviarParaUsuario(UUID usuarioId, String titulo, String mensagem, TipoNotificacao tipo, PrioridadeNotificacao prioridade, String icone, String link, UUID origemUsuarioId) {
        NotificacaoEntity notificacao = new NotificacaoEntity();
        notificacao.setTitulo(titulo);
        notificacao.setMensagem(mensagem);
        notificacao.setTipo(tipo);
        notificacao.setPrioridade(prioridade);
        notificacao.setIcone(icone);
        notificacao.setLink(link);
        notificacao.setDataCriacao(LocalDateTime.now());
        if (origemUsuarioId != null) {
            ProfileEntity origem = profileRepository.findById(origemUsuarioId).orElseThrow();
            notificacao.setOrigemUsuario(origem);
        }
        notificacao = notificacaoRepository.save(notificacao);
        ProfileEntity usuario = profileRepository.findById(usuarioId).orElseThrow();
        UsuarioNotificacaoEntity usuarioNotificacao = new UsuarioNotificacaoEntity();
        usuarioNotificacao.setUsuario(usuario);
        usuarioNotificacao.setNotificacao(notificacao);
        usuarioNotificacaoRepository.save(usuarioNotificacao);
    }

    public List<UsuarioNotificacaoEntity> listar(UUID usuarioId) {
        return usuarioNotificacaoRepository.findByUsuarioIdOrderByNotificacaoDataCriacaoDesc(usuarioId);
    }

    public long contarNaoLidas(UUID usuarioId) {
        return usuarioNotificacaoRepository.countByUsuarioIdAndLidaFalse(usuarioId);
    }

    public void marcarComoLida(Long id) {
        UsuarioNotificacaoEntity notificacao = usuarioNotificacaoRepository.findById(id).orElseThrow();
        notificacao.setLida(true);
        notificacao.setDataLeitura(LocalDateTime.now());
        usuarioNotificacaoRepository.save(notificacao);
    }

    public void marcarTodasComoLidas(UUID usuarioId) {
        List<UsuarioNotificacaoEntity> lista = listar(usuarioId);
        lista.forEach(n -> {
            n.setLida(true);
            n.setDataLeitura(LocalDateTime.now());
        });
        usuarioNotificacaoRepository.saveAll(lista);
    }
}
