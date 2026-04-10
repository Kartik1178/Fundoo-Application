package com.example.fundoo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String NOTES_EXCHANGE      = "notes.exchange";
    public static final String REMINDER_QUEUE      = "reminder.queue";
    public static final String REMINDER_ROUTING    = "reminder.created";
    public static final String NOTIFICATION_QUEUE  = "notification.queue";
    public static final String NOTIFICATION_ROUTING = "notification.send";
    public static final String AUDIT_QUEUE         = "audit.queue";
    public static final String AUDIT_ROUTING       = "audit.event";

    // Exchange
    @Bean
    public DirectExchange notesExchange() {
        return new DirectExchange(NOTES_EXCHANGE, true, false);
    }

    // Reminder queue + binding
    @Bean
    public Queue reminderQueue() {
        return new Queue(REMINDER_QUEUE, true);
    }

    @Bean
    public Binding reminderBinding(Queue reminderQueue, DirectExchange notesExchange) {
        return BindingBuilder.bind(reminderQueue).to(notesExchange).with(REMINDER_ROUTING);
    }

    // Notification queue + binding
    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public Binding notificationBinding(Queue notificationQueue, DirectExchange notesExchange) {
        return BindingBuilder.bind(notificationQueue).to(notesExchange).with(NOTIFICATION_ROUTING);
    }

    // Audit queue + binding
    @Bean
    public Queue auditQueue() {
        return new Queue(AUDIT_QUEUE, true);
    }

    @Bean
    public Binding auditBinding(Queue auditQueue, DirectExchange notesExchange) {
        return BindingBuilder.bind(auditQueue).to(notesExchange).with(AUDIT_ROUTING);
    }

    // JSON message converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory) {
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
