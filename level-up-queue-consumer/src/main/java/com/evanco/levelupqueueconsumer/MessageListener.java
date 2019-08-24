package com.evanco.levelupqueueconsumer;

import com.evanco.levelupqueueconsumer.util.feign.LevelUpClient;
import com.evanco.levelupqueueconsumer.util.messages.LevelUp;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageListener {

    @Autowired
    private LevelUpClient client;

    @RabbitListener(queues = LevelUpQueueConsumerApplication.QUEUE_NAME)
    public void receiveMessage(LevelUp msg) {

        // Let the presence of a valid ID determine create or update
        System.out.println(msg.toString());
        if(msg.getLevelUpId() != null){
            client.updateLevelUp(msg.getLevelUpId(), msg);
            System.out.println("=== Update LevelUp ===");
        }else {
            client.addLevelUp(msg);
            System.out.println("=== Create new LevelUp ===");
        }
    }
}
