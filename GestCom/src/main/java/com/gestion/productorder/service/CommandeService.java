package com.gestion.productorder.service;

import com.gestion.productorder.dto.CommandeDTO;
import com.gestion.productorder.entity.Commande;
import com.gestion.productorder.entity.Produit;
import com.gestion.productorder.repository.CommandeRepository;
import com.gestion.productorder.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class CommandeService {
    @Autowired
    private CommandeRepository commandeRepository;
    @Autowired
    private ProduitRepository produitRepository;
    @Autowired
    private EmailService emailService;

    public Page<CommandeDTO> getAllCommandes(Pageable pageable) {
        return commandeRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    public Optional<CommandeDTO> getCommandeById(Long id) {
        return commandeRepository.findById(id).map(this::convertToDTO);
    }

    public CommandeDTO saveCommande(CommandeDTO commandeDTO) {
        List<Produit> produits = produitRepository.findAllById(commandeDTO.getProduitIds());
        if (produits.size() != commandeDTO.getProduitIds().size()) {
            throw new RuntimeException("Certains produits n'existent pas");
        }
        double total = produits.stream()
                .mapToDouble(Produit::getPrix)
                .sum();
        Commande commande = new Commande();
        commande.setNomClient(commandeDTO.getNomClient());
        commande.setEmailClient(commandeDTO.getEmailClient());
        commande.setProduitsCommandes(new HashSet<>(produits));
        commande.setTotal(total);
        Commande savedCommande = commandeRepository.save(commande);
        try {
            emailService.sendOrderConfirmation(savedCommande);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email: " + e.getMessage());
        }
        return convertToDTO(savedCommande);
    }

    public Page<CommandeDTO> getCommandesByEmail(String email, Pageable pageable) {
        return commandeRepository.findByEmailClientContainingIgnoreCase(email, pageable)
                .map(this::convertToDTO);
    }

    public Page<CommandeDTO> getCommandesByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return commandeRepository.findByDateCommandeBetween(startDate, endDate, pageable)
                .map(this::convertToDTO);
    }

    private CommandeDTO convertToDTO(Commande commande) {
        List<Long> produitIds = commande.getProduitsCommandes().stream()
                .map(Produit::getId)
                .toList();
        return new CommandeDTO(
                commande.getId(),
                commande.getDateCommande(),
                commande.getNomClient(),
                commande.getEmailClient(),
                produitIds,
                commande.getTotal()
        );
    }
} 