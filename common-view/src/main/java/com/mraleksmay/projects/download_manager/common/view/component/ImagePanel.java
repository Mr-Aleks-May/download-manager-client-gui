package com.mraleksmay.projects.download_manager.common.view.component;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    /**
     * Image to draw.
     */
    private BufferedImage image;


    // Constructors
    public ImagePanel() {
    }

    public ImagePanel(BufferedImage image) {
        this.image = image;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (getImage() != null) {
            // Draw image on panel
            g.drawImage(getImage(), 0, 0, this);
        }
    }


    // Getters and Setters
    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
