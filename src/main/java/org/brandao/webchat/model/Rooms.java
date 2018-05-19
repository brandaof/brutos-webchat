package org.brandao.webchat.model;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Singleton;

@Singleton
public class Rooms {
    
    private Map<String,Room> rooms;
    
    public Rooms(){
        this.rooms = new HashMap<String,Room>();
        this.rooms.put("room1", new Room("room1"));
        this.rooms.put("room2", new Room("room2"));
        this.rooms.put("room3", new Room("room3"));
    }
    
    public Room getRoom(String id){
    	return rooms.get(id);
    }
    
}
