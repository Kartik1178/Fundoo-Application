package com.example.fundoo.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsConsumer {

    private static final Logger log = LoggerFactory.getLogger(JmsConsumer.class);

    @JmsListener(destination = JmsProducer.REGISTRATION_QUEUE)
    public void handleRegistrationEmail(String email) {
        log.info("JMS received registration email request for: {}", email);
        // In production: send welcome/verification email via JavaMailSender
        log.info("Sending welcome email to: {}", email);
    }

    @JmsListener(destination = JmsProducer.PASSWORD_RESET_QUEUE)
    public void handlePasswordResetEmail(String email) {
        log.info("JMS received password reset email request for: {}", email);
        // In production: send password reset link via JavaMailSender
        log.info("Sending password reset email to: {}", email);
    }
}
