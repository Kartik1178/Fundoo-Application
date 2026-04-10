package com.example.fundoo.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsProducer {

    private static final Logger log = LoggerFactory.getLogger(JmsProducer.class);

    public static final String REGISTRATION_QUEUE  = "user.registration.queue";
    public static final String PASSWORD_RESET_QUEUE = "user.password.reset.queue";

    private final JmsTemplate jmsTemplate;

    public JmsProducer(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendRegistrationEmail(String email) {
        log.info("Queuing registration email for: {}", email);
        jmsTemplate.convertAndSend(REGISTRATION_QUEUE, email);
    }

    public void sendPasswordResetEmail(String email) {
        log.info("Queuing password reset email for: {}", email);
        jmsTemplate.convertAndSend(PASSWORD_RESET_QUEUE, email);
    }
}
