package technikMobiler.concurrent;

import technikMobiler.module.AcknowledgmentDBController;

public class CheckingAcknowledgmentDB implements Runnable {


    private AcknowledgmentDBController acknowledgmentDBController = AcknowledgmentDBController.getSingleInstance();
    @Override
    public void run() {
        while (true) {

        }
    }
}
