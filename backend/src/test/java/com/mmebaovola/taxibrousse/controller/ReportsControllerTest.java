package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.repository.ReservationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.mmebaovola.taxibrousse.repository.TrajetRepository;
import com.mmebaovola.taxibrousse.entity.Trajet;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReportsController.class)
class ReportsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private TrajetRepository trajetRepository;

    @Test
    void getTurnover_ReturnsOk() throws Exception {
        when(reservationRepository.findTurnoverByTaxiPerDay(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());
        when(reservationRepository.findTurnoverByVoyage(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());
        when(reservationRepository.findTurnoverByTrajet(any(LocalDateTime.class), any(LocalDateTime.class),
                any(Long.class)))
                .thenReturn(List.of());
        when(trajetRepository.findAll()).thenReturn(List.of(new Trajet()));

        mockMvc.perform(get("/reports/turnover?trajetId=1"))
                .andExpect(status().isOk());
    }
}
