package com.manniu.screen.websocket;

import cn.hutool.json.JSONUtil;
import com.jinfu.lock.core.LockTemplate;
import com.jinfu.lock.pojo.Params;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ServerEndpoint 通过这个 spring boot 就可以知道你暴露出去的 ws
 * 应用的路径，有点类似我们经常用的@RequestMapping。比如你的启动端口是8080，而这个注解的值是ws，那我们就可以通过 ws://127.0.0.1:8080/ws 来连接你的应用
 * @OnOpen 当 websocket 建立连接成功后会触发这个注解修饰的方法，注意它有一个 Session 参数
 * @OnClose 当 websocket 建立的连接断开后会触发这个注解修饰的方法，注意它有一个 Session 参数
 * @OnMessage 当客户端发送消息到服务端时，会触发这个注解修改的方法，它有一个 String 入参表明客户端传入的值
 * @OnError 当 websocket 建立连接时出现异常会触发这个注解修饰的方法，注意它有一个 Session 参数
 */
@ServerEndpoint("/locksocket/{id}")
@Component
@Slf4j
public class WebSocketServer {

    @Autowired
    LockTemplate lockTemplate;
    public static LockTemplate template;

    // 项目启动时把fingerprintTemplate交给静态的template
    @PostConstruct
    public void init() {
        template = this.lockTemplate;
    }

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的WebSocketController对象。
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 接收userId
     */
    private String id;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        this.session = session;
        this.id = id;
        //如果已经包含该用户id，则移除后重新加入
        if (webSocketMap.containsKey(id)) {
            webSocketMap.remove(id);
            webSocketMap.put(id, this);
        } else {
            //否则直接加入
            webSocketMap.put(id, this);
        }
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(id)) {
            webSocketMap.remove(id);
            log.info(">>> 用户：{}已关闭连接", id);
        } else {
            log.info(">>> 连接已关闭...");
        }
    }


    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //接收传过来的消息
        Params params = JSONUtil.toBean(message, Params.class);
        sendMessage(JSONUtil.toJsonStr(lockTemplate.getWebsocket(params)));
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error(">>> WebSocket出现未知错误: ");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error(">>> WebSocket消息发送出现错误: ");
            e.printStackTrace();
        }
    }
}