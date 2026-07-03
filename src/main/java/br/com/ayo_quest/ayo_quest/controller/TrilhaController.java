package br.com.ayo_quest.ayo_quest.controller;

import br.com.ayo_quest.ayo_quest.dto.TrilhaDTO;
import br.com.ayo_quest.ayo_quest.models.TrilhaEntity;
import br.com.ayo_quest.ayo_quest.service.TrilhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trilhas")
@CrossOrigin("*")

public class TrilhaController {

    @Autowired
    private TrilhaService trilhaService;

    @GetMapping("/listar")
    public ResponseEntity<List<TrilhaDTO>> listar() {
        return ResponseEntity.ok(trilhaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrilhaEntity> detalhar(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                trilhaService.detalhar(id)
        );
    }

    @PostMapping("/criar")
    public ResponseEntity<TrilhaEntity> criar(
            @RequestBody TrilhaEntity trilha
    ) {

        return ResponseEntity.ok(
                trilhaService.criar(trilha)
        );
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<TrilhaEntity> atualizar(
            @PathVariable Long id,
            @RequestBody TrilhaEntity trilha
    ) {

        return ResponseEntity.ok(
                trilhaService.atualizar(id, trilha)
        );
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(            @PathVariable Long id
    ) {

        trilhaService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}

