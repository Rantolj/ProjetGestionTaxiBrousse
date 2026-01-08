package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Personne;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PersonneRepositoryTest {

    @Autowired
    private PersonneRepository personneRepository;

    @Test
    void saveAndFind() {
        Personne p = new Personne();
        p.setNom("TestNom");
        p.setPrenom("TestPrenom");
        p.setContact("+261000000");

        Personne saved = personneRepository.save(p);

        assertThat(saved.getId()).isNotNull();
        assertThat(personneRepository.findById(saved.getId())).isPresent();
    }
}