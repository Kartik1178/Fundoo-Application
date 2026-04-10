package com.example.fundoo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenCacheService {

    private static final Logger log = LoggerFactory.getLogger(TokenCacheService.class);

    private final RedisTemplate<String, Object> redisTemplate;

    public TokenCacheService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Store OTP for password reset - expires in 10 minutes
    public void storeResetOtp(String email, String otp) {
        log.debug("Storing reset OTP for email: {}", email);
        redisTemplate.opsForValue().set("reset:otp:" + email, otp, Duration.ofMinutes(10));
    }

    public Object getResetOtp(String email) {
        return redisTemplate.opsForValue().get("reset:otp:" + email);
    }

    public void deleteResetOtp(String email) {
        redisTemplate.delete("reset:otp:" + email);
    }

    // Store registration verification token - expires in 24 hours
    public void storeVerificationToken(String email, String token) {
        log.debug("Storing verification token for email: {}", email);
        redisTemplate.opsForValue().set("verify:token:" + email, token, Duration.ofHours(24));
    }

    public Object getVerificationToken(String email) {
        return redisTemplate.opsForValue().get("verify:token:" + email);
    }

    // Blacklist a token on logout - store until JWT expiry (24h)
    public void blacklistToken(String token) {
        log.info("Blacklisting token");
        redisTemplate.opsForValue().set("blacklist:token:" + token, "true", Duration.ofHours(24));
    }

    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey("blacklist:token:" + token));
    }
}
