package com.mmebaovola.taxibrousse.service;

import com.mmebaovola.taxibrousse.entity.Voyage;
import com.mmebaovola.taxibrousse.entity.VoyageStatus;
import com.mmebaovola.taxibrousse.repository.VoyageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VoyageMonitorService {

    private final VoyageRepository voyageRepository;
    private final NotificationService notificationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(VoyageMonitorService.class);

    // Run every minute
    @Scheduled(fixedDelay = 60_000)
    public void checkDelays() {
        LocalDateTime now = LocalDateTime.now();
        // threshold in minutes
        int threshold = 10;
        List<Voyage> voyages = voyageRepository.findAll();
        for (Voyage v : voyages) {
            if (v.getDateDepart() == null) continue;
            if (v.getDateDepart().isBefore(now.minus(threshold, ChronoUnit.MINUTES))) {
                if (v.getStatus() == VoyageStatus.PREVU || v.getStatus() == VoyageStatus.A_L_HEURE) {
                    VoyageStatus old = v.getStatus();
                    v.setStatus(VoyageStatus.EN_RETARD);
                    voyageRepository.save(v);
                    notificationService.notifyVoyageStatusChange(v, old.name(), VoyageStatus.EN_RETARD.name());
                    LOGGER.info("Voyage {} marked as EN_RETARD by monitor", v.getId());
                }
            }
        }
    }
}