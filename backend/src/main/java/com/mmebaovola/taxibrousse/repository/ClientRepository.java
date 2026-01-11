package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
