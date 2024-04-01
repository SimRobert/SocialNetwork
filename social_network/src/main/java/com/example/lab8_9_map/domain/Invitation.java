package com.example.lab8_9_map.domain;

import java.util.Objects;

public class Invitation extends Entity<Long>{
    private Long to;
    private Long from;
    private String status;

    public Invitation(Long to, Long from, String status) {
        this.to = to;
        this.from = from;
        this.status = status;
    }

    public Long getTo() {
        return to;
    }

    public Long getFrom() {
        return from;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Invitation that = (Invitation) o;
        return ((Objects.equals(to, that.to) && Objects.equals(from, that.from)) || (Objects.equals(to, that.from) && Objects.equals(from, that.to))) && Objects.equals(status, that.status);
    }

}
