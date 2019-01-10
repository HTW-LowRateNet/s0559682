package technikMobiler.bean;

import technikMobiler.module.AcknowledgmentDBController;
import technikMobiler.module.AddressDBController;
import technikMobiler.module.MessageDBController;

import java.util.Date;

public class MultihopBean {

    private boolean isCoordinator;
    private String tempAddress;
    private String permanentAddress;
    private AcknowledgmentDBController acknowledgmentDBController = AcknowledgmentDBController.getSingleInstance();
    private AddressDBController addressDBController = AddressDBController.getSingleInstance();
    private MessageDBController messageDBController = MessageDBController.getSingleInstance();


    public MultihopBean() {

    }
    public MultihopBean(boolean isCoordinator, String tempAddress, String permanentAddress) {
        this.isCoordinator = isCoordinator;
        this.tempAddress = tempAddress;
        this.permanentAddress = permanentAddress;
    }

    public MultihopBean(boolean isCoordinator, String tempAddress) {
        this.isCoordinator = isCoordinator;
        this.tempAddress = tempAddress;
    }


    public void handleAACKRequestAsCoordinator(Message message) {
        try {
            Integer permanentAddress = Integer.parseInt(message.getSender(), 16);
            System.out.println("die permanente Addresse im AACK Req: "  + permanentAddress);
            boolean success = this.acknowledgmentDBController.checkAddress(permanentAddress);
            if(success) {
                this.acknowledgmentDBController.removeAddress(permanentAddress);
                this.addressDBController.addAddress(permanentAddress);
                System.out.println("Permanent address: " + permanentAddress + " saved");
            } else {
                System.out.println(permanentAddress + " couldn't be saved since it never got created");
            }

            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void handleADDRRequestAsCoordinator(Integer permADDR, Integer tempAddr) {
        this.acknowledgmentDBController.addAddressPair(permADDR, tempAddr);
        this.acknowledgmentDBController.printAddresses();
    }

    public boolean checkIfMessageIsTheSame(Message message) {
        return this.messageDBController.checkMessageIfItsTheSame(message);
    }

    public void safeMessageIntoDB(Message message) {
        Date date = new Date();
        message.setArrivalTimeStamp(date.getTime());
        this.messageDBController.addMessage(message);
    }


    public void printMessageDB() {
        this.messageDBController.printDB();
    }

    public boolean isCoordinator() {
        return isCoordinator;
    }

    public void setCoordinator(boolean coordinator) {
        isCoordinator = coordinator;
    }

    public String getTempAddress() {
        return tempAddress;
    }

    public void setTempAddress(String tempAddress) {
        this.tempAddress = tempAddress;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public void resetBean() {
        this.isCoordinator = false;
        this.tempAddress = "0099";
        this.permanentAddress = null;
        this.messageDBController.deleteAllMessages();
        this.acknowledgmentDBController.clearACKDB();
        this.addressDBController.deleteAllAdresses();
    }
}

