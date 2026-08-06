package br.com.ayo_quest.ayo_quest.dto.auth;

import br.com.ayo_quest.ayo_quest.enuns.TipoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CadastroUsuarioDTO {

    private String nome;

    private String email;

    private String senha;

    private TipoUsuario role;

}
