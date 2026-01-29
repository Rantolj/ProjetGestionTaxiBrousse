package com.mmebaovola.taxibrousse.service;

import com.mmebaovola.taxibrousse.entity.PrixProduit;
import com.mmebaovola.taxibrousse.entity.Produit;
import com.mmebaovola.taxibrousse.repository.PrixProduitRepository;
import com.mmebaovola.taxibrousse.repository.ProduitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProduitService {

    private final ProduitRepository produitRepository;
    private final PrixProduitRepository prixProduitRepository;

    public ProduitService(ProduitRepository produitRepository, PrixProduitRepository prixProduitRepository) {
        this.produitRepository = produitRepository;
        this.prixProduitRepository = prixProduitRepository;
    }

    public List<Produit> findAll() {
        return produitRepository.findAllByOrderByLibelleAsc();
    }

    public Optional<Produit> findById(Long id) {
        return produitRepository.findById(id);
    }

    public Produit save(Produit produit) {
        return produitRepository.save(produit);
    }

    /**
     * Crée un produit avec son prix initial
     */
    public Produit createWithPrice(String libelle, BigDecimal prix) {
        Produit produit = new Produit();
        produit.setLibelle(libelle);
        produit = produitRepository.save(produit);

        PrixProduit prixProduit = new PrixProduit();
        prixProduit.setProduit(produit);
        prixProduit.setPrix(prix);
        prixProduit.setDateEffective(LocalDate.now());
        prixProduitRepository.save(prixProduit);

        return produit;
    }

    /**
     * Met à jour le prix d'un produit (ajoute une nouvelle entrée dans l'historique)
     */
    public void updatePrice(Long produitId, BigDecimal nouveauPrix, LocalDate dateEffective) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

        PrixProduit prixProduit = new PrixProduit();
        prixProduit.setProduit(produit);
        prixProduit.setPrix(nouveauPrix);
        prixProduit.setDateEffective(dateEffective != null ? dateEffective : LocalDate.now());
        prixProduitRepository.save(prixProduit);
    }

    /**
     * Récupère le prix actuel d'un produit
     */
    public BigDecimal getPrixActuel(Long produitId) {
        return prixProduitRepository.findPrixActuelByProduitId(produitId)
                .orElse(BigDecimal.ZERO);
    }

    /**
     * Récupère le prix d'un produit à une date donnée
     */
    public BigDecimal getPrixADate(Long produitId, LocalDate date) {
        return prixProduitRepository.findPrixActuel(produitId, date)
                .map(PrixProduit::getPrix)
                .orElse(BigDecimal.ZERO);
    }

    public void delete(Long id) {
        produitRepository.deleteById(id);
    }

    public List<PrixProduit> getHistoriquePrix(Long produitId) {
        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        return prixProduitRepository.findByProduitOrderByDateEffectiveDesc(produit);
    }
}
