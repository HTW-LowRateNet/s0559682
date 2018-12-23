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
         return data == null;
     }

     protected void removeData(Integer key) {
         this.ackDB.remove(key);
     }


}
