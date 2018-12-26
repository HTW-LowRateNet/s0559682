package technikMobiler.concurrent;

import technikMobiler.bean.Message;
import technikMobiler.module.MessageDBController;

import java.util.Date;
import java.util.Map;

public class CheckingMessageDB implements Runnable {

    MessageDBController dbController = MessageDBController.getSingleInstance();
    @Override
    public void run() {
        while (true) {
            try {
                for(Map.Entry<String, Message> entry : dbController.getMessageDB().entrySet()) {
                    long current = new Date().getTime();
                    if(current - entry.getValue().getArrivalTimeStamp() > 300000) {
                        this.dbController.removeMessage(entry.getValue());
                        System.out.println("Message: " + entry.getValue().getCode() + " to old, gets deleted");
                    }
                }
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
