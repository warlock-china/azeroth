package cn.com.warlock.test;

import java.io.Serializable;

import cn.com.warlock.kafka.handler.MessageHandler;
import cn.com.warlock.kafka.message.DefaultMessage;

public class DemoMessageHandler implements MessageHandler {

    @Override
    public void p1Process(DefaultMessage message) {
        //TODO 
    }

    @Override
    public void p2Process(DefaultMessage message) {
        Serializable body = message.getBody();
        System.out.println("DemoMessageHandler process message:" + body);
        try {
            Thread.sleep(100);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onProcessError(DefaultMessage message) {
        System.out.println("ignore error message : " + message);
        return true;
    }

}
