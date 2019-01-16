package technikMobiler.config;

import technikMobiler.module.AddressDBController;

import java.util.Random;

public class AddressHelper {

    public static int coordinatorAddress = 0x0000;

    private static int temporaryAddressesUpperBound = 0x0FFF;
    private static int temporaryAddressesLowerBound = 0x0011;

    private static int temporaryCoordinatorAddressesUpperBound = 0x000F;
    private static int temporaryCoordinatorAddressesLowerBound = 0x0001;

    private static int permanentAddressLowerBound = 0x1000;
    private static int permanentAddressUpperBound = 0xFFFE;

    private final static int adder = 1;


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
        String addr = Integer.toHexString(myRandomNumber);
        addr = convertToPaddedHex(addr.toUpperCase());
        System.out.printf("own temp addr: " + addr); // Prints it in hex, such as "0x14"
        // or....
        return addr; // Random hex number in result
    }


    public static Integer createPermanentAddress () {
        Integer lastSentAddress = addressDBController.getLastAddress();
        System.out.println("last address which was saved: " + Integer.toHexString(lastSentAddress));
        int newAddress = lastSentAddress + adder;
//        String hex = Integer.toHexString(newAddress);
//        String paddedHexString = convertToPaddedHex(hex);
//        System.out.println("new Address " + paddedHexString);
        return newAddress;
    }

    public static String convertToPaddedHex(String hex) {
        int length = 4;
        int result = length - hex.length();
        for(int i = 0; i < result; i++) {
            hex = "0" + hex;
        }
        return hex;
    }
}
