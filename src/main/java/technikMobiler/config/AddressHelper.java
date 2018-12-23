package technikMobiler.config;

import technikMobiler.module.AddressDBController;

import java.util.Random;

public class AddressHelper {

    public static int coordinatorAddress = 0x0000;

    private static int temporaryAddressesUpperBound = 0x00FF;
    private static int temporaryAddressesLowerBound = 0x0011;

    private static int temporaryCoordinatorAddressesUpperBound = 0x000F;
    private static int temporaryCoordinatorAddressesLowerBound = 0x0001;

    private static int permanentAddressLowerBound = 0x0100;
    private static int permanentAddressUpperBound = 0xFFFE;

    private final static int adder = 0x0001;


    private static int AddressLowerBound = 0x0100;
    private static int AddressUpperBound = 0xFFFE;

    private static AddressDBController addressDBController = AddressDBController.getSingleInstance();


    /**
     * this method is used by the Node on startup. It is necessary to request a permanent address.
     * @return
     */
    public static String createTemporaryNodeAddress() {
        Random rand = new Random();
        int myRandomNumber = rand.nextInt(temporaryAddressesUpperBound-temporaryAddressesLowerBound) + temporaryAddressesLowerBound; // Generates a random number between 0011 and 00FF (00EE+0011)
        System.out.printf("%x\n",myRandomNumber); // Prints it in hex, such as "0x14"
        // or....
        return Integer.toHexString(myRandomNumber).toUpperCase(); // Random hex number in result
    }


    public static Integer createPermanentAddress () {
        int lastSentAddress = addressDBController.getLastAddress();
        int newAddress = lastSentAddress + adder;
        System.out.printf("New Address + %x\n",newAddress);
        return newAddress;
    }
}
