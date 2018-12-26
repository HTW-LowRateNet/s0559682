package technikMobiler.module;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

class AcknowledgmentDB {
     private ConcurrentHashMap<Integer, Integer> ackDB;

     protected AcknowledgmentDB() {
         this.ackDB = new ConcurrentHashMap<>();
     }

     protected void addData(Integer key, Integer value) {
         this.ackDB.put(key, value);
     }

     protected boolean checkData(Integer key) {
         Integer data = this.ackDB.get(key);
         System.out.println("checkData, Data: " + data + " key: " + key);
         System.out.println("success:" + data != null);
         return data != null;
     }

     protected void removeData(Integer key) {
         this.ackDB.remove(key);
     }

     protected ConcurrentHashMap<Integer, Integer> getAllData() {
         return this.ackDB;
     }

     protected void clearDB() {
         this.ackDB.clear();
     }


}
