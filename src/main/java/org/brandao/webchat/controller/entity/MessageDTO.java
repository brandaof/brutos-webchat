package org.brandao.webchat.controller.entity;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.brandao.brutos.annotation.*;
import org.brandao.webchat.model.Message;
import org.brandao.webchat.model.Room;
import org.brandao.webchat.model.User;
import org.brandao.webchat.model.UserNotFoundException;

public class MessageDTO implements Serializable{

	private static final long serialVersionUID = 9113414670176819186L;

	private UserDTO origin;
    
    @NotNull
    private UserDTO dest;

    @NotNull
    @Pattern(regexp="[^\\<\\>]{1,200}")
    private String message;

    @Transient
    private boolean forMe;
    
    @Constructor
    public MessageDTO(){
    }
    
    public MessageDTO(Message msg){
        this.origin  = 
            msg.getOrigin() == null? 
                null : 
                new UserDTO(msg.getOrigin());
        this.dest    = new UserDTO(msg.getDest());
        this.message = msg.getMessage();
    }
    
    public UserDTO getOrigin() {
        return origin;
    }

    public void setOrigin(UserDTO origin) {
        this.origin = origin;
    }

    public UserDTO getDest() {
        return dest;
    }

    public void setDest(UserDTO dest) {
        this.dest = dest;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public Message rebuild(Room roomService, User currentUser) 
            throws UserNotFoundException{
        
        Message obj = new Message();
        
        obj.setDest(this.dest.rebuild(roomService));
        
        obj.setOrigin(
                currentUser == null?
                (this.origin != null? 
                    this.origin.rebuild(roomService)
                    : null) :
                currentUser
        );
        obj.setMessage(this.message);
        return obj;
    }
    
    public Message rebuild(Room roomService) throws UserNotFoundException{
        return rebuild(roomService,null);
    }

    public boolean isForMe() {
        return forMe;
    }

    public void setForMe(boolean forMe) {
        this.forMe = forMe;
    }
}
