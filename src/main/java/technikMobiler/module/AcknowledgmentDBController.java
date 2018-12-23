package technikMobiler.module;

public class AcknowledgmentDBController {

    private static AcknowledgmentDBController singleInstance = null;

    private AcknowledgmentDB acknowledgmentDB;

    private AcknowledgmentDBController() {
        acknowledgmentDB = new AcknowledgmentDB();
    }

    public static AcknowledgmentDBController getSingleInstance() {
        if(singleInstance == null) {
            singleInstance = new AcknowledgmentDBController();
        }
        return singleInstance;
    }

    public void addAddressPair(Integer permanentAddr, Integer tempAddr) {
        this.acknowledgmentDB.addData(permanentAddr, tempAddr);
    }


    public boolean checkAddress(Integer permanentAddr) {
        return this.acknowledgmentDB.checkData(permanentAddr);
    }


    public void removeAddress(Integer permanentAddr) {
        this.acknowledgmentDB.removeData(permanentAddr);
    }
}
