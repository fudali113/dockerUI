package ren.doob.websocket;

/**
 * @author fudali
 * @package ren.doob.websocket
 * @class MyHandler
 * @date 2016-2-4
 * <p>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓    Code
 * 　　┏┛┻━━━┛┻┓  is
 * 　　┃　　　　　　　┃  far
 * 　　┃　　　━　　　┃  away
 * 　　┃　┳┛　┗┳　┃  from
 * 　　┃　　　　　　　┃  bug
 * 　　┃　　　┻　　　┃  with
 * 　　┃　　　　　　　┃  the
 * 　　┗━┓　　　┏━┛  animal
 * 　　　　┃　　　┃      protecting
 * 　　　　┃　　　┃神兽保佑,代码无bug
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p>
 * ━━━━━━感觉萌萌哒━━━━━━
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ren.doob.common.CommonField;
import ren.doob.common.Mc;

import java.io.IOException;
import java.util.ArrayList;

public class MyHandler extends TextWebSocketHandler {

    private MyHandler(){}
    private static final MyHandler myHandler = new MyHandler();
    public static MyHandler getMyHandler(){
        return myHandler;
    }

    public static final String WEBSOCKET = "myWebSocket";

    private static final Log logger;

    private static final ArrayList<WebSocketSession> users;

    static {
        users = new ArrayList();
        logger = LogFactory.getLog(MyHandler.class);
    }

    /*@Autowired
    private WebSocketService webSocketService;*/

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        logger.debug("connect to the websocket success......");
        Mc.getSes().setAttribute(WEBSOCKET , session);
        users.add(session);
        String userName = (String) session.getAttributes().get(CommonField.NOWONLINE_USERNAME);
        if(userName!= null){
            //查询未读消息
            //int count = webSocketService.getUnReadNews((String) session.getAttributes().get("websocket_username"));

            //session.sendMessage(new TextMessage(count + ""));
        }
    }


    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

        //sendMessageToUsers();
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        if(session.isOpen()){
            session.close();
        }
        logger.debug("websocket connection closed......");
        users.remove(session);
    }


    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.debug("websocket connection closed......");
        users.remove(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

    /**
     * 给所有在线用户发送消息
     *
     * @param message
     */
    public void sendMessageToUsers(TextMessage message) {
        for (WebSocketSession user : users) {
            try {
                if (user.isOpen()) {
                    user.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 给某个用户发送消息
     *
     * @param userName
     * @param message
     */
    public void sendMessageToUser(String userName, TextMessage message) {

        for (WebSocketSession user : users) {
            if (user.getAttributes().get(CommonField.NOWONLINE_USERNAME).equals(userName)) {
                try {
                    if (user.isOpen()) {
                        user.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public void sendMessage(String message) {

        WebSocketSession webSocketSession = (WebSocketSession)Mc.getSes().getAttribute(WEBSOCKET);
        if(webSocketSession == null) return ;
        if(webSocketSession.isOpen()) {
            try {
                webSocketSession.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
