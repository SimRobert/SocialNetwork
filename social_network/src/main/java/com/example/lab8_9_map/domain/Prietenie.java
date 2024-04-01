package com.example.lab8_9_map.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


public class Prietenie extends Entity<Tuple<Long, Long>> {
    private LocalDateTime date;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");

    public Prietenie(Long u1, Long u2) {
        id = new Tuple<>(u1,u2);
        date = LocalDateTime.now();
    }

    @Override
    public Tuple<Long, Long> getId() {
        return super.id;
    }

    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public boolean contain(Long userId){
        return this.getId().getRight().equals(userId) || this.getId().getLeft().equals(userId);
    }

    public Long otherFriend(Long id){
        if(this.getId().getRight() == id)
            return this.getId().getLeft();
        return this.getId().getRight();
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                "date=" + date +
                ", id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prietenie)) return false;
        Prietenie that = (Prietenie) o;
        return (getId().getLeft().toString().equals(that.getId().getLeft().toString()) &&
                getId().getRight().toString().equals(that.getId().getRight().toString())) ||
                (getId().getLeft().toString().equals(that.getId().getRight().toString()) &&
                        getId().getRight().toString().equals(that.getId().getLeft().toString()));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId().getLeft(), getId().getRight());
    }
}
