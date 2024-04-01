package com.example.lab8_9_map.GUI;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.lab8_9_map.domain.Invitation;
import com.example.lab8_9_map.domain.Message;
import com.example.lab8_9_map.domain.Prietenie;
import com.example.lab8_9_map.domain.Utilizator;
import com.example.lab8_9_map.observers.Observer;
import com.example.lab8_9_map.service.ServiceInvitation;
import com.example.lab8_9_map.service.ServiceMessage;
import com.example.lab8_9_map.service.ServicePrieten;
import com.example.lab8_9_map.service.ServiceUtilizator;
import com.example.lab8_9_map.validators.ValidationException;

import java.util.HashSet;
import java.util.Iterator;

public class ProfileController implements Observer {

    ServiceUtilizator serviceUtilizator;
    ServicePrieten servicePrieten;
    ServiceInvitation serviceInvitation;
    ServiceMessage serviceMessage;
    Stage dialogStage;
    Utilizator user;
    Message reply;
    ChatController chat;

    @FXML
    private Label labelFirstname1;
    @FXML
    private Label labelLastname1;
    @FXML
    private Label labelFirstname2;
    @FXML
    private Label labelLastname2;
    @FXML
    private ChoiceBox<Utilizator> choiceBoxFriends;
    @FXML
    private ListView<Message> listViewChat;
    @FXML
    private Label labelReply;
    @FXML
    private TextField textFieldChat;
    @FXML
    private ListView<Utilizator> listViewSuggestions;
    @FXML
    private ListView<Utilizator> listViewFriends;
    @FXML
    private ListView<Invitation> listViewInvitations;

    @FXML
    public void initialize() {
        this.listViewInvitations.setCellFactory(param-> new ListCell<Invitation>(){
            @Override
            protected void updateItem(Invitation invitation, boolean empty) {
                super.updateItem(invitation, empty);

                if (empty || invitation == null) {
                    setText(null);
                } else {
                    Utilizator fromUser = serviceUtilizator.findUser(invitation.getFrom());
                    setText(fromUser.toString());
                }
            }
        });
        choiceBoxFriends.setOnAction(event -> {
            Utilizator selectedOption = choiceBoxFriends.getValue();
            this.chat = new ChatController(serviceUtilizator, servicePrieten, serviceInvitation, serviceMessage, dialogStage, this.user, selectedOption);
            this.chat.setChat(this.listViewChat);
        });
        this.listViewChat.setCellFactory(param-> new ListCell<Message>(){
            @Override
            protected void updateItem(Message message, boolean empty) {
                super.updateItem(message, empty);

                if (empty || message == null) {
                    setText(null);
                } else {
                    if(message.getTo().equals(user.getId())){
                        setStyle("-fx-alignment: CENTER_LEFT");
                    }
                    else if(message.getFrom().equals(user.getId())){
                        setStyle("-fx-alignment: CENTER_RIGHT");
                    }
                    if(message.getReply() == null)
                        setText(message.getMessage());
                    else if((message.getReply() != null)){
                        String reply = serviceMessage.findMessage(message.getReply()).getMessage();
                        setText("Reply: " + reply.substring(0,Math.min(reply.length(),10)) + "... ----- " + message.getMessage());
                    }
                }
            }
        });
    }
    @FXML
    private void refresh(){
        initModel();
    }
    @FXML
    private void refreshChat(){
        this.chat.setChat(this.listViewChat);
        this.reply = null;
        this.labelReply.setText("");
    }
    @FXML
    public void acceptInvitation(){
        Invitation i = listViewInvitations.getSelectionModel().getSelectedItem();
        if(i != null) {
            try{
                Utilizator user = this.serviceUtilizator.findUser(i.getFrom());
                this.servicePrieten.addFriendship(user, this.user);
                this.serviceInvitation.updateInvitation(i.getId(), "approved");
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Invitation", "Am acceptat cu succes o cerere de prietenie!");
                initModel();
            }
            catch(ValidationException ex){
                MessageAlert.showErrorMessage(null, ex.toString());
            }
        }
        else{
            MessageAlert.showErrorMessage(null, "Nu am selectat niciun utilizator!");
        }
    }
    @FXML
    public void declineInvitation(){
        Invitation i = listViewInvitations.getSelectionModel().getSelectedItem();
        if(i != null) {
            try{
                Utilizator user = this.serviceUtilizator.findUser(i.getFrom());
                this.serviceInvitation.updateInvitation(i.getId(), "rejected");
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Invitation", "Am refuzat cu succes o cerere de prietenie!");
                this.setListViewInvitations();
            }
            catch(ValidationException ex){
                MessageAlert.showErrorMessage(null, ex.toString());
            }
        }
        else{
            MessageAlert.showErrorMessage(null, "Nu am selectat niciun utilizator!");
        }
    }
    @FXML
    public void sendInvitation(){
        Utilizator user = listViewSuggestions.getSelectionModel().getSelectedItem();
        if(user != null) {
            try{
                this.serviceInvitation.addInvitations(this.user, user);
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Send Invitation", "Am trimis cu succes o cerere de prietenie!");
            }
            catch(ValidationException ex){
                MessageAlert.showErrorMessage(null, ex.toString());
            }
        }
        else{
            MessageAlert.showErrorMessage(null, "Nu am selectat niciun utilizator!");
        }
    }
    @FXML
    private void setLabelsFriends(){
        Utilizator user = listViewFriends.getSelectionModel().getSelectedItem();
        this.labelFirstname2.setText(user.getFirstName());
        this.labelLastname2.setText(user.getLastName());
    }
    @FXML
    private void sendMessage(){
        try {
            this.chat.sendMessage(this.textFieldChat, this.choiceBoxFriends, this.reply);
            this.chat.setChat(this.listViewChat);
            this.reply = null;
            this.labelReply.setText("");
        }
        catch (ValidationException ex){
            MessageAlert.showErrorMessage(null, ex.toString());
        }
    }
    @FXML
    private void setLabelReply(){
        Message msg = this.listViewChat.getSelectionModel().getSelectedItem();
        this.reply = msg;
        this.labelReply.setText("Reply: " + msg.getMessage().substring(0,Math.min(msg.getMessage().length(),15)) + "...");

    }
    private void initModel(){
        this.user.getFriends().clear();
        setFriends();
        //setChat();
        setChoiceBoxFriends();
        setListViewFriends();
        setListViewInvitations();
        setListViewSuggestions();
    }

