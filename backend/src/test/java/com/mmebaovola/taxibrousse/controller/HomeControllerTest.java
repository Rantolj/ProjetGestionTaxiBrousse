package com.mmebaovola.taxibrousse.controller;

import com.mmebaovola.taxibrousse.repository.ClientRepository;
import com.mmebaovola.taxibrousse.repository.ReservationRepository;
import com.mmebaovola.taxibrousse.repository.TaxiBrousseRepository;
import com.mmebaovola.taxibrousse.repository.VoyageRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoyageRepository voyageRepository;

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private TaxiBrousseRepository taxiBrousseRepository;

    @MockBean
    private ClientRepository clientRepository;

    @Test
    void getIndex_ReturnsOk() throws Exception {
        // Mock the repository counts
        when(voyageRepository.count()).thenReturn(5L);
        when(reservationRepository.count()).thenReturn(10L);
        when(taxiBrousseRepository.count()).thenReturn(3L);
        when(clientRepository.count()).thenReturn(8L);

        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }
}
