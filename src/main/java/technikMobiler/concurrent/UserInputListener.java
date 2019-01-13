package technikMobiler.concurrent;

import technikMobiler.controller.MessageController;
import technikMobiler.controller.SenderController;

import java.util.Scanner;

public class UserInputListener implements Runnable {

	private SenderController sender;

    private MessageController messageController;

	public UserInputListener(SenderController sender) {
		this.sender = sender;
		this.messageController = new MessageController(this.sender);
	}

	@Override
    public void run() {
        while (true) {
            Scanner inputReader = new Scanner(System.in).useDelimiter(System.getProperty("line.separator"));
            while (inputReader.hasNext()) {
                String userinput = inputReader.next();
                System.out.println("User typed: " + userinput);
                sender.sendATCommand(userinput);
//                messageController.handleMessage(userinput);
                System.out.println();
            }
            inputReader.close();
        }
    }

}
