package com.example.banque;

import io.swagger.v3.oas.annotations.Operation;
import java.net.URI;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/banque")
public class CompteController {
    private final CompteRepository repository;

    public CompteController(CompteRepository repository) { this.repository = repository; }

    @Operation(summary = "Lister tous les comptes")
    @GetMapping(value = "/comptes", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<Compte> getAllComptes() { return repository.findAll(); }

    @Operation(summary = "Obtenir un compte par identifiant")
    @GetMapping(value = "/comptes/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Compte> getCompteById(@PathVariable Long id) {
        return repository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Créer un compte")
    @PostMapping(value = "/comptes", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Compte> createCompte(@RequestBody Compte compte) {
        compte.setId(null);
        Compte saved = repository.save(compte);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @Operation(summary = "Modifier un compte")
    @PutMapping(value = "/comptes/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Compte> updateCompte(@PathVariable Long id, @RequestBody Compte details) {
        return repository.findById(id).map(compte -> {
            compte.setSolde(details.getSolde());
            compte.setDateCreation(details.getDateCreation());
            compte.setType(details.getType());
            return ResponseEntity.ok(repository.save(compte));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Supprimer un compte")
    @DeleteMapping("/comptes/{id}")
    public ResponseEntity<Void> deleteCompte(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
