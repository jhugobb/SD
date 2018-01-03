package Server.Game;

import Server.Hub;

public class Timer implements Runnable{
    private Hub gameHub;

    Timer(Hub gameHub) {
        this.gameHub = gameHub;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(30000);
            gameHub.timeOut();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
