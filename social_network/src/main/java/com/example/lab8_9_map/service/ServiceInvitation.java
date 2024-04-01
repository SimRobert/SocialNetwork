package com.example.lab8_9_map.service;

import com.example.lab8_9_map.domain.Invitation;
import com.example.lab8_9_map.domain.Utilizator;
import com.example.lab8_9_map.repository.OptionalRepository;
import com.example.lab8_9_map.validators.ValidationException;

import java.util.Iterator;
import java.util.Optional;

public class ServiceInvitation {
    OptionalRepository repo_invitations;

    public ServiceInvitation(OptionalRepository invitations) {
        this.repo_invitations = invitations;
    }
    private boolean exists(Invitation i){
        Iterable<Invitation> invitations = this.findAll();
        Iterator<Invitation> iterator = invitations.iterator();
        while(iterator.hasNext()){
            Invitation in = iterator.next();
            if(in.equals(i))
                return true;
        }
        return false;
    }
    public Invitation addInvitations(Utilizator user1, Utilizator user2) throws ValidationException {
        Invitation i = new Invitation(user2.getId(), user1.getId(), "pending");
        if(this.exists(i))
            throw new ValidationException("Invitatia exista deja sau ai primit deja o invitatie de la utilizatorul selectat!");

        return repo_invitations.save(i).isEmpty()? i : null;
    }
    public Invitation findInvitation(Long id){
        return repo_invitations.findOne(id).isEmpty()? null : (Invitation) repo_invitations.findOne(id).get();
    }
    public Invitation updateInvitation(Long id, String status){
        Invitation invitation = findInvitation(id);
        invitation.setStatus(status);
        Optional<Invitation> opt = this.repo_invitations.update(invitation);
        if(opt.isEmpty())
            return null;
        else{

            return opt.get();
        }
    }
    //    public void updateAll(Long id1, Long id2){
//        Iterable<Invitation> all = this.findAll();
//        Iterator<Invitation> iterator = all.iterator();
//        while(iterator.hasNext()){
//            Invitation i = iterator.next();
//            if(i.getFrom().equals(id2)){
//                updateInvitation(i.getId(), "approved");
//            }
//        }
//    }
    public Iterable<Invitation> findAll(){
        return repo_invitations.findAll();
    }
}
