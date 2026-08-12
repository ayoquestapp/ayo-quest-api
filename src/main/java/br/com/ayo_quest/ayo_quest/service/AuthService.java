package br.com.ayo_quest.ayo_quest.service;

import br.com.ayo_quest.ayo_quest.dto.auth.CadastroUsuarioDTO;
import br.com.ayo_quest.ayo_quest.dto.auth.LoginRequestDTO;
import br.com.ayo_quest.ayo_quest.dto.auth.LoginResponseDTO;
import br.com.ayo_quest.ayo_quest.enuns.TipoUsuario;
import br.com.ayo_quest.ayo_quest.models.ProfileEntity;
import br.com.ayo_quest.ayo_quest.models.UsuarioEntity;
import br.com.ayo_quest.ayo_quest.repository.ProfileRepository;
import br.com.ayo_quest.ayo_quest.repository.UsuarioRepository;
import br.com.ayo_quest.ayo_quest.security.JWTService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UsuarioRepository usuarioRepository;

    private final JWTService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final ProfileRepository profileRepository;



    public void cadastrar(CadastroUsuarioDTO dto) throws MessagingException, UnsupportedEncodingException {


        if (usuarioRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }


        String token = UUID.randomUUID().toString();


        UsuarioEntity usuario = UsuarioEntity.builder()
                .id(UUID.randomUUID())
                .nome(dto.getNome())
                .email(dto.getEmail())
                .senha(passwordEncoder.encode(dto.getSenha()))
                .role(dto.getRole())
                .ativo(false)
                .tokenConfirmacao(token)
                .build();

        ProfileEntity profile = ProfileEntity.builder()
                .id(usuario.getId())
                .name(usuario.getNome())
                .email(usuario.getEmail())
                .role(usuario.getRole())
                .xp(0L)
                .level(1)
                .created_at(Instant.now())
                .updated_at(Instant.now())
                .usuario(usuario)
                .build();

        usuario.setProfile(profile);



        usuarioRepository.save(usuario);



        emailService.enviarConfirmacaoEmail(
                usuario.getEmail(),
                usuario.getNome(),
                token
        );
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {

        String email = dto.getEmail()
                .trim()
                .toLowerCase();

        UsuarioEntity usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuário não encontrado"
                        )
                );

        boolean matches = passwordEncoder.matches(
                dto.getSenha(),
                usuario.getSenha()
        );

        System.out.println("SENHA CORRETA: " + matches);

        if (!matches) {
            throw new RuntimeException("Senha inválida");
        }

        String token = jwtService.gerarToken(usuario);

        return new LoginResponseDTO(
                token,
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole().name()
        );
    }
}