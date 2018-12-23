package technikMobiler.bean;

public class Message {
    private String code;
    private String payload;
    private String messageId;
    private int timeToLive;
    private int currentHops = 0;
    private String sender;
    private String targetAddress;


    public Message(String[] message) {
        this.code = message[0];
        this.messageId = message[1];
        this.timeToLive = Integer.parseInt(message[2]);
        this.currentHops = Integer.parseInt(message[3]);
        this.sender = message[4];
        this.targetAddress = message[5];
        this.payload = message[6];


    }
    public Message(MessageCode mCode, String messageId, String sender, String targetAddress, String payload) {
        this.code = mCode.code;
        this.messageId = messageId;
        this.timeToLive = 3;
        this.currentHops = 0;
        this.payload = payload;
        this.sender = sender;
        this.targetAddress = targetAddress;
    }

    @Override
    public String toString() {
        return code +","+ messageId +","+ timeToLive +","+ currentHops +"," + sender + ","+ targetAddress + "," + payload + ",";
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    public int getCurrentHops() {
        return currentHops;
    }

    public void setCurrentHops(int currentHops) {
        this.currentHops = currentHops;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }
}
