package GUI;

import DataStructures.Game;

/**
 * Osztály, ami elvégzi a játékállás frissítését. Külön szálon futtatja magát.
 */
public class GameTimer implements Runnable {
    private boolean run = false;
    private Game game = null;
    private boolean isWorking = false;
    private int waitTime = -1;
    private GUI gui;

    private boolean updateNeeded = false;

    private Thread thread = new Thread(this);

    public GameTimer(GUI gui) {
        this.gui = gui;
    }

    public void setRun(boolean run) {
        this.run = run;
        if (run) {
            this.thread.start();   
        } else {
            this.thread.interrupt();
            this.thread = new Thread(this);
        }
    }

    public boolean isWorking() {
        return this.isWorking;
    }

    public void setGame(Game game) {
        this.setRun(false);
        this.game = game;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public void setUpdateNeeded(boolean updateNeeded) {
        this.updateNeeded = updateNeeded;
    }

    @Override
    public void run() {
        while (this.run) {
            this.isWorking = true;
            this.game.doRound();
            this.isWorking = false;
            try {
                Thread.sleep(this.waitTime);
            } catch (InterruptedException e) {
                this.run = false;
            }
            if (updateNeeded) {
                updateNeeded = false;
                this.gui.getBq().add(new Runnable() {
                    @Override
                    public void run() {
                        gui.refreshGameField();
                    }
                });
            }
        }
    }
}
