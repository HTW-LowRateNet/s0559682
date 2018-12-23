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


    public void addAddress(int i) {
        this.addressDb.addData(i);
    }


    public int getAddress(int i) {
        return this.addressDb.getData(i);
    }

    public boolean deleteAddress(int i) {
        return this.addressDb.deleteData(i);
    }


    public int getLastAddress() {
        return this.addressDb.getLasEntry();
    }

}
