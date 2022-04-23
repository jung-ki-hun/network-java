package com.nhnacademy.network;

public class ThreadStop extends Thread {
    private boolean stop = false;
    @Override
    public void run() {
        try {
            Thread.sleep(3000);
            this.stop = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public boolean isStop() {
        return stop;
    }
}
