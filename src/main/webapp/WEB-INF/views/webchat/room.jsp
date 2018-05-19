<%@page session="false" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>

	<head>
        <script type="text/javascript" src="${pageContext.servletContext.contextPath}/resources/js/jquery-1.4.4.min.js"></script>
        <script type="text/javascript">
            
            function init(){
                reloadUsers();
                readMessage();
            }
            
            function reloadUsers(){
                $.ajax({
                    type: "GET",
                    url: "${pageContext.servletContext.contextPath}/room/${roomID}/listUsers",
                    cache: false,
                    success: function(users){
                        form.reloadUsers(users);
                    },
                    error: function(){
                        alert("erro!");
                    }
                });
            }
            
            function readMessage(){
                $.ajax({
                    type: "GET",
                    url: "${pageContext.servletContext.contextPath}/room/${roomID}/message",
                    cache: false,
                    success: function(msg){
                        readMessage();
                        
                        if(msg == null)
                            return;
                        
                        var origin = msg.origin;
                        
                        if(origin == null)
                            reloadUsers();
                        
                        text.addMessage(msg)
                        
                    }
                });
                
            };
            
            function sendMessage(){
                $.ajax({
                    type: "POST",
                    url: "${pageContext.servletContext.contextPath}/room/${roomID}/send",
                    cache: false,
                    data: form.getMessage()
                });
                return false;
            };
            
            function exit(){
                $.ajax({
                    type: "POST",
                    url: "${pageContext.servletContext.contextPath}/room/${roomID}/exit",
                    cache: false,
                    success: function(){
                        location.href = "${pageContext.servletContext.contextPath}/${param.roomID}";
                    },
                    error: function(){
                        location.href = "${pageContext.servletContext.contextPath}/${param.roomID}";
                    }
                });
            }
        </script>
    </head>

    <frameset onload="javascript:init()" rows=*,100>
        <frame name="text" src="${pageContext.servletContext.contextPath}/room/${roomID}/messagePart"></frame>
        <frame name="form" src="${pageContext.servletContext.contextPath}/room/${roomID}/sendPart"></frame>
    </frameset>
</html>
