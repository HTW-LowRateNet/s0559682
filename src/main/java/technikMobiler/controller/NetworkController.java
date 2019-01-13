package technikMobiler.controller;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialFactory;
import com.pi4j.util.Console;
import technikMobiler.concurrent.CheckingMessageDB;
import technikMobiler.concurrent.IncomingMessageListener;
import technikMobiler.concurrent.MessageHandler;
import technikMobiler.concurrent.UserInputListener;
import technikMobiler.config.SerialConfigurator;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class NetworkController {


    /*
    temporäre eigene Adresse zum koordinator schicken
    feste addresse bekommt man von koordinator
    addresse dann selbst setzen
    ---> ADDR als code
    KIDS --> CDIS
    */

    // Structural variables
    final static Console console = new Console();
    public static Boolean lock1 = true;
    final Serial serial = SerialFactory.createInstance(); // create an instance of the serial communications
    final SenderController senderController = new SenderController(serial);// create an instance of the MultihopBean class
    final SerialConfigurator configurator = new SerialConfigurator(serial, console);
    final MessageController messageController = new MessageController(senderController);

    // business logic variables
//    String address; // Current address of this node
//    static boolean isCoordinator = false; // Is this node the coordinator in the current network?
//    static boolean coordinatorIsPresent = false;
//    List<String> neighbors; // List of known neighbors
//    List<String> allNodesInNetwork; // If this node is coordinator, it should keep a record of all nodes currently
//    // in the network

    /**
     * This example program supports the following optional command
     * arguments/options: "--device (device-path)" [DEFAULT: /dev/ttyAMA0] "--baud
     * (baud-rate)" [DEFAULT: 38400] "--data-bits (5|6|7|8)" [DEFAULT: 8] "--parity
     * (none|odd|even)" [DEFAULT: none] "--stop-bits (1|2)" [DEFAULT: 1]
     * "--flow-control (none|hardware|software)" [DEFAULT: none]
     *
     * @param args
     * @throws InterruptedException
     * @throws IOException
     */
    public void startNetwork(String[] args) throws InterruptedException, IOException {

        console.title("<-- HUHN-P Project -->", "Technik Mobiler Systeme");

        // allow for user to exit program using CTRL-C
        console.promptForExit();

        //configure module
        configurator.configureSerial(args);

        // create and register the serial data listener
        BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(300);
        Thread incomingMessageListener = new Thread(new IncomingMessageListener(serial, messageQueue));
        Thread messageHandler = new Thread(new MessageHandler(messageController, messageQueue));
        Thread userInputListener = new Thread(new UserInputListener(senderController));
        Thread checkingMessageDB = new Thread(new CheckingMessageDB());
        incomingMessageListener.start();
        userInputListener.start();
        checkingMessageDB.start();
        messageHandler.start();

        // start initial congfiguration of module and set own temporary address
        senderController.configureModule();


        // continuous loop to keep the program running until the user terminates the
        // program
        while (console.isRunning()) {
            try {
                System.out.println("Koordinatorstand: " + senderController.getMultihopBean().isCoordinator());
            if (senderController.getMultihopBean().isCoordinator()) {
                senderController.sendCoordinatorKeepAlive();
                System.out.println("");
                Thread.sleep(8000);
            } else {
                if (senderController.isCoordinatorPresent()) {
                    System.out.println("Coordinator is present.");
                    Thread.sleep(10000);
                } else {
                    System.out.println("Missing Coordinator.");
                    for (int i = 0; i < 4; i++) {
                        if(senderController.isCoordinatorPresent()) {
                            break;
                        }
                        senderController.discoverCoordinator();
                        Thread.sleep(8000);
                    }
                    if(!senderController.isCoordinatorPresent()) {
                        this.senderController.setCoordinator();
                    }
                }
            }
            } catch (IllegalStateException ex) {
                System.out.println(ex.getMessage());
            }
            // wait 1 second before continuing
            Thread.sleep(10000);
        }
        System.out.println("No Threading?");
    }
}
