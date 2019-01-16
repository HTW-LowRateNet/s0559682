package technikMobiler.controller;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.util.Console;
import technikMobiler.concurrent.CheckingMessageDB;
import technikMobiler.concurrent.IncomingMessageListener;
import technikMobiler.concurrent.MessageQueueHandler;
import technikMobiler.concurrent.UserInputListener;
import technikMobiler.config.SerialConfigurator;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class NetworkController {



    final static Console console = new Console();
    public static Boolean lock1 = true;
    final Serial serial = SerialFactory.createInstance();
    final SenderController senderController = new SenderController(serial);
    final SerialConfigurator configurator = new SerialConfigurator(serial, console);
    final MessageController messageController = new MessageController(senderController);



    public void startNetwork(String[] args) throws InterruptedException, IOException {

        console.title("Ad-Hoc Network with LoRa", "Technik Mobiler Systeme");


        console.promptForExit();


        configurator.configureSerial(args);


        BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(300);
        Thread incomingMessageListener = new Thread(new IncomingMessageListener(serial, messageQueue));
        Thread messageHandler = new Thread(new MessageQueueHandler(messageController, messageQueue));
        Thread userInputListener = new Thread(new UserInputListener(senderController));
        Thread checkingMessageDB = new Thread(new CheckingMessageDB());
        incomingMessageListener.start();
        userInputListener.start();
        checkingMessageDB.start();
        messageHandler.start();

        senderController.configureModule();

        while (console.isRunning()) {
            try {
            if (senderController.getMultihopBean().isCoordinator()) {
                senderController.sendCoordinatorIsStillAlive();
                Thread.sleep(4000);
            } else {
                if (senderController.isCoordinatorPresent()) {
                    System.out.println("Koordinator existiert");
                    Thread.sleep(5000);
                } else {
                    System.out.println("Kein Koordinator vorhanden");
                    for (int i = 0; i < 4; i++) {
                        if(senderController.isCoordinatorPresent()) {
                            break;
                        }
                        senderController.findCoordinator();
                        Thread.sleep(5000);
                    }
                    if(!senderController.isCoordinatorPresent()) {
                        this.senderController.setCoordinator();
                    }
                }
            }
            } catch (IllegalStateException ex) {
                System.out.println(ex.getMessage());
            }
            Thread.sleep(10000);
        }
    }
}
