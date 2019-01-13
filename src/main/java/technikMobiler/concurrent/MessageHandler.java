package technikMobiler.concurrent;

import technikMobiler.controller.MessageController;
import technikMobiler.controller.SenderController;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class MessageHandler implements Runnable {


    private MessageController messageController;
    private Queue<String> messageQueue;

    public MessageHandler(SenderController messageController, BlockingQueue<String> blockingQueue) {
        this.messageController = messageController;
        this.messageQueue = blockingQueue;
    }



    @Override
    public void run() {

    }
}
