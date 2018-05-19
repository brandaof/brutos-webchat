package org.brandao.webchat.controller.type;

import org.brandao.brutos.ApplicationContext;
import org.brandao.brutos.Invoker;
import org.brandao.brutos.type.StringType;
import org.brandao.webchat.model.Room;
import org.brandao.webchat.model.Rooms;

/**
 *
 * @author Brandao
 */
public class RoomType 
	extends StringType{
    
    @Override
    public Object convert(Object value){
        
        String roomID = (String) super.convert(value);
        
        ApplicationContext context = 
            Invoker.getCurrentApplicationContext();
        
        Rooms roomServiceFactory = 
                (Rooms) context.getBean(Rooms.class);
        
        return roomServiceFactory.getRoom(roomID);
    }
    
    @Override
    public Class<?> getClassType(){
        return Room.class;
    }
    
}
