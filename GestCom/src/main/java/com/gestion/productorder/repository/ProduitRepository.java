package com.gestion.productorder.repository;

import com.gestion.productorder.entity.Produit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    @Query("SELECT p FROM Produit p WHERE " +
           "LOWER(p.nom) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.categorie) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Produit> findByNomContainingIgnoreCaseOrCategorieContainingIgnoreCase(
            @Param("searchTerm") String searchTerm, Pageable pageable);

    Page<Produit> findByCategorieIgnoreCase(String categorie, Pageable pageable);

    @Query("SELECT DISTINCT p.categorie FROM Produit p ORDER BY p.categorie")
    List<String> findDistinctCategories();
} 