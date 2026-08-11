package br.com.ayo_quest.ayo_quest.controller;

import br.com.ayo_quest.ayo_quest.dto.*;
import br.com.ayo_quest.ayo_quest.dto.resolver.ModuloResolverDTO;
import br.com.ayo_quest.ayo_quest.models.ModuloEntity;
import br.com.ayo_quest.ayo_quest.repository.ModuloImpl;
import br.com.ayo_quest.ayo_quest.service.ModuloService;
import br.com.ayo_quest.ayo_quest.service.TentativaModuloService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/modulos")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ModuloController {

    @Autowired
    private ModuloService service;
    @Autowired
    private final TentativaModuloService tentativaService;
    @Autowired
    private ModuloImpl moduloImpl;


    @PostMapping("/cadastrar")
    public ResponseEntity<ModuloResponseDTO> salvar(
            @RequestBody ModuloCadastroDTO dto
    ) {

        return ResponseEntity.ok(
                service.criarModulo(dto)
        );
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ModuloDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @DeleteMapping("deletar/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarModulo(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("alterar/{id}")
    public ResponseEntity<ModuloResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody ModuloAtualizacaoDTO dto) {

        return ResponseEntity.ok(service.atualizarModulo(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuloResponseDTO> buscarPorId(@PathVariable Long id) {
        ModuloResponseDTO modulo = service.buscarPorId(id);
        return ResponseEntity.ok(modulo);
    }

    @GetMapping("/trilha/{id}")
    public ResponseEntity<List<ModuloDTO>> buscarPorTrilha(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                service.buscarPorTrilha(id)
        );

    }

    @GetMapping("/{id}/resolver")
    public ResponseEntity<ModuloResolverDTO> buscarPorIdResolver(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarModuloResolver(id));
    }

}