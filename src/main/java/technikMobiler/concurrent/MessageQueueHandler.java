package technikMobiler.concurrent;

import technikMobiler.controller.MessageController;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

public class MessageQueueHandler implements Runnable {


    private MessageController messageController;
    private BlockingQueue<String> messageQueue;

    public MessageQueueHandler(MessageController messageController, BlockingQueue<String> blockingQueue) {
        this.messageController = messageController;
        this.messageQueue = blockingQueue;
    }


    @Override
    public void run() {
        while (true) {
            synchronized (messageController) {
                String message = messageQueue.poll();
                if(message != null) {
                    System.out.println("Warteschlangenlaenge: " + messageQueue.size());
                    System.out.println("Genommene Nachricht: " + message);
                    messageController.handleMessage(message);
                }
            }
        }
    }
}
