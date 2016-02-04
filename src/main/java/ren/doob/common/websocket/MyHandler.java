package ren.doob.common.websocket;

/**
 * @author fudali
 * @package ren.doob.common.websocket
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

import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyHandler extends TextWebSocketHandler {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // ...
    }

}
