package com.gestion.productorder.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Set;

@Entity
@Table(name = "produits")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Le nom est requis")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;
    
    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    private String description;
    
    @NotNull(message = "Le prix est requis")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le prix doit être positif")
    private Double prix;
    
    @NotNull(message = "Le stock est requis")
    @Min(value = 0, message = "Le stock ne peut pas être négatif")
    private Integer stock;
    
    @NotBlank(message = "La catégorie est requise")
    private String categorie;
    
    @ManyToMany(mappedBy = "produitsCommandes")
    private Set<Commande> commandes;
    
    public Produit() {}
    
    public Produit(String nom, String description, Double prix, Integer stock, String categorie) {
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.stock = stock;
        this.categorie = categorie;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Double getPrix() { return prix; }
    public void setPrix(Double prix) { this.prix = prix; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }
    public Set<Commande> getCommandes() { return commandes; }
    public void setCommandes(Set<Commande> commandes) { this.commandes = commandes; }
} 