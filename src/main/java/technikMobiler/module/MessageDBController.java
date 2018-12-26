package technikMobiler.module;

import technikMobiler.bean.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MessageDBController {

    private static MessageDBController singleInstance = null;

    private MessageDB messageDB;

    private MessageDBController() {
        messageDB = new MessageDB();
    }

    public static MessageDBController getSingleInstance() {
        if(singleInstance == null) {
            singleInstance = new MessageDBController();
        }
        return singleInstance;
    }

    public void addMessage(Message message) {
        this.messageDB.addData(message.getMessageId(), message);
    }

    public boolean checkMessageIfItsTheSame(Message message) {
        String messageId = message.getMessageId();
        int currentHop = message.getCurrentHops();
        if(this.messageDB.getData(messageId) != null) {
            Message messageFromDB = this.messageDB.getData(messageId);
            String messageIDFromDB = messageFromDB.getMessageId();
            int hopMessageFromDB = messageFromDB.getCurrentHops();
            boolean ids = messageId.equals(messageIDFromDB);
            boolean hops = currentHop == hopMessageFromDB;
            return ids && hops;
        } else {
            return false;
        }

    }

    public void printDB() {
        for(Map.Entry<String, Message> entry : this.messageDB.getDB().entrySet()) {
            System.out.println("MessageDB: key: " + entry.getKey() + " value:" + entry.getValue().toString());
        }
    }

    public ConcurrentHashMap<String, Message> getMessageDB() {
        return this.messageDB.getDB();
    }



    public void removeMessage(Message message) {
        this.messageDB.deleteData(message.getMessageId());
    }

    public void deleteAllMessages() {
        this.messageDB.clearDB();
    }


}
