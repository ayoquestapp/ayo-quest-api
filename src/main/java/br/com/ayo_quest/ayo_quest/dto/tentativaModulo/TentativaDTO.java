package br.com.ayo_quest.ayo_quest.dto.tentativaModulo;

import br.com.ayo_quest.ayo_quest.enuns.StatusTentativa;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TentativaDTO {

    private Long id;
    private StatusTentativa status;


}
