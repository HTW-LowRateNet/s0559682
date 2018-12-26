package technikMobiler.module;

import java.util.ArrayList;
import java.util.List;

class AddressDB {
    private List<Integer> db;
    private Integer lasEntry;


    protected AddressDB() {
        this.db = new ArrayList<>();
        this.lasEntry = 256;
    }

    protected void addData(Integer data) {
        this.db.add(data);
        this.lasEntry = data;
    }

    protected Integer getData(Integer data) {
        int addr = -1;
        if(db.size() == 0) {
            return addr;
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

    protected Integer getLasEntry() {
        return this.lasEntry;
    }

    protected void clearDB() {
        this.db.clear();
    }
}
