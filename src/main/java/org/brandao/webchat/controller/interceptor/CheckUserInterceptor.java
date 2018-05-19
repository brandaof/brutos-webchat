package org.brandao.webchat.controller.interceptor;

import java.util.HashSet;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;

import org.brandao.brutos.ResourceAction;
import org.brandao.brutos.annotation.Intercepts;
import org.brandao.brutos.interceptor.AbstractInterceptor;
import org.brandao.brutos.interceptor.InterceptedException;
import org.brandao.brutos.interceptor.InterceptorHandler;
import org.brandao.brutos.interceptor.InterceptorStack;
import org.brandao.brutos.mapping.Action;
import org.brandao.brutos.scope.Scope;
import org.brandao.brutos.web.RequestMethodTypes;
import org.brandao.brutos.web.WebFlowController;
import org.brandao.brutos.web.WebScopeType;
import org.brandao.webchat.controller.RoomController;
import org.brandao.webchat.model.User;

@ApplicationScoped
@Intercepts
public class CheckUserInterceptor extends AbstractInterceptor{

	private Set<String> notAccept;
	
    public CheckUserInterceptor(){
    	this.notAccept = new HashSet<String>();
    	this.notAccept.add("/login");
    	this.notAccept.add("/enter");
    	this.notAccept.add("/exit");
    }
    
    @Override
    public void intercepted(InterceptorStack is, InterceptorHandler ih) 
            throws InterceptedException {
        
        Scope scope = ih.getContext().getScopes().get(WebScopeType.SESSION);
        User user = (User) scope.get("sessionUser");
        
        if( user != null && user.getRoom() != null )
            is.next(ih);
        else{
            WebFlowController.execute(RoomController.class, RequestMethodTypes.GET, "/login");
        }
    }
    
    @Override
    public boolean accept(InterceptorHandler handler) {
        Object controllerInstance = handler.getResource();
        
        if(controllerInstance instanceof RoomController){
        	ResourceAction ra = handler.getResourceAction();
        	Action action = ra.getMethodForm();
            return action == null || !this.notAccept.contains(action.getName());
        }
        else
            return false;
    }
    
}
