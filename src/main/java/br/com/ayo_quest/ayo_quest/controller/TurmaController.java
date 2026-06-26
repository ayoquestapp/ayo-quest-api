package br.com.ayo_quest.ayo_quest.controller;

import br.com.ayo_quest.ayo_quest.dto.TurmaDTO;
import br.com.ayo_quest.ayo_quest.models.TurmaEntity;

import br.com.ayo_quest.ayo_quest.service.TurmaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/turmas")
@CrossOrigin("*")
public class TurmaController {

    @Autowired
    private TurmaService turmaService;

    @GetMapping("/listar")
    private ResponseEntity<List<TurmaEntity>> listar(){
        turmaService.listar();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/listar/{id}")
    private ResponseEntity<TurmaDTO> detalhar(@PathVariable Long id){
        turmaService.detalhar(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cadastrar")
    private ResponseEntity<TurmaEntity> cadastrar(@RequestBody TurmaEntity entity){
        turmaService.criar(entity);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/atualizar/{id}")
    private ResponseEntity<TurmaEntity> alterar(@PathVariable Long id , @RequestBody TurmaEntity entity){
        turmaService.atualizar(id,entity);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deletar/{id}")
    private ResponseEntity<TurmaEntity> deletar(@PathVariable Long id){
        turmaService.deletar(id);
        return ResponseEntity.ok().build();
    }

}
