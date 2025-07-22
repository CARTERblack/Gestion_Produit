package com.gestion.productorder.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

public class CommandeDTO {
    private Long id;
    private LocalDateTime dateCommande;
    
    @NotBlank(message = "Le nom du client est requis")
    private String nomClient;
    
    @NotBlank(message = "L'email du client est requis")
    @Email(message = "L'email doit être valide")
    private String emailClient;
    
    @NotEmpty(message = "Au moins un produit doit être commandé")
    private List<Long> produitIds;
    
    private Double total;
    
    public CommandeDTO() {}
    
    public CommandeDTO(Long id, LocalDateTime dateCommande, String nomClient, String emailClient, List<Long> produitIds, Double total) {
        this.id = id;
        this.dateCommande = dateCommande;
        this.nomClient = nomClient;
        this.emailClient = emailClient;
        this.produitIds = produitIds;
        this.total = total;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDateTime dateCommande) { this.dateCommande = dateCommande; }
    public String getNomClient() { return nomClient; }
    public void setNomClient(String nomClient) { this.nomClient = nomClient; }
    public String getEmailClient() { return emailClient; }
    public void setEmailClient(String emailClient) { this.emailClient = emailClient; }
    public List<Long> getProduitIds() { return produitIds; }
    public void setProduitIds(List<Long> produitIds) { this.produitIds = produitIds; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
} 