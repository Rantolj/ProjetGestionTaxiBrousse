package com.mmebaovola.taxibrousse.service;

import com.mmebaovola.taxibrousse.entity.Notification;
import com.mmebaovola.taxibrousse.entity.Reservation;
import com.mmebaovola.taxibrousse.entity.Voyage;
import com.mmebaovola.taxibrousse.repository.NotificationRepository;
import com.mmebaovola.taxibrousse.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ReservationRepository reservationRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

    public void notifyVoyageStatusChange(Voyage voyage, String oldStatus, String newStatus) {
        String message = "Statut du voyage " + (voyage.getTrajet() != null ? voyage.getTrajet().getNom() : "#" + voyage.getId())
                + " modifié : " + oldStatus + " → " + newStatus;

        // Notify chauffeur if exists
        if (voyage.getChauffeur() != null && voyage.getChauffeur().getId() != null) {
            Notification n = new Notification();
            n.setDestinataire("chauffeur:" + voyage.getChauffeur().getId());
            n.setMessage(message);
            n.setVoyage(voyage);
            notificationRepository.save(n);
            LOGGER.info("Notification created for chauffeur {}: {}", voyage.getChauffeur().getId(), message);
        }

        // Notify all clients who have reservations on this voyage
        List<Reservation> reservations = reservationRepository.findByVoyage_Id(voyage.getId());
        for (Reservation r : reservations) {
            Notification n = new Notification();
            n.setDestinataire("client:" + (r.getClient() != null ? r.getClient().getId() : "unknown"));
            n.setMessage(message);
            n.setVoyage(voyage);
            notificationRepository.save(n);
            LOGGER.info("Notification created for client {}: {}", r.getClient() != null ? r.getClient().getId() : "?", message);
        }

        // For MVP we just create notifications in DB and log them; actual sending (SMS/email) can be added later.
    }
}
