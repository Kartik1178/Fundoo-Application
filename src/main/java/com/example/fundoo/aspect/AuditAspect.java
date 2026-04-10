package com.example.fundoo.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {

    private static final Logger log = LoggerFactory.getLogger(AuditAspect.class);

    // Target only note-mutating operations
    @Pointcut("execution(* com.example.fundoo.service.impl.NoteServiceImpl.createNote(..))" +
              " || execution(* com.example.fundoo.service.impl.NoteServiceImpl.updateNote(..))" +
              " || execution(* com.example.fundoo.service.impl.NoteServiceImpl.deleteNote(..))" +
              " || execution(* com.example.fundoo.service.impl.NoteServiceImpl.pinNote(..))" +
              " || execution(* com.example.fundoo.service.impl.NoteServiceImpl.archiveNote(..))" +
              " || execution(* com.example.fundoo.service.impl.NoteServiceImpl.trashNote(..))")
    public void noteMutations() {}

    @AfterReturning("noteMutations()")
    public void auditNoteOperation(JoinPoint joinPoint) {
        log.info("AUDIT - Note operation: {} completed successfully",
                joinPoint.getSignature().getName());
    }
}
