package com.gigalove.chat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    private final MessageRepository messageRepository;

    public ChatController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    private Long currentUserId() {
        try { return Long.valueOf(SecurityContextHolder.getContext().getAuthentication().getName()); } catch (Exception e) { return 1L; }
    }

    @GetMapping("/{matchId}")
    public List<MessageEntity> getMessages(@PathVariable Long matchId) {
        return messageRepository.findByMatchIdOrderByCreatedAtDesc(matchId);
    }

    @PostMapping("/{matchId}")
    public ResponseEntity<MessageEntity> send(@PathVariable Long matchId, @RequestBody Map<String, String> body) {
        String content = body.getOrDefault("content", "");
        MessageEntity m = new MessageEntity();
        m.setMatchId(matchId);
        m.setSenderId(currentUserId());
        m.setContent(content);
        m.setCreatedAt(Instant.now());
        messageRepository.save(m);
        return ResponseEntity.ok(m);
    }
}


