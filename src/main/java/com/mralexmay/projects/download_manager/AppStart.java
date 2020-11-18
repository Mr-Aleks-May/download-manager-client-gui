package com.mralexmay.projects.download_manager;

import com.mralexmay.projects.download_manager.view.MainFrame;

import javax.swing.*;
import java.awt.*;

public class AppStart {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        JFrame frame = new MainFrame();
        frame.setTitle("Downloader");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(1000, 650));
        frame.setPreferredSize(new Dimension(1000, 650));
        frame.pack();
        frame.setVisible(true);
    }
}
