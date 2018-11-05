package com.alexbros.opidlubnyi.allfootball;

import android.os.Handler;

public class PeriodicalTimer {
    private Handler handler = new Handler();
    private long period;
    private Runnable task;
    private long lastRunTime = 0;

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            task.run();

            lastRunTime = System.currentTimeMillis();

            handler.postDelayed(runnableCode, period);
        }
    };

    public PeriodicalTimer(long period, Runnable task) {
        this.period = period;
        this.task = task;
    }

    public void start() {
        handler.removeCallbacks(runnableCode);

        final long diff = System.currentTimeMillis() - lastRunTime;

        if (diff >= period || diff <= 0) {
            handler.post(runnableCode);
        } else {
            final long delay = period - diff;
            handler.postDelayed(runnableCode, delay);
        }
    }

    public boolean start(long temporaryPeriod) {
        final long diff = System.currentTimeMillis() - lastRunTime;

        if (diff >= temporaryPeriod || diff <= 0) {
            handler.removeCallbacks(runnableCode);
            handler.post(runnableCode);

            return true;
        }

        return false;
    }

    public void restart() {
        handler.removeCallbacks(runnableCode);
        handler.post(runnableCode);
    }

    public void restart(long newPeriod, long delay) {
        this.period = newPeriod;

        handler.removeCallbacks(runnableCode);
        handler.postDelayed(runnableCode, delay);
    }

    public void stop() {
        handler.removeCallbacks(runnableCode);
    }
}
