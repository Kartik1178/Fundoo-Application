package com.example.fundoo.messaging;

import com.example.fundoo.config.RabbitMQConfig;
import com.example.fundoo.dto.event.AuditEvent;
import com.example.fundoo.dto.event.NotificationEvent;
import com.example.fundoo.dto.event.ReminderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class RabbitMQProducer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishReminderEvent(ReminderEvent event) {
        log.info("Publishing reminder event for note id: {}", event.getNoteId());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTES_EXCHANGE,
                RabbitMQConfig.REMINDER_ROUTING,
                event);
    }

    public void publishNotificationEvent(NotificationEvent event) {
        log.info("Publishing notification event to: {}", event.getRecipientEmail());
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTES_EXCHANGE,
                RabbitMQConfig.NOTIFICATION_ROUTING,
                event);
    }

    public void publishAuditEvent(Long userId, String action, String resourceType, Long resourceId) {
        AuditEvent event = new AuditEvent(userId, action, resourceType, resourceId, LocalDateTime.now());
        log.debug("Publishing audit event: {} on {} id: {}", action, resourceType, resourceId);
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.NOTES_EXCHANGE,
                RabbitMQConfig.AUDIT_ROUTING,
                event);
    }
}
