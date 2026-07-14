package br.com.ayo_quest.ayo_quest.controller;

import br.com.ayo_quest.ayo_quest.dto.TrilhaCreateDTO;
import br.com.ayo_quest.ayo_quest.dto.TrilhaDTO;
import br.com.ayo_quest.ayo_quest.dto.TrilhaUpdateDTO;
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

    @GetMapping("/detalhar/{id}")
    public ResponseEntity<TrilhaDTO> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(trilhaService.detalhar(id));
    }

    @PostMapping("/criar")
    public ResponseEntity<TrilhaEntity> criar(
            @RequestBody TrilhaCreateDTO dto
    ) {

        return ResponseEntity.ok(
                trilhaService.criar(dto)
        );
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Void> atualizar(
            @PathVariable Long id,
            @RequestBody TrilhaUpdateDTO dto
    ){
        trilhaService.atualizar(id, dto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletar(            @PathVariable Long id
    ) {

        trilhaService.deletar(id);

        return ResponseEntity.noContent().build();
    }
}

