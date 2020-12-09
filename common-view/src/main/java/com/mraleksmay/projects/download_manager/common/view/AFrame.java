package com.mraleksmay.projects.download_manager.common.view;

import javax.swing.*;
import java.awt.*;

public abstract class AFrame extends JFrame {
    /**
     * Stores information about a user selection in a frame.
     */
    protected DialogResult result = DialogResult.NONE;


    // Constructors
    public AFrame() throws HeadlessException {
        super();
    }

    public AFrame(GraphicsConfiguration gc) {
        super(gc);
    }

    public AFrame(String title) throws HeadlessException {
        super(title);
    }

    public AFrame(String title, GraphicsConfiguration gc) {
        super(title, gc);
    }


    public abstract void init();

    public abstract DialogResult showFrame();


    // Getters and Setters
    public DialogResult getResult() {
        return result;
    }

    public void setResult(DialogResult result) {
        this.result = result;
    }
}
