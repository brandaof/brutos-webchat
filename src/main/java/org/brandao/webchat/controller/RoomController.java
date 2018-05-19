package org.brandao.webchat.controller;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.validation.Valid;

import org.brandao.brutos.annotation.*;
import org.brandao.brutos.annotation.web.MediaTypes;
import org.brandao.brutos.annotation.web.RequestMethod;
import org.brandao.brutos.annotation.web.ResponseStatus;
import org.brandao.brutos.web.HttpStatus;
import org.brandao.brutos.web.RequestMethodTypes;
import org.brandao.brutos.web.WebFlowController;
import org.brandao.webchat.controller.entity.MessageDTO;
import org.brandao.webchat.controller.entity.UserDTO;
import org.brandao.webchat.model.*;

@RequestScoped
@Controller("/room/{roomID:[a-z0-9]{1,10}}")
@View(value = "webchat/room")
@Actions({
    @Action(value="/messagePart", view=@View(value = "webchat/messages")),
    @Action(value="/sendPart",    view=@View(value = "webchat/send")),
    @Action(value="/login",       view=@View(value = "webchat/login"))
})
public class RoomController {

    private User sessionUser;
    
    private Room room;
    
    @Action("/send")
    @RequestMethod(RequestMethodTypes.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @AcceptRequestType(MediaTypes.)
    @ResponseType(MediaTypes.APPLICATION_JSON)
    public void sendMessage(
    		@Valid
    		@Basic(bean="message")
            MessageDTO message) throws UserNotFoundException{
    	
    	room.sendMessage(
            message.rebuild(
            		room, 
            		sessionUser
            )
        );
        
    }
    
    @Action("/enter")
    @RequestMethod(RequestMethodTypes.POST)
    @View("webchat/login")
    public void putUser(
    		@Valid
            @Basic(bean="user")
            UserDTO userDTO) 
            throws UserExistException, MaxUsersException{

        if(userDTO == null)
            throw new NullPointerException();
        
        if(sessionUser != null){
        	sessionUser.exitRoom();
        }
        
        User user = userDTO.rebuild();
        room.putUser(user);
        
        this.sessionUser = user;
        
        WebFlowController.redirectTo("/room/" + room.getId());
    }
    
    @Action("/exit")
    public void removeUser() throws UserNotFoundException{
    	
        if(sessionUser != null){
        	sessionUser.exitRoom();
        }
        
        WebFlowController.redirectTo("/");
        
    }
    
    @Action("/message")
    @ResponseType(MediaTypes.APPLICATION_JSON)
    public MessageDTO readMessage() 
            throws UserNotFoundException, InterruptedException{
        
        Message msg = room.getMessage(sessionUser);
        
        if( msg != null){
            MessageDTO msgDTO = new MessageDTO(msg);
            msgDTO.setForMe(
                msg.getDest().equals(this.sessionUser));
            return msgDTO;
        }
        else
            return null;
    }
    
    @Action("/listUsers")
    @ResponseType(MediaTypes.APPLICATION_JSON)
    public ArrayList<UserDTO> readUsers(){
        List<User> users = room.getUsers();
        ArrayList<UserDTO> usersDTO = new ArrayList<UserDTO>();
        
        for(User user: users)
            usersDTO.add(new UserDTO(user));
        
        return usersDTO;
    }

    @Basic(bean="roomID")
    public void setRoom(Room room){
    	this.room = room;
    }

    public Room getRoom(){
    	return this.room;
    }
    
    @Basic(bean="sessionUser", mappingType=MappingTypes.VALUE, scope=ScopeType.SESSION)
    public void setSessionUser(User currentUser){
    	this.sessionUser = currentUser;
    }

    public User getSessionUser(){
    	return this.sessionUser;
    }
    
}
