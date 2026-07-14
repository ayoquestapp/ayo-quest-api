package br.com.ayo_quest.ayo_quest.dto;

import br.com.ayo_quest.ayo_quest.enuns.PrioridadeNotificacao;
import br.com.ayo_quest.ayo_quest.enuns.TipoNotificacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacaoDTO {
    private Long id;
    private String titulo;
    private String mensagem;
    private TipoNotificacao tipo;
    private PrioridadeNotificacao prioridade;
    private String icone;
    private String link;
    private boolean lida;
    private LocalDateTime dataCriacao;
}