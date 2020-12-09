package com.mraleksmay.projects.download_manager.common.view;


import com.mraleksmay.projects.download_manager.common.annotation.NotNull;
import com.mraleksmay.projects.download_manager.common.exception.ThreadAlreadyStartException;
import com.mraleksmay.projects.download_manager.common.exception.ThreadAlreadyStopException;

/**
 * Data is being updated after a specified period of time
 */
public abstract class UIWorker {
    /**
     * Update thread.
     */
    private Thread uiUpdater;
    /**
     * Time interval.
     */
    private int millis;
    /**
     * Updater status.
     */
    private volatile boolean isAlive = true;
    /**
     * Actions performed every time when update occurs.
     */
    private Runnable uiUpdateAction;

    // Constructors
    public UIWorker(@NotNull final Runnable uiUpdateAction,
                    final int millis) {
        // Set actions performed every time when update occurs
        this.uiUpdateAction = uiUpdateAction;
        // Set time interval
        this.millis = millis;
    }

    public void start() throws ThreadAlreadyStartException {
        // Check if updater exists and updater is running
        if (this.uiUpdater != null && this.uiUpdater.isAlive()) {
            throw new ThreadAlreadyStartException();
        }

        // Initialize new updater
        uiUpdater = new Thread(() -> {
            while (isAlive) {
                // Do specified actions
                uiUpdateAction.run();

                // Check if the update will be performed at some time intervals or once
                if (millis < 0) {
                    isAlive = false;
                } else {
                    try {
                        // Sleep
                        Thread.sleep(millis);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        // Set update status to true
        isAlive = true;
        // Start updater
        uiUpdater.start();
    }

    public void stop() throws ThreadAlreadyStopException {
        // Check if updater exists and updater is not running
        if (this.uiUpdater == null || !this.uiUpdater.isAlive() || !this.uiUpdater.isInterrupted()) {
            throw new ThreadAlreadyStopException();
        }

        // Set update status to false
        isAlive = false;
    }
}
