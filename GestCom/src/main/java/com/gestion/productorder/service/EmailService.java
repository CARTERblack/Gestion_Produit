package com.gestion.productorder.service;

import com.gestion.productorder.entity.Commande;
import com.gestion.productorder.entity.Produit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    // @Autowired
    // private JavaMailSender mailSender;

    public void sendOrderConfirmation(Commande commande) {
        // Désactivé temporairement pour éviter l'erreur de bean manquant
        // SimpleMailMessage message = new SimpleMailMessage();
        // message.setTo(commande.getEmailClient());
        // message.setSubject("Confirmation de commande #" + commande.getId());

        // StringBuilder content = new StringBuilder();
        // content.append("Bonjour ").append(commande.getNomClient()).append(",\n\n");
        // content.append("Votre commande #").append(commande.getId()).append(" a été confirmée.\n\n");
        // content.append("Détails de la commande:\n");
        // content.append("Date: ").append(commande.getDateCommande()).append("\n");
        // content.append("Produits commandés:\n");
        // for (Produit produit : commande.getProduitsCommandes()) {
        //     content.append("- ").append(produit.getNom())
        //            .append(" (").append(produit.getPrix()).append("€)\n");
        // }
        // content.append("\nTotal: ").append(commande.getTotal()).append("€\n\n");
        // content.append("Merci pour votre confiance!\n");
        // content.append("L'équipe de gestion des commandes");
        // message.setText(content.toString());
        // mailSender.send(message);
    }
} 