package br.com.ayo_quest.ayo_quest.dto.resolver;

import br.com.ayo_quest.ayo_quest.enuns.TipoConteudo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConteudoResolverDTO {

    private Long id;

    private TipoConteudo tipo;

    private String valor;

}
