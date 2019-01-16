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
		serial.addListener(new SerialDataEventListener() {
			public void dataReceived(SerialDataEvent event) {
				try {
					System.out.println("Daten im buffer " + event.getAsciiString());
					messageQueue.put(event.getAsciiString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
