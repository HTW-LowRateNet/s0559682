package technikMobiler.module;

import java.util.ArrayList;
import java.util.List;

class AddressDB {
    private List<Integer> db;
    private int lasEntry;


    protected AddressDB() {
        this.db = new ArrayList<>();
        this.lasEntry = 0x0100;
    }

    protected void addData(int data) {
        this.db.add(data);
        this.lasEntry = data;
    }

    protected int getData(int data) {
        int addr = -1;
        if(db.size() == 0) {
            return -1;
        }
        for(Integer i : this.db) {
            if(i == data) {
                addr = i;
            }
        }
        return addr;
    }

    protected boolean deleteData(int data) {
        boolean deleted = false;
        int index = -1;
        for(int i = 0; i < db.size(); i ++) {
            if(this.db.get(i) == data) {
                this.db.remove(i);
                deleted = true;
                this.lasEntry = db.get(db.size()-1);
            }
        }
        return deleted;
    }

    protected int getLasEntry() {
        return this.lasEntry;
    }
}
