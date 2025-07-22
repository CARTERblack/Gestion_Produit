package com.gestion.productorder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "commandes")
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "date_commande")
    private LocalDateTime dateCommande;
    
    @NotBlank(message = "Le nom du client est requis")
    private String nomClient;
    
    @NotBlank(message = "L'email du client est requis")
    @Email(message = "L'email doit être valide")
    private String emailClient;
    
    @ManyToMany
    @JoinTable(
        name = "commande_produit",
        joinColumns = @JoinColumn(name = "commande_id"),
        inverseJoinColumns = @JoinColumn(name = "produit_id")
    )
    private Set<Produit> produitsCommandes = new HashSet<>();
    
    @NotNull(message = "Le total est requis")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le total doit être positif")
    private Double total;
    
    public Commande() {
        this.dateCommande = LocalDateTime.now();
    }
    
    public Commande(String nomClient, String emailClient, Double total) {
        this();
        this.nomClient = nomClient;
        this.emailClient = emailClient;
        this.total = total;
    }
    
    @PrePersist
    protected void onCreate() {
        if (dateCommande == null) {
            dateCommande = LocalDateTime.now();
        }
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDateTime dateCommande) { this.dateCommande = dateCommande; }
    public String getNomClient() { return nomClient; }
    public void setNomClient(String nomClient) { this.nomClient = nomClient; }
    public String getEmailClient() { return emailClient; }
    public void setEmailClient(String emailClient) { this.emailClient = emailClient; }
    public Set<Produit> getProduitsCommandes() { return produitsCommandes; }
    public void setProduitsCommandes(Set<Produit> produitsCommandes) { this.produitsCommandes = produitsCommandes; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
} 