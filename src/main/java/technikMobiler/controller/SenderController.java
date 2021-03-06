package technikMobiler.controller;

import com.pi4j.io.serial.Serial;
import technikMobiler.bean.Message;
import technikMobiler.bean.MultihopBean;
import technikMobiler.bean.MessageCode;
import technikMobiler.config.AddressHelper;
import technikMobiler.module.AddressDBController;

import java.io.IOException;
import java.util.Random;

public class SenderController {

    private MultihopBean multihopBean;
    private Serial serial;
    public static Boolean preparedToSend = false;
    private boolean tempAddress = false;
    private volatile boolean coordinatorPresent = false;

    public SenderController(Serial serial) {
        this.serial = serial;
        this.multihopBean = new MultihopBean();
    }

    protected void setPermanentAddress(String addr) {
        String address;
        synchronized (NetworkController.lock1) {
            address = addr;
            System.out.println("Set own permanent address: " + address);
            this.sendATCommand("AT+ADDR=" + address);
            this.multihopBean.setPermanentAddress(address);
            this.multihopBean.setCoordinator(true);
            if(this.multihopBean.getTempAddress() != null) {
                this.multihopBean.setTempAddress(null);
            }
        }
    }

    public void setCoordinator() {
        this.setPermanentAddress("0000");
    }


    public synchronized void findCoordinator() {
        try {
            System.out.println("Is there a coordinator asked by Manuel?");
            Message coordinatorDiscoveryMessage;
            if(multihopBean.getTempAddress() != null) {
                coordinatorDiscoveryMessage = new Message(MessageCode.CDIS, generateMessageID(), this.multihopBean.getTempAddress(), "FFFF", "Is there a coordinator asked by Manuel");
            } else {
                coordinatorDiscoveryMessage = new Message(MessageCode.CDIS, generateMessageID(), this.multihopBean.getPermanentAddress(), "FFFF", "Is there a coordinator asked by Manuel");
            }
            System.out.println("discoveryCoordinator message " + coordinatorDiscoveryMessage);
            Thread.sleep(2000);
            sendMessageHelper(coordinatorDiscoveryMessage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public synchronized void sendCoordinatorIsStillAlive() {
        try {
            Message imTheCaptainMessage = new Message(MessageCode.ALIV, generateMessageID(), this.multihopBean.getPermanentAddress(),"FFFF" , "I am the coordinator send by Manuel");
            Thread.sleep(5000);
            sendMessageHelper(imTheCaptainMessage);
        } catch (Exception e) {
            System.err.println(e.getCause());
        }
    }

    public void sendAddressAcknowledgment() {
        try {
            Message ackMessage = new Message(MessageCode.AACK, generateMessageID(), this.multihopBean.getPermanentAddress(),"FFFF", null);
//            Thread.sleep(3000);
            sendMessageHelper(ackMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendPermanentAddress(Integer address, String tempAddressFromNode) {
        try {
            String hexAddress = Integer.toHexString(address);
            hexAddress = AddressHelper.convertToPaddedHex(hexAddress);
            System.out.println("New Address is: " + hexAddress);
            Message messageWithPermanentAddres = new Message(MessageCode.ADDR, generateMessageID(), this.multihopBean.getPermanentAddress(), tempAddressFromNode, hexAddress);
//            Thread.sleep(2000);
            sendMessageHelper(messageWithPermanentAddres);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void sendNetworkReset() {
        try {
            Message netWorkReset = new Message(MessageCode.NRST, generateMessageID(), this.multihopBean.getTempAddress(), "FFFF", null);
//            Thread.sleep(3000);
            this.sendMessageHelper(netWorkReset);
            System.out.println("Network has to be resetted");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void requestPermanentAddress() {
        try {
            Message giveMePermanentAddress = new Message(MessageCode.ADDR, generateMessageID(), this.multihopBean.getTempAddress(), "FFFF", null);
//            Thread.sleep(3000);
            this.sendMessageHelper(giveMePermanentAddress);
            System.out.println("Requesting a permanent Address for myself from Coordiantor");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void forwardingMessage(Message message) {
        this.sendMessageHelper(message);
    }


    private void sendMessageHelper(Message message) {
        try {
            int messageLength = message.toString().length();
            System.out.println("AT+SEND=" + messageLength);
            System.out.println("Nachricht: "+message.toString());
//            serial.write("AT+DESC=FFFF");
//            serial.write('\r');
//            serial.write('\n');
            serial.write("AT+SEND=" + messageLength);
            serial.write('\r');
            serial.write('\n');
            serial.flush();
            // logging to console

            //Waiting for preparedToSend to be true
            while (SenderController.preparedToSend == false) {
                try {
                    synchronized (NetworkController.lock1) {
                        NetworkController.lock1.wait();
                    }
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            serial.write(message.toString());
            SenderController.preparedToSend = false;

        } catch (IllegalStateException | IOException e) {
            System.out.println("Something went wrong when preparing to send the message:");
            e.printStackTrace();
        }

    }

    public void sendATCommand(String command) {
        try {
            serial.write(command);
            serial.write('\r');
            serial.write('\n');

            // logging to console
            System.out.println("Sent Command: " + command);
        } catch (IllegalStateException | IOException e) {
            System.out.println("Error sending AT command: " + command);
            e.printStackTrace();
        }
    }

    private String generateMessageID () {
        Random random = new Random();
        Integer number = random.nextInt(8999)+1000;
        return number.toString();
    }


    protected void configureModule() throws InterruptedException {
        synchronized (NetworkController.lock1) {
            this.sendATCommand("AT+CFG=433000000,20,9,10,1,1,0,0,0,0,3000,8,4");
            NetworkController.lock1.wait();
        }
        this.setTemporaryAddress();
    }

    protected void setTemporaryAddress() {
        String address;
        synchronized (NetworkController.lock1) {
            address = AddressHelper.createTemporaryNodeAddress();
            System.out.println("Set own temporary address: " + address);
            this.multihopBean.setTempAddress(address);
        }
    }

    public boolean isTempAddress() {
        return tempAddress;
    }

    public void setTempAddress(boolean tempAddress) {
        this.tempAddress = tempAddress;
    }

    public MultihopBean getMultihopBean() {
        return multihopBean;
    }

    public void setMultihopBean(MultihopBean multihopBean) {
        this.multihopBean = multihopBean;
    }

    public synchronized boolean isCoordinatorPresent() {
        return coordinatorPresent;
    }

    public void setCoordinatorPresent(boolean coordinatorPresent) {
        this.coordinatorPresent = coordinatorPresent;
    }
}
