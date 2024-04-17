package br.com.fiap.unidades.resource;

import br.com.fiap.unidades.entity.Usuario;
import br.com.fiap.unidades.service.UsuarioService;
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
@RequestMapping(value = "/usuario")
public class UsuarioResource {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Collection<Usuario>> findAll(
            @RequestParam(name = "username", required = false) String username,
            @RequestParam(name = "pessoaId", required = false) Long pessoaId,
            @RequestParam(name = "nome", required = false) String nome,
            @RequestParam(name = "sobrenome", required = false) String sobrenome,
            @RequestParam(name = "nascimento", required = false) String nascimento,
            @RequestParam(name = "tipo", required = false) String tipo,
            @RequestParam(name = "email", required = false) String email
    ) {
        Usuario usuario = Usuario.builder()
                .username(username)
                .pessoa(Pessoa.builder()
                        .id(pessoaId)
                        .nome(nome)
                        .sobrenome(sobrenome)
                        .nascimento(nascimento)
                        .tipo(tipo)
                        .email(email)
                        .build())
                .build();

        ExampleMatcher matcher = ExampleMatcher.matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase();

        Example<Usuario> example = Example.of(usuario, matcher);

        Collection<Usuario> encontrados = usuarioService.findAll(example);
        return ResponseEntity.ok(encontrados);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        Usuario usuario = usuarioService.findById(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Usuario> save(@RequestBody @Valid Usuario usuario) {
        Usuario savedUsuario = usuarioService.save(usuario);

        var uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(savedUsuario.getId())
                .toUri();

        return ResponseEntity.created(uri).body(savedUsuario);
    }
}
