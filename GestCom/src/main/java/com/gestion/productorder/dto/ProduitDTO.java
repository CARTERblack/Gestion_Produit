package com.gestion.productorder.dto;

import jakarta.validation.constraints.*;

public class ProduitDTO {
    private Long id;
    
    @NotBlank(message = "Le nom est requis")
    @Size(min = 2, max = 100)
    private String nom;
    
    @Size(max = 500)
    private String description;
    
    @NotNull(message = "Le prix est requis")
    @DecimalMin(value = "0.0", inclusive = false)
    private Double prix;
    
    @NotNull(message = "Le stock est requis")
    @Min(value = 0)
    private Integer stock;
    
    @NotBlank(message = "La catégorie est requise")
    private String categorie;
    
    public ProduitDTO() {}
    
    public ProduitDTO(Long id, String nom, String description, Double prix, Integer stock, String categorie) {
        this.id = id;
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
} 