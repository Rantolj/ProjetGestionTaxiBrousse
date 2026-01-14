package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client, Long> {
    List<Client> findByPersonne_NomContainingIgnoreCaseOrPersonne_PrenomContainingIgnoreCaseOrPersonne_ContactContainingIgnoreCase(
            String nom, String prenom, String contact);
}
