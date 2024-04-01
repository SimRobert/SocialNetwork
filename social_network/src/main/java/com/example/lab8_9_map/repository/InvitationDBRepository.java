package com.example.lab8_9_map.repository;

import com.example.lab8_9_map.domain.Invitation;
import com.example.lab8_9_map.domain.Utilizator;
import com.example.lab8_9_map.validators.ValidationException;
import com.example.lab8_9_map.validators.Validator;

import java.sql.*;
import java.util.*;

public class InvitationDBRepository implements OptionalRepository<Long, Invitation> {
    private String url;
    private String username;
    private String password;
    private Map<Long, Invitation> invitations;

    public InvitationDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        invitations = new HashMap<>();
        this.loadFromDB();
    }

    @Override
    public Optional<Invitation> findOne(Long id) throws ValidationException{
        if(id == null)
            throw new ValidationException("ID-ul nu trebuie sa fie null");
        return Optional.ofNullable(invitations.get(id));
    }

    @Override
    public Iterable<Invitation> findAll() {
        return invitations.values();
    }

    @Override
    public Optional<Invitation> save(Invitation entity) throws ValidationException{
        if(entity == null)
            throw new ValidationException("Invitatia nu trebuie sa fie null");
        //validator.validate(entity);
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO invitations(sendto, sendfrom, status) VALUES (?,?,?)");
        )
        {
            statement.setLong(1, Math.toIntExact(entity.getTo()));
            statement.setLong(2, Math.toIntExact(entity.getFrom()));
            statement.setString(3, entity.getStatus());
            statement.executeUpdate();
            this.loadFromDB();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<Invitation> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Invitation> update(Invitation entity) {
        //this.validator.validate(entity);
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("UPDATE invitations SET status = ? WHERE id = ?");
        )
        {
            statement.setString(1, entity.getStatus());
            statement.setLong(2, entity.getId());
            statement.executeUpdate();
            loadFromDB();
            return Optional.ofNullable(entity);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadFromDB() {
        Set<Invitation> invitations = new HashSet<>();

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from invitations");
            ResultSet set = statement.executeQuery();
        )
        {
            while(set.next()){
                Long id = set.getLong("id");
                Long to = set.getLong("sendto");
                Long from = set.getLong("sendfrom");
                String status = set.getString("status");
                Invitation invitation = new Invitation(to, from, status);
                invitation.setId(id);
                invitations.add(invitation);
            }
            this.invitations.clear();
            invitations.forEach(x ->{
                this.invitations.put(x.getId(), x);
            });
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
