package com.mmebaovola.taxibrousse.repository;

import com.mmebaovola.taxibrousse.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
