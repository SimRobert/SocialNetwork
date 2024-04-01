package com.example.lab8_9_map;

import com.example.lab8_9_map.service.ServicePrieten;
import com.example.lab8_9_map.validators.PrietenieValidator;
import com.example.lab8_9_map.validators.UtilizatorValidator;
import com.example.lab8_9_map.UI.Consola;
import com.example.lab8_9_map.repository.FriendshipDBRepository;
import com.example.lab8_9_map.repository.UserDBRepository;
import com.example.lab8_9_map.service.ServiceUtilizator;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {

        String url="jdbc:postgresql://localhost:5432/socialnetwork";
        String username = "postgres";
        String password = "Robi2003";

        UtilizatorValidator val_users = new UtilizatorValidator();
        PrietenieValidator val_friends = new PrietenieValidator();
        UserDBRepository repo_users = new UserDBRepository(url, username, password, val_users);
        FriendshipDBRepository repo_friends = new FriendshipDBRepository(url, username, password,val_friends);
        ServiceUtilizator srv_users = new ServiceUtilizator(repo_users);
        ServicePrieten srv_friends = new ServicePrieten(repo_friends);


        Consola ui = new Consola(srv_users, srv_friends);
        ui.run();
    }
}
