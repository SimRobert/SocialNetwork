package com.example.lab8_9_map.repository;

import com.example.lab8_9_map.domain.Invitation;
import com.example.lab8_9_map.domain.Message;
import com.example.lab8_9_map.validators.ValidationException;

import java.sql.*;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class MessageDBRepository implements OptionalRepository<Long, Message>{
    private String url;
    private String username;
    private String password;
    private Map<Long, Message> messages;

    public MessageDBRepository(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.messages = new HashMap<>();
        loadFromDB();
    }

    @Override
    public Optional<Message> findOne(Long id) {
        if(id == null)
            throw new ValidationException("ID-ul nu trebuie sa fie null");
        return Optional.ofNullable(messages.get(id));
    }

    @Override
    public Iterable<Message> findAll() {
        List<Message> allMessages = new ArrayList<>(this.messages.values());
        Collections.sort(allMessages, Comparator.comparing(Message::getDate));
        return allMessages;
    }

    @Override
    public Optional<Message> save(Message entity) throws ValidationException{
        if(entity == null)
            throw new ValidationException("Mesajul nu trebuie sa fie null");
        //validator.validate(entity);
        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO messages(sendto, sendfrom , message, date, reply) VALUES (?,?,?,?,?)");
        )
        {
            statement.setLong(1, Math.toIntExact(entity.getTo()));
            statement.setLong(2, Math.toIntExact(entity.getFrom()));
            statement.setString(3, entity.getMessage());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(entity.getDate()));
            if(entity.getReply() == null)
                statement.setNull(5, java.sql.Types.BIGINT);
            else
                statement.setLong(5, Math.toIntExact(entity.getReply()));
            statement.executeUpdate();
            this.loadFromDB();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(entity);
    }

    @Override
    public Optional<Message> delete(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Optional<Message> update(Message entity) {
        return Optional.empty();
    }

    @Override
    public void loadFromDB() {
        Set<Message> messages = new HashSet<>();

        try(Connection connection = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = connection.prepareStatement("select * from messages");
            ResultSet set = statement.executeQuery();
        )
        {
            while(set.next()){
                Long id = set.getLong("id");
                Long to = set.getLong("sendto");
                Long from = set.getLong("sendfrom");
                String message = set.getString("message");
                LocalDateTime date = set.getTimestamp("date").toLocalDateTime();
                Long reply = set.getLong("reply");
                if(set.wasNull())
                    reply = null;

                Message msg = new Message(to, from, message, reply);
                msg.setId(id);
                msg.setDate(date);
                messages.add(msg);
            }
            this.messages.clear();
            messages.forEach(x ->{
                this.messages.put(x.getId(), x);
            });
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
