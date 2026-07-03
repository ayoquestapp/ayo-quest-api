    package br.com.ayo_quest.ayo_quest.controller;

    import br.com.ayo_quest.ayo_quest.dto.ConvidarAlunoDTO;
    import br.com.ayo_quest.ayo_quest.dto.PeriodoDTO;
    import br.com.ayo_quest.ayo_quest.dto.TurmaCadastroDTO;
    import br.com.ayo_quest.ayo_quest.dto.TurmaDTO;
    import br.com.ayo_quest.ayo_quest.enuns.TiposPeriodos;
    import br.com.ayo_quest.ayo_quest.models.TurmaEntity;

    import br.com.ayo_quest.ayo_quest.service.TurmaService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.Arrays;
    import java.util.List;

    @RestController
    @RequestMapping("/turmas")
    @CrossOrigin("*")
    public class TurmaController {

        @Autowired
        private TurmaService turmaService;

        @GetMapping("/listar")
        public ResponseEntity<List<TurmaDTO>> listar() {
            return ResponseEntity.ok(turmaService.listar());
        }

        @GetMapping("/listar/{id}")
        private ResponseEntity<TurmaDTO> detalhar(@PathVariable Long id){
            turmaService.detalhar(id);
            return ResponseEntity.ok().build();
        }

        @PostMapping("/cadastrar")
        private ResponseEntity<TurmaDTO> cadastrar(@RequestBody TurmaCadastroDTO dto){
            return ResponseEntity.ok(turmaService.criar(dto));
        }

        @PutMapping("/atualizar/{id}")
        private ResponseEntity<TurmaEntity> alterar(@PathVariable Long id , @RequestBody TurmaEntity entity){
            turmaService.atualizar(id,entity);
            return ResponseEntity.ok().build();
        }

        @DeleteMapping("/deletar/{id}")
        private void deletar(@PathVariable Long id){
            turmaService.deletar(id);
        }

        @GetMapping("/periodos")
        public ResponseEntity<List<PeriodoDTO>> listarPeriodos() {

            List<PeriodoDTO> periodos = Arrays.stream(TiposPeriodos.values())
                    .map(PeriodoDTO::new)
                    .toList();

            return ResponseEntity.ok(periodos);
        }


    }
