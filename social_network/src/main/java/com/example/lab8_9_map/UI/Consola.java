package com.example.lab8_9_map.UI;

import com.example.lab8_9_map.domain.Prietenie;
import com.example.lab8_9_map.domain.Utilizator;
import com.example.lab8_9_map.service.ServicePrieten;
import com.example.lab8_9_map.service.ServiceUtilizator;
import com.example.lab8_9_map.validators.ValidationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Consola {
    ServiceUtilizator serviceUtilizator;
    ServicePrieten servicePrieten;
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm");
    public Consola(ServiceUtilizator serviceUtilizator, ServicePrieten servicePrieten) {
        this.serviceUtilizator = serviceUtilizator;
        this.servicePrieten = servicePrieten;
    }
    private void menu(){
        System.out.println("Meniu");
        System.out.println("0.Exit");
        System.out.println("1.Adauga utilizator");
        System.out.println("2.Sterge utilizator");
        System.out.println("3.Afiseaza lista de utilizatori");
        System.out.println("4.Creeaza o prietenie");
        System.out.println("5.Sterge o prietenie");
        System.out.println("6.Afiseaza lista de prietenii");
        System.out.println("7.Afiseaza numarul de comunitati");
        System.out.println("8.Afiseaza cea mai sociabila comunitate");
        System.out.println("9.Afiseaza relatiile de prietenie ale unui utilizator intr o luna data");

    }
    private void addUser(String firstName, String lastName) throws ValidationException {
        Utilizator u = serviceUtilizator.addUser(firstName, lastName);
        System.out.println("Am adaugat cu succes utilizatorul: " + firstName + ' ' + lastName);
    }
    private void removeUser(Long id){
        Utilizator u = serviceUtilizator.removeUser(id);
        if(u == null){
            System.out.println("Nu am gasit utilizatorul: " + id.toString());
        }
        else{
            System.out.println("Am gasit utilizatorul: " + u);
            servicePrieten.loadFromDB();
        }
    }
    private Utilizator findUser(Long id){
        return serviceUtilizator.findUser(id);
    }
    private void addFriendship(Long id1, Long id2){
        servicePrieten.addFriendship(findUser(id1), findUser(id2));
        System.out.println("Prietenie adaugata");
    }
    private void removeFriendship(Long id1, Long id2){
        //servicePrieten.removeFriendship(findUser(id1), findUser(id2));
        //System.out.println("Prietenie stersa");
    }
    private void printTheMostSociableComunity(){
        Iterable<Utilizator> comunity = serviceUtilizator.theMostSociableComunity();
        System.out.println("Cea mai sociabila comunitate: ");
        comunity.forEach(x ->{
            System.out.print(x);
        });
    }
    private void printNrComunities(){
        int nr = serviceUtilizator.nrComunities();
        System.out.print("Numar de comunitati: ");
        System.out.println(nr);
    }

    private void printFriendshipMonthUser(Long userId, int month){
        Iterable<Prietenie> p = this.servicePrieten.friendshipInTheMonth(findUser(userId), month);
        System.out.println("Prieteniile gasite sunt: ");
        printAllFriendship(p);
    }

    private void populate(){
        serviceUtilizator.addUser("Robert","Marcus");
        serviceUtilizator.addUser("Paul", "Spac");
        serviceUtilizator.addUser("Iuliana", "Popescu");
        serviceUtilizator.addUser("Alex", "Boldog");
        serviceUtilizator.addUser("George", "Borta");
        serviceUtilizator.addUser("Marian", "Dohan");
        serviceUtilizator.addUser("Claudiu", "Sim");
        serviceUtilizator.addUser("Horia", "Orban");
        serviceUtilizator.addUser("Ioana", "Hasas");
    }
    private void printAllUsers(){
        System.out.println("*************************");
        serviceUtilizator.findAll().forEach(System.out::print);
        System.out.println("*************************");
    }
    private void printAllFriendship(Iterable<Prietenie> friendships){
        System.out.println("*************************");
        for(Prietenie u : friendships){
            Utilizator u1 = serviceUtilizator.findUser(u.getId().getLeft());
            Utilizator u2 = serviceUtilizator.findUser(u.getId().getRight());
            System.out.println("Prieten1: " + u1.toString() + "Prieten2: " +
                    u2.toString() + "Data prieteniei: " + u.getDate() + '\n' + "_______________________________");
        }
        System.out.println("*************************");
    }

    private void setFriends(){
        this.serviceUtilizator.setFriends(servicePrieten.findAll());
    }
    public void run() throws IOException{
        boolean notExit = true;
        while(notExit){
            this.menu();
            Scanner scanner = new Scanner(System.in);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Da o comanda: ");
            int cmd = 0;
            try {
                cmd = scanner.nextInt();
            }
            catch(Exception ex){
                System.out.println("Comanda trebuie sa fie un numar!");
            }
            try {
                switch (cmd) {
                    case 0:
                        notExit = false;
                        break;
                    case 1:
                        System.out.print("FirstName: ");
                        String firstName1 = reader.readLine();
                        System.out.print("LastName: ");
                        String lastName1 = reader.readLine();
                        this.addUser(firstName1, lastName1);
                        break;
                    case 2:
                        System.out.print("ID: ");
                        Long id = scanner.nextLong();
                        this.removeUser(id);
                    case 3:
                        this.printAllUsers();
                        break;
                    case 4:
                        this.printAllUsers();
                        System.out.print("Da ID-ul primului utilizator: ");
                        Long id1 = scanner.nextLong();
                        System.out.print("Da ID-ul celui de-al doilea utilizator: ");
                        Long id2 = scanner.nextLong();
                        this.addFriendship(id1, id2);
                        break;
                    case 5:
                        this.printAllUsers();
                        System.out.print("Da ID-ul primului utilizator: ");
                        Long id3 = scanner.nextLong();
                        System.out.print("Da ID-ul celui de-al doilea utilizator: ");
                        Long id4 = scanner.nextLong();
                        this.removeFriendship(id3, id4);
                        break;
                    case 6:
                        this.printAllFriendship(this.servicePrieten.findAll());
                        break;
                    case 7:
                        this.setFriends();
                        this.printNrComunities();
                        break;
                    case 8:
                        this.setFriends();
                        this.printTheMostSociableComunity();
                        break;
                    case 9:
                        this.printAllUsers();
                        System.out.print("Da ID-ul primului utilizator: ");
                        Long id5 = scanner.nextLong();
                        System.out.print("Da luna din an: ");
                        int month = scanner.nextInt();
                        if(0 > month || month > 12)
                            throw new ValidationException("Luna trebuie sa fie intre 1-12");
                        this.printFriendshipMonthUser(id5, month);
                        break;
                    default:
                        System.out.println("Comanda invalida!");
                        break;
                }
            }
            catch(ValidationException ex){
                System.out.println(ex);
            }
        }
    }
}
