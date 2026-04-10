package com.example.fundoo.messaging;

import com.example.fundoo.config.RabbitMQConfig;
import com.example.fundoo.dto.event.AuditEvent;
import com.example.fundoo.dto.event.NotificationEvent;
import com.example.fundoo.dto.event.ReminderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.REMINDER_QUEUE)
    public void consumeReminderEvent(ReminderEvent event) {
        log.info("Received reminder event for note: {} user: {}", event.getNoteId(), event.getUserId());
        // In production: trigger email/push notification for reminder
        log.info("Processing reminder for note '{}' at time: {}", event.getNoteTitle(), event.getReminderTime());
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void consumeNotificationEvent(NotificationEvent event) {
        log.info("Received notification event type: {} to: {}", event.getEventType(), event.getRecipientEmail());
        // In production: use JavaMailSender to send the email
        log.info("Sending '{}' to {}", event.getSubject(), event.getRecipientEmail());
    }

    @RabbitListener(queues = RabbitMQConfig.AUDIT_QUEUE)
    public void consumeAuditEvent(AuditEvent event) {
        log.info("Audit: user {} performed {} on {} id: {}",
                event.getUserId(), event.getAction(), event.getResourceType(), event.getResourceId());
    }
}
