package technikMobiler.controller;

import technikMobiler.bean.Message;
import technikMobiler.config.AddressHelper;

public class MessageController {

    public static boolean requestedAnAddress = false;
    private SenderController senderController;
    public static volatile int countForNewAddressrequest = 0;


    public MessageController(SenderController senderController) {
        this.senderController = senderController;
    }

    public void handleMessage(String data) {
        System.out.println("Counter für neue Adresseanfrage: " + countForNewAddressrequest);
        System.out.println("Bin ich Koordinator?: " + senderController.getMultihopBean().isCoordinator());
        countForNewAddressrequest = countForNewAddressrequest + 1;
        if(countForNewAddressrequest > 10 && senderController.getMultihopBean().getPermanentAddress() == null) {
            requestedAnAddress = false;
            countForNewAddressrequest = 0;
        }
            System.out.println("Daten im MessageController sind: " + data);
            if(data == null) {
                System.out.println("data was null");
            } else {
                if (data.contains("AT")){
                    this.handleATMessage(data);
                } else {
                    try {
                        Message message = converToMessage(data);
                        System.out.println("Die reingekommenen Daten in message umgewandelt: " + message.toString());
                        System.out.println("Eigene permanente Adresse: " + senderController.getMultihopBean().getPermanentAddress());
                        if (message.getCode().equals("ALIV")){
                            System.out.println("IM ALIVE BLOCK");
                            this.handleALIVRequest(message);
                        } else if(message.getCode().equals("NRST")) {
                            System.out.println("IM NRST BLOCK");
                        }else if(!messageIsForMe(message)) {
                            this.handleMessageIsNotForMe(message);
                        } else {
                            this.handleIfMessageIsForMe(message);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        System.out.println(e.getCause());
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                        System.out.println("wrong data: " + data);
                    }
                }
            }

    }

    private void handleIfMessageIsForMe(Message message) {
        System.out.println("Im restlichen Block");
        if (message.getCode().equals("CDIS") && senderController.getMultihopBean().isCoordinator()){
            System.out.println("Im CDIS BLOCK");
            this.handleCDISRequest(message);
        }

        if(message.getCode().equals("MSSG")) {
            System.out.println("IM MSSG BLOCK");
            handleMSSGRequest(message);
        }

        if(message.getCode().equals("ADDR")) {
            System.out.println("IM ADDR BLOCK");
            this.handleADDRRequest(message);
        }

        if(message.getCode().equals("AACK")) {
            System.out.println("IM AACK BLOCK");
            this.handleAACKRequest(message);
        }
    }

    private void handleATMessage(String data) {
        if(data.contains("AT,OK")) {
            SenderController.preparedToSend = true;
            synchronized (NetworkController.lock1) {
                NetworkController.lock1.notify();
            }
        } if(data.contains("AT,SENDED")) {
            System.out.println("AT SENDED Block  " + data);
        }
    }

    private void handleMessageIsNotForMe(Message message) {
        if(senderController.getMultihopBean().getPermanentAddress() != null) {
            System.out.println("Im forwarding block");
            this.handleForwardingProcess(message);
        } else {
            System.out.println("not allowed to forward");
        }
    }

    private Message converToMessage(String data) {
        String[] temp = data.split(",");
        String[] newMessage = new String[7];
        newMessage[0] = temp[3];
        newMessage[1] = temp[4];
        newMessage[2] = temp[5];
        newMessage[3] = temp[6];
        newMessage[4] = temp[7];
        newMessage[5] = temp[8];
        newMessage[6] = temp[9];
        Message message = new Message(newMessage);
        return message;
    }

    private void handleNRSTRequest() {
        requestedAnAddress = false;
        this.resetNetwork();
    }

    private void handleAACKRequest(Message message) {
       this.senderController.getMultihopBean().handleAACKRequestAsCoordinator(message);
    }

    private void handleCDISRequest(Message message) {
        System.out.println(message.getSender() + ": joined");
        senderController.sendCoordinatorIsStillAlive();
    }

    private void handleALIVRequest(Message message) {
        try {
            if(senderController.getMultihopBean().isCoordinator()) {
                this.resetNetwork();
                senderController.sendNetworkReset();

            } else {
                senderController.setCoordinatorPresent(true);
                System.out.println("I am a worker");
                Thread.sleep(3000);
                //handleForwardingProcess(message);
                if(!requestedAnAddress) {
                    senderController.requestPermanentAddress();
                    requestedAnAddress = true;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private void handleMSSGRequest(Message message) {
        System.out.println("Message: " + message.getPayload());
    }

    private void handleADDRRequest(Message message) {
        if(message.getSender().equals("0000") && this.senderController.getMultihopBean().getPermanentAddress() == null && message.getTargetAddress().equals(senderController.getMultihopBean().getTempAddress())) {
            String tempAddr = this.senderController.getMultihopBean().getTempAddress();
            this.senderController.setPermanentAddress(message.getPayload());
            this.senderController.sendAddressAcknowledgment();
            System.out.println("SET OWN PERMANENT ADDR " + message.getPayload());
        } else {
            System.out.println("Since I am coordinator I will send a new address");
            String tempAddressFromSomeNode = message.getSender();
            Integer addr = AddressHelper.createPermanentAddress();
            senderController.sendPermanentAddress(addr, tempAddressFromSomeNode);
            Integer addressForDB = Integer.valueOf(addr);
            this.senderController.getMultihopBean().handleADDRRequestAsCoordinator(addressForDB, Integer.valueOf(tempAddressFromSomeNode));
            System.out.println(senderController.getMultihopBean().getPermanentAddress() + " (permanent");
            System.out.println(senderController.getMultihopBean().getTempAddress() + " (tempAddress)");
            System.out.println(message.getSender() + " sender aus message");
            System.out.println(message.getTargetAddress() + " target aus message");
            System.out.println(message.toString());
        }
    }

    private boolean messageIsForMe(Message message) {
        if(senderController.getMultihopBean().getTempAddress() != null) {
            return message.getTargetAddress().equals(this.senderController.getMultihopBean().getTempAddress());
        } else {
            return message.getTargetAddress().equals(this.senderController.getMultihopBean().getPermanentAddress());
        }
    }

    private void handleForwardingProcess(Message message) {
        senderController.getMultihopBean().printMessageDB();
        if(senderController.getMultihopBean().checkIfMessageIsTheSame(message)) {
            System.out.println("Message: " + message.getMessageId() + " already in DB. Not being forwarded");
        } else if(canMessageBeForwarded(message)) {
            senderController.getMultihopBean().safeMessageIntoDB(message);
            senderController.forwardingMessage(message);
            System.out.println(message.getCode() + " Request with id: " + message.getMessageId() + " going to be forwarded");
        } else {
            System.out.println(message.getCode() + " Request with id: " + message.getMessageId() + " not being forwardet");
        }
    }

    private void resetNetwork() {
        this.senderController.getMultihopBean().resetBean();
    }

    private boolean canMessageBeForwarded(Message message) {
        int ttl = message.getTimeToLive();
        int currentHop = message.getCurrentHops();
        boolean messageCanBeSend = false;
        if(currentHop < ttl) {
            message.setCurrentHops(currentHop + 1);
            messageCanBeSend = true;
        } else {
            messageCanBeSend = false;
        }
        return messageCanBeSend;
    }
}
