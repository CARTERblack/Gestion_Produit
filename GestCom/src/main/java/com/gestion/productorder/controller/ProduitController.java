package com.gestion.productorder.controller;

import com.gestion.productorder.dto.ProduitDTO;
import com.gestion.productorder.service.ProduitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/produits")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Produits", description = "API de gestion des produits")
public class ProduitController {
    @Autowired
    private ProduitService produitService;

    @GetMapping
    @Operation(summary = "Récupérer tous les produits avec pagination")
    public ResponseEntity<Page<ProduitDTO>> getAllProduits(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nom") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String categorie) {
        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                   Sort.by(sortBy).descending() :
                   Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProduitDTO> produits;
        if (search != null && !search.isEmpty()) {
            produits = produitService.searchProduits(search, pageable);
        } else if (categorie != null && !categorie.isEmpty()) {
            produits = produitService.getProduitsByCategorie(categorie, pageable);
        } else {
            produits = produitService.getAllProduits(pageable);
        }
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un produit par ID")
    public ResponseEntity<ProduitDTO> getProduitById(@PathVariable Long id) {
        return produitService.getProduitById(id)
                .map(produit -> ResponseEntity.ok().body(produit))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau produit")
    public ResponseEntity<ProduitDTO> createProduit(@Valid @RequestBody ProduitDTO produitDTO) {
        ProduitDTO savedProduit = produitService.saveProduit(produitDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduit);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un produit")
    public ResponseEntity<ProduitDTO> updateProduit(@PathVariable Long id, @Valid @RequestBody ProduitDTO produitDTO) {
        ProduitDTO updatedProduit = produitService.updateProduit(id, produitDTO);
        return ResponseEntity.ok(updatedProduit);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un produit")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        produitService.deleteProduit(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categories")
    @Operation(summary = "Récupérer toutes les catégories distinctes")
    public ResponseEntity<List<String>> getAllCategories() {
        return ResponseEntity.ok(produitService.getAllCategories());
    }
} 