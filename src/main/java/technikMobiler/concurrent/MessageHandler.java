package technikMobiler.concurrent;

import technikMobiler.controller.MessageController;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class MessageHandler implements Runnable {


    private MessageController messageController;
    private Queue<String> messageQueue;

    public MessageHandler(MessageController messageController, BlockingQueue<String> blockingQueue) {
        this.messageController = messageController;
        this.messageQueue = blockingQueue;
    }



    @Override
    public void run() {
        while (true) {
            synchronized (messageController) {
                String message = messageQueue.poll();
                if(message != null) {
                    messageController.handleMessage(message);
                }
            }
        }
    }
}
