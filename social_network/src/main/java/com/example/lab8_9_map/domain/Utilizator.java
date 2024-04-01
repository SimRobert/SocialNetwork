package com.example.lab8_9_map.domain;


import java.util.ArrayList;
import java.util.List;

public class Utilizator extends Entity<Long> {
    private String firstName;
    private String lastName;
    private List<Utilizator> friends;

    public Utilizator(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        friends = new ArrayList<Utilizator>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Utilizator> getFriends() {
        return friends;
    }
    public void addFriend(Utilizator u){
        friends.add(u);
    }
    public void removeFriend(Utilizator u){
        for(Utilizator f : friends){
            if(f.equals(u)){
                friends.remove(f.getId()); break;
            }
        }
    }
    @Override
    public String toString() {
        return id.toString() + ' ' + firstName + ' ' + lastName + ' ' + '\n';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Utilizator)) return false;
        Utilizator that = (Utilizator) o;
        return getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getFriends().equals(that.getFriends());
    }

    @Override
    public int hashCode() {
        int result = (int) (this.getId() ^ (this.getId() >>> 32));
        result = 31 * result + this.lastName.hashCode();
        result = 31 * result + this.firstName.hashCode();
        return result;
    }
}