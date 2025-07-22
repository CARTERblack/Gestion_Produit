package com.gestion.productorder.controller;

import com.gestion.productorder.dto.CommandeDTO;
import com.gestion.productorder.service.CommandeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/commandes")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Commandes", description = "API de gestion des commandes")
public class CommandeController {
    @Autowired
    private CommandeService commandeService;

    @GetMapping
    @Operation(summary = "Récupérer toutes les commandes avec pagination")
    public ResponseEntity<Page<CommandeDTO>> getAllCommandes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(commandeService.getAllCommandes(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une commande par ID")
    public ResponseEntity<CommandeDTO> getCommandeById(@PathVariable Long id) {
        return commandeService.getCommandeById(id)
                .map(commande -> ResponseEntity.ok().body(commande))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer une nouvelle commande")
    public ResponseEntity<CommandeDTO> createCommande(@Valid @RequestBody CommandeDTO commandeDTO) {
        CommandeDTO savedCommande = commandeService.saveCommande(commandeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCommande);
    }

    @GetMapping("/by-email")
    @Operation(summary = "Récupérer les commandes par email client")
    public ResponseEntity<Page<CommandeDTO>> getCommandesByEmail(
            @RequestParam String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(commandeService.getCommandesByEmail(email, pageable));
    }

    @GetMapping("/by-date")
    @Operation(summary = "Récupérer les commandes par plage de dates")
    public ResponseEntity<Page<CommandeDTO>> getCommandesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(commandeService.getCommandesByDateRange(startDate, endDate, pageable));
    }
} 