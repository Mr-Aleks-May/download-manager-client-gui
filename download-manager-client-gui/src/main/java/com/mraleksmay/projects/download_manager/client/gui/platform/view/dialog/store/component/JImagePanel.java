package com.mraleksmay.projects.download_manager.client.gui.platform.view.dialog.store.component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class JImagePanel extends JPanel {
    private Image image;


    public JImagePanel() {
    }

    public JImagePanel(Image image) {
        this.image = image;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
        }
    }


    public Image getImage() {
        return image;
    }

    public JImagePanel setImage(Image image) {
        this.image = image;
        return this;
    }
}
