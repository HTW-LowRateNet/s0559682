package technikMobiler.module;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    public void printAddresses() {
        for (Map.Entry<Integer, Integer> entry : this.acknowledgmentDB.getAllData().entrySet()) {
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
    }

    public void clearACKDB() {
        this.acknowledgmentDB.clearDB();
    }
}
