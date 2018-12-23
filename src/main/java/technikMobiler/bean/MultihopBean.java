package technikMobiler.bean;

import technikMobiler.module.AcknowledgmentDBController;
import technikMobiler.module.AddressDBController;
import technikMobiler.module.MessageDBController;

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
        Integer permanentAddress = Integer.valueOf(message.getSender());
        boolean success = this.acknowledgmentDBController.checkAddress(permanentAddress);
        if(success) {
            this.acknowledgmentDBController.removeAddress(permanentAddress);
        }
        this.addressDBController.addAddress(permanentAddress);
    }

    public void handleADDRRequestAsCoordinator(Integer permADDR, Integer tempAddr) {
        this.acknowledgmentDBController.addAddressPair(permADDR, tempAddr);
    }

    public boolean checkIfMessageIsTheSame(Message message) {
        return this.messageDBController.checkMessageIfItsTheSame(message);
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
}

