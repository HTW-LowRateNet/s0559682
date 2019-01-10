package technikMobiler.concurrent;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import technikMobiler.controller.MessageReceiverController;
import technikMobiler.controller.NetworkController;
import technikMobiler.controller.SenderController;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IncomingMessageListener implements Runnable {

	private Serial serial;
	private SenderController senderController;
	private MessageReceiverController messageReceiverController;
	private ConcurrentLinkedQueue<SerialDataEvent> queue;
	public IncomingMessageListener(Serial serial, SenderController senderController) {
		this.serial = serial;
		this.senderController = senderController;
		this.messageReceiverController = new MessageReceiverController(this.senderController);
		this.queue = new ConcurrentLinkedQueue<>();
	}

	@Override
	public void run() {
		// create and register the serial data listener
		serial.addListener(new SerialDataEventListener() {
			public void dataReceived(SerialDataEvent event) {
				queue.add(event);
				// NOTE! - It is extremely important to read the data received from the
				// serial port. If it does not get read from the receive buffer, the
				// buffer will continue to grow and consume memory.

				// print out the data received to the console

				try {
					synchronized (queue) {
						System.out.println("[Serial input] " + event.getAsciiString());
						SerialDataEvent actualEvent = queue.poll();
						messageReceiverController.parseIncoming(actualEvent.getAsciiString().toString());
						NetworkController.lock1.notify();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

	}

	public String getData(String data) {
		return data;
	}

}
