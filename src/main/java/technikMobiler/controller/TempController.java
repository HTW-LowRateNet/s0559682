package technikMobiler.controller;

import com.pi4j.io.serial.Serial;
import technikMobiler.bean.Message;

public class TempController {
    private Serial serial;

    public TempController(Serial serial) {
        this.serial = serial;
    }

    public static void sendMessage(Message message) {
        switch (message.getCode()) {
            case "DISC":
                break;
            case "CDIS":
                break;
            case "ADDR":
                break;
            case "MSSG":
                break;
            case "POLL":
                break;
            default:
                System.out.println("ERROR: Unknown Message code: " + message.getCode());
                break;
        }

    }
}
