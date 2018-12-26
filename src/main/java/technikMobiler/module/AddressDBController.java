package technikMobiler.module;

public class AddressDBController {

    private static AddressDBController singleInstance = null;

    private AddressDB addressDb;

    private AddressDBController() {
        addressDb = new AddressDB();
    }

    public static AddressDBController getSingleInstance() {
        if(singleInstance == null) {
            singleInstance = new AddressDBController();
        }
        return singleInstance;
    }


    public void addAddress(Integer i) {
        this.addressDb.addData(i);
    }


    public Integer getAddress(Integer i) {
        return this.addressDb.getData(i);
    }

    public boolean deleteAddress(Integer i) {
        return this.addressDb.deleteData(i);
    }


    public Integer getLastAddress() {
        return this.addressDb.getLasEntry();
    }

    public void deleteAllAdresses() {
        this.addressDb.clearDB();
    }

}
