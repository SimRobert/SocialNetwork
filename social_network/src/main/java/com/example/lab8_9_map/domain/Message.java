package com.example.lab8_9_map.domain;

import java.time.LocalDateTime;

public class Message extends Entity<Long>{
    private Long from;
    private Long to;
    private String message;
    private LocalDateTime date;
    private Long reply;

    public Message(Long from, Long to, String message, Long reply) {
        this.from = from;
        this.to = to;
        this.message = message;
        this.reply = reply;
        this.date = LocalDateTime.now();
    }
    public boolean contain(Long userId){
        return this.getTo().equals(userId) || this.getFrom().equals(userId);
    }
    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Long getReply() {
        return reply;
    }

    public void setFrom(Long from) {
        this.from = from;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setReply(Long reply) {
        this.reply = reply;
    }
}
