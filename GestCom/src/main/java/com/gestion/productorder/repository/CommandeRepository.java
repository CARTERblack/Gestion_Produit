package com.gestion.productorder.repository;

import com.gestion.productorder.entity.Commande;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    Page<Commande> findByEmailClientContainingIgnoreCase(String email, Pageable pageable);

    @Query("SELECT c FROM Commande c WHERE c.dateCommande BETWEEN :startDate AND :endDate")
    Page<Commande> findByDateCommandeBetween(
            @Param("startDate") LocalDateTime startDate, 
            @Param("endDate") LocalDateTime endDate, 
            Pageable pageable);
} 