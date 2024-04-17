package br.com.fiap.unidades.resource;

import br.com.fiap.unidades.entity.Unidade;
import br.com.fiap.unidades.service.UnidadeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;

@RestController
@RequestMapping(value = "/unidade")
public class UnidadeResource {

    @Autowired
    private UnidadeService unidadeService;

    @GetMapping
    public ResponseEntity<Collection<Unidade>> findAll(
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "sigla", required = false) String sigla,
            @RequestParam(name = "macro", required = false) Unidade macro
    ) {
        Unidade unidade = Unidade.builder()
                .nome(nome)
                .sigla(sigla)
                .macro(Unidade.builder().id(macro).build())
                .build();

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase()
                .withMatcher("macro.id", ExampleMatcher.GenericPropertyMatcher::exact);

        Example<Unidade> example = Example.of(unidade, matcher);

        Collection<Unidade> encontradas = unidadeService.findAll(example);
        return ResponseEntity.ok(encontradas);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Unidade> findById(@PathVariable Long id) {
        Unidade unidade = unidadeService.findById(id);
        if (unidade == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(unidade);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Unidade> save(@RequestBody @Valid Unidade unidade) {
        Unidade savedUnidade = unidadeService.save(unidade);

        var uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(savedUnidade.getId())
                .toUri();

        return ResponseEntity.created(uri).body(savedUnidade);
    }
}