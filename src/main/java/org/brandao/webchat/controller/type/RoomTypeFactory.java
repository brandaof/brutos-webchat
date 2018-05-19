package org.brandao.webchat.controller.type;

import org.brandao.brutos.annotation.TypeDef;
import org.brandao.brutos.type.Type;
import org.brandao.brutos.type.TypeFactory;
import org.brandao.webchat.model.Room;

/**
 *
 * @author Brandao
 */
@TypeDef
public class RoomTypeFactory 
    implements TypeFactory{

    @Override
    public Type getInstance() {
        return new RoomType();
    }

    @Override
    public Class getClassType() {
        return Room.class;
    }

    @Override
    public boolean matches(Class type) {
        return Room.class == type;
    }
    
}
