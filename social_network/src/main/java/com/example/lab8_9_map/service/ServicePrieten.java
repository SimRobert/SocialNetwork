package com.example.lab8_9_map.service;

import com.example.lab8_9_map.domain.Prietenie;
import com.example.lab8_9_map.domain.Utilizator;
import com.example.lab8_9_map.repository.OptionalRepository;
import com.example.lab8_9_map.validators.ValidationException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ServicePrieten {
    OptionalRepository repo_friends;

    public ServicePrieten(OptionalRepository repo_friends) {
        this.repo_friends = repo_friends;
    }

    /**
     * Adauga o prietenie
     * @param u1 - utilizator 1
     * @param u2 - utilizator 2
     * @throws ValidationException
     */
    public void addFriendship(Utilizator u1, Utilizator u2) throws ValidationException {
        if(u1 == null || u2 == null)
            throw new ValidationException("Utilizatori invalizi!");
        Prietenie p = new Prietenie(u1.getId() , u2.getId());
        this.findAll().forEach(x -> {
            if (x.equals(p))
                throw new ValidationException("Prietenie existenta!");
        });
        repo_friends.save(p);
        u1.addFriend(u2);
        u2.addFriend(u1);
    }

    /**
     * Sterge o prietenie
     * @param u1 - utilizator 1
     * @param u2 - utilizator 2
     * @throws ValidationException
     */
    public void removeFriendship(Utilizator u1, Utilizator u2) throws ValidationException{
        if(u1 == null || u2 == null)
            throw new ValidationException("Utilizatori invalizi!");
        Prietenie p = new Prietenie(u1.getId(), u2.getId());
        for(Prietenie x : this.findAll()){
            if(x.equals(p)) {
                this.repo_friends.delete(x.getId());
                break;
            }
        }
        u1.removeFriend(u2);
        u2.removeFriend(u1);
    }

    public Iterable<Prietenie> friendshipInTheMonth(Utilizator user, int month){
        if(user == null)
            throw new ValidationException("User ul trebuie sa existe");
        List<Prietenie> friendships = new ArrayList<>();
        for(Prietenie p : this.findAll()){
            if (p.getDate().getMonthValue() == month && p.contain(user.getId())) {
                friendships.add(p);
            }
        }
        return friendships;
    }

    /**
     *
     * @return lista de prietenii
     */
    public Iterable<Prietenie> findAll(){
        return repo_friends.findAll();
    }

    public void loadFromDB(){
        this.repo_friends.loadFromDB();
    }
}
