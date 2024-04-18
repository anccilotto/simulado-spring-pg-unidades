package br.com.fiap.unidades.resource;

import br.com.fiap.unidades.entity.Chefe;
import br.com.fiap.unidades.entity.Unidade;
import br.com.fiap.unidades.entity.Usuario;
import br.com.fiap.unidades.service.ChefeServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequestMapping(value = "/chefe")
public class ChefeResource {

    @Autowired
    private ChefeService chefeService;

    @GetMapping
    public ResponseEntity<Collection<Chefe>> findAll(
            @RequestParam(name = "substituto", required = false) Boolean substituto,
            @RequestParam(name = "usuario", required = false) Long usuarioId,
            @RequestParam(name = "unidade", required = false) Long unidadeId,
            @RequestParam(name = "inicio", required = false) LocalDateTime inicio,
            @RequestParam(name = "fim", required = false) LocalDateTime fim
    ) {
        Chefe chefe = Chefe.builder()
                .substituto(substituto)
                .usuario(Usuario.builder().id(usuarioId).build())
                .unidade(Unidade.builder().id(unidadeId).build())
                .inicio(inicio)
                .fim(fim)
                .build();

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase();

        Example<Chefe> example = Example.of(chefe, matcher);

        Collection<Chefe> encontrados = chefeService.findAll(example);
        return ResponseEntity.ok(encontrados);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Chefe> findById(@PathVariable Long id) {
        Chefe chefe = chefeService.findById(id);
        if (chefe == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(chefe);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Chefe> save(@RequestBody @Valid Chefe chefe) {
        Chefe savedChefe = chefeService.save(chefe);

        var uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(savedChefe.getId())
                .toUri();

        return ResponseEntity.created(uri).body(savedChefe);
    }
}