package com.example.lab8_9_map.service;

import com.example.lab8_9_map.domain.Prietenie;
import com.example.lab8_9_map.domain.Utilizator;
import com.example.lab8_9_map.repository.OptionalRepository;
import com.example.lab8_9_map.validators.ValidationException;

import java.util.*;

public class ServiceUtilizator {
    OptionalRepository repo_users;
    public ServiceUtilizator(OptionalRepository repo_users) {
        this.repo_users = repo_users;
    }

    /**
     * Adauga utilizator
     * @param FirstName
     * @param LastName
     * @return utilizatorul adaugat
     * @throws ValidationException
     */
    public Utilizator addUser(String FirstName, String LastName) throws ValidationException {
        Utilizator u = new Utilizator(FirstName, LastName);
        return repo_users.save(u).isEmpty()? u : null;
    }

    /**
     * Sterge un utilizator
     * @param id
     * @return utilizatorul sters
     */

    public Utilizator removeUser(Long id){
        Optional<Utilizator> optionalUtilizator = repo_users.delete(id);
        return optionalUtilizator.isEmpty()? null : (Utilizator) optionalUtilizator.get();
    }

    /**
     * Gaseste un utlizator dupa id
     * @param id
     * @return utilizatorul gasit
     */
    public Utilizator findUser(Long id){
        return repo_users.findOne(id).isEmpty()? null : (Utilizator) repo_users.findOne(id).get();
    }

    public Utilizator updateUser(Long id, String firstname, String lastname){
        Utilizator user = findUser(id);
        user.setFirstName(firstname);
        user.setLastName(lastname);
        Optional<Utilizator> opt = this.repo_users.update(user);
        return opt.isEmpty()? null : opt.get();
    }
    public void setFriends(Iterable<Prietenie> friends){
        Iterator<Utilizator> utilizatorIterator = this.findAll().iterator();
        while(utilizatorIterator.hasNext()){
            Utilizator user = utilizatorIterator.next();
            Iterator<Prietenie> prietenieIterator = friends.iterator();
            while(prietenieIterator.hasNext()){
                Prietenie p = prietenieIterator.next();
                if(p.getId().getLeft() == user.getId()){
                    user.addFriend(this.findUser(p.getId().getRight()));
                }
                else if(p.getId().getRight() == user.getId()){
                    user.addFriend(this.findUser(p.getId().getLeft()));
                }
            }
        }
    }

    public Iterable<Utilizator> findAll(){
        return repo_users.findAll();
    }


    /**
     * Algoritm dfs
     * @param u - sursa drumului
     * @param set - drumul
     * @return lista de utilizatori (drumul gasit)
     */
    private List<Utilizator> DFS(Utilizator u, Set<Utilizator> set) {
        List<Utilizator> list = new ArrayList();
        list.add(u);
        set.add(u);
        Iterator<Utilizator> var4 = u.getFriends().iterator();

        while(true) {
            Utilizator f;
            do {
                if (!var4.hasNext()) {
                    return list;
                }

                f = (Utilizator)var4.next();
            } while(set.contains(f));

            List<Utilizator> l = this.DFS(f, set);
            Iterator var7 = l.iterator();

            while(var7.hasNext()) {
                Utilizator x = (Utilizator)var7.next();
                list.add(x);
            }
        }
    }

    /**
     * Gaseste numarul de comunitati
     * @return numarul de comunitati
     */
    public int nrComunities(){
        int nr = 0;
        Set<Utilizator> set = new HashSet();
        Iterable<Utilizator> u = this.findAll();
        Iterator var4 = u.iterator();

        while(var4.hasNext()) {
            Utilizator utilizator = (Utilizator)var4.next();
            if (!set.contains(utilizator)) {
                ++nr;
                this.DFS(utilizator, set);
            }
        }
        return nr;
    }

    /**
     * Gaseste cel mai lung drum dintr o lista de noduri (numarul lor)
     * @param nodes
     * @return numarul de noduri
     */
    private int longestPath(List<Utilizator> nodes) {
        int max = 0;
        Iterator var3 = nodes.iterator();

        while(var3.hasNext()) {
            Utilizator u = (Utilizator)var3.next();
            int l = this.longestPathFromSource(u);
            if (max < l) {
                max = l;
            }
        }

        return max;
    }


    /**
     * Gasete cel mai lung drum dintr o anumita sursa
     * @param source
     * @return numarul de noduri din drumul gasit
     */
    private int longestPathFromSource(Utilizator source) {
        Set<Utilizator> set = new HashSet();
        return this.BFS(source, set);
    }

    /**
     * Algoritm bfs
     * @param source - sursa
     * @param set - graful dat
     * @return - numarul de noduri
     */
    private int BFS(Utilizator source, Set<Utilizator> set) {
        int max = -1;
        Iterator var4 = source.getFriends().iterator();

        while(var4.hasNext()) {
            Utilizator f = (Utilizator)var4.next();
            if (!set.contains(f)) {
                set.add(f);
                int l = this.BFS(f, set);
                if (l > max) {
                    max = l;
                }

                set.remove(f);
            }
        }

        return max + 1;
    }

    /**
     * Cea mai sociabila comunitate
     * @return lista de utilizatoru din comunitate
     */
    public Iterable<Utilizator> theMostSociableComunity(){
        List<Utilizator> list = new ArrayList();
        Iterable<Utilizator> it = this.findAll();
        Set<Utilizator> set = new HashSet();
        int max = -1;
        Iterator var5 = it.iterator();

        while(var5.hasNext()) {
            Utilizator u = (Utilizator)var5.next();
            if (!set.contains(u)) {
                List<Utilizator> aux = this.DFS(u, set);
                int l = this.longestPath(aux);
                if (l > max) {
                    list = aux;
                    max = l;
                }
            }
        }

        return (Iterable)list;
    }
}
