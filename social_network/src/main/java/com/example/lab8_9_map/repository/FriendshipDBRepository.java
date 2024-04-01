package com.example.lab8_9_map.repository;

import com.example.lab8_9_map.domain.Prietenie;
import com.example.lab8_9_map.domain.Tuple;
import com.example.lab8_9_map.validators.ValidationException;
import com.example.lab8_9_map.validators.Validator;

import java.sql.*;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.*;

public class FriendshipDBRepository implements OptionalRepository<Tuple<Long,Long>, Prietenie>{
    private String url;
    private String username;
    private String password;
    private Validator<Prietenie> validator;
    private Map<Tuple<Long,Long>,Prietenie> friendships;

    public FriendshipDBRepository(String url, String username, String password, Validator<Prietenie> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
        friendships = new HashMap<>();
        this.loadFromDB();
    }

    @Override
    public Optional<Prietenie> findOne(Tuple<Long,Long> id) {
        if(id.getLeft() == null || id.getRight() == null)
            throw new ValidationException("ID-ul nu trebuie sa fie null");
        return Optional.ofNullable(friendships.get(id));
    }


    @Override
    public void loadFromDB() {
        Set<Prietenie> friendships = new HashSet<>();

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships");
            ResultSet set = statement.executeQuery();
        )
        {
            while(set.next()){
                Long User1 = set.getLong("id1");
                Long User2 = set.getLong("id2");
                Date date = set.getDate("data_prietenie");
                Prietenie prietenie = new Prietenie(User1,User2);
                prietenie.setDate(Instant.ofEpochMilli(date.getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate().atStartOfDay());
                friendships.add(prietenie);

            }
            this.friendships.clear();
            friendships.forEach(x->{
                this.friendships.put(x.getId(),x);
            });
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Iterable<Prietenie> findAll() {
        return this.friendships.values();
    }

    @Override
    public Optional<Prietenie> save(Prietenie entity) {
        if(entity.getId().getLeft() == null || entity.getId().getRight() == null)
            throw new ValidationException("Prietenia nu poate fi facuta intre utilizatori null.");
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO friendships(id1, id2, data_prietenie) VALUES (?,?,?)");
        )
        {
            statement.setLong(1, Math.toIntExact(entity.getId().getLeft()));
            statement.setLong(2, Math.toIntExact(entity.getId().getRight()));
            statement.setDate(3, java.sql.Date.valueOf(entity.getDate().toLocalDate()));
            statement.executeUpdate();
            this.loadFromDB();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(entity);
    }
    @Override
    public Optional<Prietenie> delete(Tuple<Long, Long> longLongTuple) {
        return Optional.empty();
    }

    @Override
    public Optional<Prietenie> update(Prietenie entity) {
        return Optional.empty();
    }


}
