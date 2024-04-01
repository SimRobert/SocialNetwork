package com.example.lab8_9_map.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.example.lab8_9_map.repository.FriendshipDBRepository;
import com.example.lab8_9_map.repository.InvitationDBRepository;
import com.example.lab8_9_map.repository.MessageDBRepository;
import com.example.lab8_9_map.repository.UserDBRepository;
import com.example.lab8_9_map.service.ServiceInvitation;
import com.example.lab8_9_map.service.ServiceMessage;
import com.example.lab8_9_map.service.ServicePrieten;
import com.example.lab8_9_map.service.ServiceUtilizator;
import com.example.lab8_9_map.validators.PrietenieValidator;
import com.example.lab8_9_map.validators.UtilizatorValidator;

import java.io.IOException;

public class StartApplication extends Application {
    private ServiceUtilizator serviceUtilizator;
    private ServicePrieten servicePrieten;
    private ServiceInvitation serviceInvitation;
    private ServiceMessage serviceMessage;

    @Override
    public void start(Stage stage) throws IOException {

        String url="jdbc:postgresql://localhost:5432/socialnetwork";
        String username = "postgres";
        String password = "Robi2003";

        UserDBRepository userDBRepository = new UserDBRepository(url, username, password, new UtilizatorValidator());
        FriendshipDBRepository friendshipDBRepository = new FriendshipDBRepository(url, username, password, new PrietenieValidator());
        MessageDBRepository messageDBRepository = new MessageDBRepository(url,username,password);
        InvitationDBRepository invitationDBRepository = new InvitationDBRepository(url, username, password);

        //userDBRepository.loadFromDB();
        //friendshipDBRepository.loadFromDB();

        serviceUtilizator = new ServiceUtilizator(userDBRepository);
        servicePrieten = new ServicePrieten(friendshipDBRepository);
        serviceInvitation = new ServiceInvitation(invitationDBRepository);
        serviceMessage = new ServiceMessage(messageDBRepository);

        initView(stage);
        stage.setTitle("SocialNetwork");
        stage.getIcons().add(new Image("C:\\Users\\Robert\\Documents\\proiecte bune\\social_network\\profile.png"));
        stage.setWidth(593);
        stage.show();
    }

    static void main(String[] args) {
        launch(args);
    }

    private void initView(Stage primaryStage) throws IOException {

        try {
            FXMLLoader userLoader = new FXMLLoader();
            userLoader.setLocation(getClass().getResource("/view/user-view.fxml"));
            AnchorPane userLayout = userLoader.load();
            primaryStage.setScene(new Scene(userLayout));

            UserController userController = userLoader.getController();
            userController.setService(serviceUtilizator, servicePrieten, serviceInvitation, serviceMessage);
        }
        catch(RuntimeException ex){
            ex.getMessage();
        }
    }
}
