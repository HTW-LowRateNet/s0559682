package technikMobiler.concurrent;

import com.pi4j.io.serial.Serial;
import com.pi4j.io.serial.SerialDataEvent;
import com.pi4j.io.serial.SerialDataEventListener;
import technikMobiler.controller.MessageController;
import technikMobiler.controller.SenderController;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class IncomingMessageListener implements Runnable {

	private Serial serial;
	private BlockingQueue<String> messageQueue;

	public IncomingMessageListener(Serial serial, BlockingQueue<String> blockingQueue) {
		this.serial = serial;
		this.messageQueue = blockingQueue;
	}

	@Override
	public void run() {
		// create and register the serial data listener
		serial.addListener(new SerialDataEventListener() {

			public void dataReceived(SerialDataEvent event) {

				// NOTE! - It is extremely important to read the data received from the
				// serial port. If it does not get read from the receive buffer, the
				// buffer will continue to grow and consume memory.

				// print out the data received to the console

				try {
						System.out.println("Available: " + serial.available());
						System.out.println(serial.available());
						System.out.println(serial.available() > 0);
						if(serial.available() > 0) {
							System.out.println("[Serial input] " + event.getAsciiString());
							System.out.println("im parsing block");
							messageQueue.put(event.getAsciiString());
						}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public String getData(String data) {
		return data;
	}

}
