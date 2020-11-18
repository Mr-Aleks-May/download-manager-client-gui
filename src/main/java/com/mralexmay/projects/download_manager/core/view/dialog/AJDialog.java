package com.mralexmay.projects.download_manager.core.view.dialog;


import javax.swing.*;
import java.awt.*;

public abstract class AJDialog extends JDialog {
    private DialogResult result = DialogResult.NONE;


    public AJDialog() {
    }

    public AJDialog(Frame frame) {
        super(frame);
    }

    public AJDialog(Frame frame, boolean b) {
        super(frame, b);
    }

    public AJDialog(Frame frame, String s) {
        super(frame, s);
    }

    public AJDialog(Frame frame, String s, boolean b) {
        super(frame, s, b);
    }

    public AJDialog(Frame frame, String s, boolean b, GraphicsConfiguration graphicsConfiguration) {
        super(frame, s, b, graphicsConfiguration);
    }

    public AJDialog(Dialog dialog) {
        super(dialog);
    }

    public AJDialog(Dialog dialog, boolean b) {
        super(dialog, b);
    }

    public AJDialog(Dialog dialog, String s) {
        super(dialog, s);
    }

    public AJDialog(Dialog dialog, String s, boolean b) {
        super(dialog, s, b);
    }

    public AJDialog(Dialog dialog, String s, boolean b, GraphicsConfiguration graphicsConfiguration) {
        super(dialog, s, b, graphicsConfiguration);
    }

    public AJDialog(Window window) {
        super(window);
    }

    public AJDialog(Window window, ModalityType modalityType) {
        super(window, modalityType);
    }

    public AJDialog(Window window, String s) {
        super(window, s);
    }

    public AJDialog(Window window, String s, ModalityType modalityType) {
        super(window, s, modalityType);
    }

    public AJDialog(Window window, String s, ModalityType modalityType, GraphicsConfiguration graphicsConfiguration) {
        super(window, s, modalityType, graphicsConfiguration);
    }


    public void setResult(DialogResult result) {
        this.result = result;
    }


    public DialogResult getResult() {
        return result;
    }
}