    @Override
    public void update() {
        this.chat.setChat(this.listViewChat);
    }

    private void setChoiceBoxFriends(){
        this.choiceBoxFriends.getItems().addAll(this.user.getFriends());
    }
    private void setLabelsProfile(Utilizator user){
        this.labelFirstname1.setText(user.getFirstName());
        this.labelLastname1.setText(user.getLastName());
    }
    private void setListViewSuggestions(){
        this.listViewSuggestions.getItems().clear();
        Iterable<Utilizator> allUsers = serviceUtilizator.findAll();
        Iterator<Utilizator> usersIterator = allUsers.iterator();
        HashSet<Utilizator> set = new HashSet<>();
        set.addAll(this.user.getFriends());
        set.add(this.user);
        while(usersIterator.hasNext()){
            Utilizator u = usersIterator.next();
            if(!set.contains(u)) {
                this.listViewSuggestions.getItems().add(u);
            }
        }
    }
    private void setListViewFriends(){
        this.listViewFriends.getItems().clear();
        //setFriends();
        ObservableList<Utilizator> friends = FXCollections.observableArrayList(this.user.getFriends());
        this.listViewFriends.setItems(friends);
    }
    private void setListViewInvitations(){
        this.listViewInvitations.getItems().clear();
        ObservableList<Invitation> invitations = FXCollections.observableArrayList();
        Iterable<Invitation> allInvitations = this.serviceInvitation.findAll();
        Iterator<Invitation> iterator = allInvitations.iterator();
        while(iterator.hasNext()){
            Invitation i = iterator.next();
            if(i.getTo().equals(this.user.getId()) && i.getStatus().equals("pending"))
                invitations.add(i);
        }
        this.listViewInvitations.setItems(invitations);
    }
    private void setFriends(){
        Iterable<Prietenie> prietenii = servicePrieten.findAll();
        Iterator<Prietenie> prieteniiIterator = prietenii.iterator();
        while(prieteniiIterator.hasNext()){
            Prietenie p = prieteniiIterator.next();
            if(p.contain(this.user.getId())) {
                this.user.addFriend(serviceUtilizator.findUser(p.otherFriend(user.getId())));
            }
        }
    }
    public void setService(ServiceUtilizator serviceUtilizator, ServicePrieten servicePrieten, ServiceInvitation serviceInvitation, ServiceMessage serviceMessage, Stage stage, Utilizator user) {
        this.serviceUtilizator = serviceUtilizator;
        this.servicePrieten = servicePrieten;
        this.serviceInvitation = serviceInvitation;
        this.serviceMessage = serviceMessage;
        this.serviceMessage.addObserver(this);
        this.dialogStage=stage;
        this.user=user;
        if (null != user) {
            setLabelsProfile(user);
        }
        initModel();
    }
}
