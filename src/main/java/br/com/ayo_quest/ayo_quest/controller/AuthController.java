package br.com.ayo_quest.ayo_quest.controller;

import br.com.ayo_quest.ayo_quest.dto.auth.CadastroUsuarioDTO;
import br.com.ayo_quest.ayo_quest.dto.auth.LoginRequestDTO;
import br.com.ayo_quest.ayo_quest.dto.auth.LoginResponseDTO;
import br.com.ayo_quest.ayo_quest.models.UsuarioEntity;
import br.com.ayo_quest.ayo_quest.repository.UsuarioRepository;
import br.com.ayo_quest.ayo_quest.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {


    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;


    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody CadastroUsuarioDTO dto
    ) throws MessagingException, UnsupportedEncodingException {

        authService.cadastrar(dto);


        return ResponseEntity.ok(
                "Usuário criado com sucesso"
        );

    }

    @GetMapping("/confirmar-email")
    public ResponseEntity<?> confirmarEmail(
            @RequestParam String token
    ){

        UsuarioEntity usuario =
                usuarioRepository
                        .findByTokenConfirmacao(token)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Token inválido"
                                )
                        );


        usuario.setAtivo(true);

        usuario.setTokenConfirmacao(null);


        usuarioRepository.save(usuario);



        return ResponseEntity.ok(
                "Email confirmado com sucesso"
        );

    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO dto) {

        System.out.println("🔥🔥🔥 ENTROU NO CONTROLLER LOGIN");
        System.out.println("EMAIL: [" + dto.getEmail() + "]");

        return authService.login(dto);
    }

}


