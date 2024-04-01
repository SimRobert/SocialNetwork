package com.example.lab8_9_map.GUI;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.example.lab8_9_map.domain.Message;
import com.example.lab8_9_map.domain.Utilizator;
import com.example.lab8_9_map.service.ServiceInvitation;
import com.example.lab8_9_map.service.ServiceMessage;
import com.example.lab8_9_map.service.ServicePrieten;
import com.example.lab8_9_map.service.ServiceUtilizator;
import com.example.lab8_9_map.validators.ValidationException;

import java.util.Iterator;

public class ChatController {
    ServiceUtilizator serviceUtilizator;
    ServicePrieten servicePrieten;
    ServiceInvitation serviceInvitation;
    ServiceMessage serviceMessage;
    Stage dialogStage;
    Utilizator currentUser;
    Utilizator fromUser;
    Message reply;

    public ChatController(ServiceUtilizator serviceUtilizator, ServicePrieten servicePrieten, ServiceInvitation serviceInvitation, ServiceMessage serviceMessage, Stage dialogStage, Utilizator currentUser, Utilizator fromUser) {
        this.serviceUtilizator = serviceUtilizator;
        this.servicePrieten = servicePrieten;
        this.serviceInvitation = serviceInvitation;
        this.serviceMessage = serviceMessage;
        this.dialogStage = dialogStage;
        this.currentUser = currentUser;
        this.fromUser = fromUser;
    }

    public void sendMessage(TextField textFieldChat, ChoiceBox<Utilizator> choiceBoxFriends, Message reply){
        String msg = textFieldChat.getText();

        if(msg != null){
            this.serviceMessage.addMessage(choiceBoxFriends.getValue(), this.currentUser, msg, reply);
            textFieldChat.clear();
            this.reply = null;
        }
        else{
            throw new ValidationException("Mesajul trimis nu poate fi gol");
        }
        this.reply = null;
    }

    public void setChat(ListView<Message> listViewChat){
        listViewChat.getItems().clear();
        Iterable<Message> messages = this.serviceMessage.findAll();
        Iterator<Message> iterator = messages.iterator();
        while(iterator.hasNext()){
            Message msg = iterator.next();
            if(msg.contain(this.currentUser.getId()) && msg.contain(fromUser.getId())) {
                if(msg.getReply() == null)
                    listViewChat.getItems().add(msg);
                else
                    listViewChat.getItems().add(msg);
            }
        }
    }
}
