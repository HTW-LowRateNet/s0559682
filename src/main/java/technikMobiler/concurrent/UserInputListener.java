package technikMobiler.concurrent;

import technikMobiler.controller.MessageReceiverController;
import technikMobiler.controller.SenderController;

import java.util.Scanner;

public class UserInputListener implements Runnable {

	private SenderController sender;

    private MessageReceiverController messageReceiverController;

	public UserInputListener(SenderController sender) {
		this.sender = sender;
		this.messageReceiverController = new MessageReceiverController(this.sender);
	}

	@Override
    public void run() {
        while (true) {
            Scanner inputReader = new Scanner(System.in).useDelimiter(System.getProperty("line.separator"));
            while (inputReader.hasNext()) {
                String userinput = inputReader.next();
                System.out.println("User typed: " + userinput);
                sender.sendATCommand(userinput);
//                messageReceiverController.parseIncoming(userinput);
                System.out.println();
            }
            inputReader.close();
        }
    }

}
