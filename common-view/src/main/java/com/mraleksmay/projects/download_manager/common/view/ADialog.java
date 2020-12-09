package com.mraleksmay.projects.download_manager.common.view;

import javax.swing.*;
import java.awt.*;

public abstract class ADialog extends JDialog {
    /**
     * Stores information about a user selection in a dialog box.
     */
    protected DialogResult result = DialogResult.NONE;


    // Constructors
    public ADialog() {
    }

    public ADialog(Frame frame) {
        super(frame);
    }

    public ADialog(Window window) {
        super(window);
    }


    public abstract void init();

    public abstract DialogResult showDialog();


    // Getters and Setters
    public DialogResult getResult() {
        return result;
    }

    public void setResult(DialogResult result) {
        this.result = result;
    }
}
