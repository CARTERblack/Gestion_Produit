package com.gestion.productorder.service;

import com.gestion.productorder.dto.ProduitDTO;
import com.gestion.productorder.entity.Produit;
import com.gestion.productorder.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProduitService {
    @Autowired
    private ProduitRepository produitRepository;

    public Page<ProduitDTO> getAllProduits(Pageable pageable) {
        return produitRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public Page<ProduitDTO> searchProduits(String searchTerm, Pageable pageable) {
        return produitRepository.findByNomContainingIgnoreCaseOrCategorieContainingIgnoreCase(
                searchTerm, pageable).map(this::convertToDTO);
    }

    public Page<ProduitDTO> getProduitsByCategorie(String categorie, Pageable pageable) {
        return produitRepository.findByCategorieIgnoreCase(categorie, pageable)
                .map(this::convertToDTO);
    }

    public Optional<ProduitDTO> getProduitById(Long id) {
        return produitRepository.findById(id).map(this::convertToDTO);
    }

    public ProduitDTO saveProduit(ProduitDTO produitDTO) {
        Produit produit = convertToEntity(produitDTO);
        Produit savedProduit = produitRepository.save(produit);
        return convertToDTO(savedProduit);
    }

    public ProduitDTO updateProduit(Long id, ProduitDTO produitDTO) {
        return produitRepository.findById(id)
                .map(existingProduit -> {
                    existingProduit.setNom(produitDTO.getNom());
                    existingProduit.setDescription(produitDTO.getDescription());
                    existingProduit.setPrix(produitDTO.getPrix());
                    existingProduit.setStock(produitDTO.getStock());
                    existingProduit.setCategorie(produitDTO.getCategorie());
                    return convertToDTO(produitRepository.save(existingProduit));
                })
                .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'id: " + id));
    }

    public void deleteProduit(Long id) {
        if (!produitRepository.existsById(id)) {
            throw new RuntimeException("Produit non trouvé avec l'id: " + id);
        }
        produitRepository.deleteById(id);
    }

    public List<String> getAllCategories() {
        return produitRepository.findDistinctCategories();
    }

    private ProduitDTO convertToDTO(Produit produit) {
        return new ProduitDTO(
                produit.getId(),
                produit.getNom(),
                produit.getDescription(),
                produit.getPrix(),
                produit.getStock(),
                produit.getCategorie()
        );
    }

    private Produit convertToEntity(ProduitDTO produitDTO) {
        Produit produit = new Produit();
        produit.setId(produitDTO.getId());
        produit.setNom(produitDTO.getNom());
        produit.setDescription(produitDTO.getDescription());
        produit.setPrix(produitDTO.getPrix());
        produit.setStock(produitDTO.getStock());
        produit.setCategorie(produitDTO.getCategorie());
        return produit;
    }
} 