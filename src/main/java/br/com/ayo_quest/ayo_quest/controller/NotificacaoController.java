package br.com.ayo_quest.ayo_quest.controller;

import br.com.ayo_quest.ayo_quest.models.UsuarioNotificacaoEntity;
import br.com.ayo_quest.ayo_quest.service.NotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notificacoes")
@RequiredArgsConstructor
public class NotificacaoController {
    private final NotificacaoService notificacaoService;

    @GetMapping("/{usuarioId}")
    public List<UsuarioNotificacaoEntity> listar(@PathVariable UUID usuarioId) {
        return notificacaoService.listar(usuarioId);
    }

    @GetMapping("/{usuarioId}/nao-lidas")
    public long contarNaoLidas(@PathVariable UUID usuarioId) {
        return notificacaoService.contarNaoLidas(usuarioId);
    }

    @PutMapping("/{id}/ler")
    public void marcarComoLida(@PathVariable Long id) {
        notificacaoService.marcarComoLida(id);
    }

    @PutMapping("/{usuarioId}/ler-todas")
    public void marcarTodas(@PathVariable UUID usuarioId) {
        notificacaoService.marcarTodasComoLidas(usuarioId);
    }
}