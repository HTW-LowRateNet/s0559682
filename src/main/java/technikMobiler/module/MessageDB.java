package technikMobiler.module;

import technikMobiler.bean.Message;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class MessageDB {

    private ConcurrentHashMap<String, Message> msgDB;

    protected MessageDB() {
        this.msgDB = new ConcurrentHashMap<>();
    }

    protected void addData(String key, Message value) {
        this.msgDB.put(key, value);
    }

    protected Message getData(String key) {
        return this.msgDB.get(key);
    }


    protected void deleteData(String key) {
        this.msgDB.remove(key);
    }


}
