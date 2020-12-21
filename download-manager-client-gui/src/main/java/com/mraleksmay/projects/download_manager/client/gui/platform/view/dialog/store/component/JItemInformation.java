/*
 * Created by JFormDesigner on Sun Dec 20 16:41:35 EET 2020
 */

package com.mraleksmay.projects.download_manager.client.gui.platform.view.dialog.store.component;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @author unknown
 */
public class JItemInformation extends JPanel {
    private String title;
    private String version;
    private String author;
    private BufferedImage image;
    private String description;
    private String tags;
    private List<ActionItemListener> listeners = new ArrayList<>();


    public JItemInformation(String title, String version, String author, BufferedImage image) {
        initComponents();
        this.title = title;
        this.version = version;
        this.author = author;
        this.image = image;

        getJLBL_title().setText(title);
        getJLBL_version().setText(version);
        getJLBL_author().setText(author);
        getJPNL_image().setImage(image);
    }

    public JItemInformation(String title, String version, String author, BufferedImage image, String description, String tags) {
        initComponents();

        this.title = title;
        this.version = version;
        this.author = author;
        this.image = image;
        this.description = description;
        this.tags = tags;

        getJLBL_title().setText(title);
        getJLBL_version().setText(version);
        getJLBL_author().setText(author);
        getJPNL_image().setImage(image);
    }


    private void jpnl_itemMouseClicked(MouseEvent e) {
        for (ActionItemListener listener : listeners) {
            listener.actionPerformed(this);
        }
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        jpnl_item = new JPanel();
        jpnl_image = new JImagePanel();
        jlbl_title = new JLabel();
        jlbl_version = new JLabel();
        jlbl_author = new JLabel();

        //======== this ========
        setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new
                javax.swing.border.EmptyBorder(0, 0, 0, 0), "JFor\u006dDesi\u0067ner \u0045valu\u0061tion", javax
                .swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java
                .awt.Font("Dia\u006cog", java.awt.Font.BOLD, 12), java.awt
                .Color.red), getBorder()));
        addPropertyChangeListener(new java.beans.
                PropertyChangeListener() {
            @Override
            public void propertyChange(java.beans.PropertyChangeEvent e) {
                if ("bord\u0065r".
                        equals(e.getPropertyName())) throw new RuntimeException();
            }
        });
        setLayout(new GridBagLayout());
        ((GridBagLayout) getLayout()).columnWidths = new int[]{65, 0};
        ((GridBagLayout) getLayout()).rowHeights = new int[]{0, 0};
        ((GridBagLayout) getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
        ((GridBagLayout) getLayout()).rowWeights = new double[]{1.0, 1.0E-4};

        //======== jpnl_item ========
        {
            jpnl_item.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    jpnl_itemMouseClicked(e);
                }
            });
            jpnl_item.setLayout(new GridBagLayout());
            ((GridBagLayout) jpnl_item.getLayout()).columnWidths = new int[]{70, 0, 0};
            ((GridBagLayout) jpnl_item.getLayout()).rowHeights = new int[]{0, 0, 0, 0};
            ((GridBagLayout) jpnl_item.getLayout()).columnWeights = new double[]{0.0, 1.0, 1.0E-4};
            ((GridBagLayout) jpnl_item.getLayout()).rowWeights = new double[]{1.0, 0.0, 0.0, 1.0E-4};

            //---- jpnl_image ----
            jpnl_image.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    jpnl_itemMouseClicked(e);
                }
            });
            jpnl_item.add(jpnl_image, new GridBagConstraints(0, 0, 1, 3, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- jlbl_title ----
            jlbl_title.setText("title");
            jlbl_title.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    jpnl_itemMouseClicked(e);
                }
            });
            jpnl_item.add(jlbl_title, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

            //---- jlbl_version ----
            jlbl_version.setText("version");
            jlbl_version.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    jpnl_itemMouseClicked(e);
                }
            });
            jpnl_item.add(jlbl_version, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

            //---- jlbl_author ----
            jlbl_author.setText("author");
            jlbl_author.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    jpnl_itemMouseClicked(e);
                }
            });
            jpnl_item.add(jlbl_author, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        add(jpnl_item, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel jpnl_item;
    private JImagePanel jpnl_image;
    private JLabel jlbl_title;
    private JLabel jlbl_version;
    private JLabel jlbl_author;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    public String getTitle() {
        return title;
    }

    public String getVersion() {
        return version;
    }

    public String getAuthor() {
        return author;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getTags() {
        return tags;
    }

    public JPanel getJPNL_item() {
        return jpnl_item;
    }

    public JImagePanel getJPNL_image() {
        return jpnl_image;
    }

    public JLabel getJLBL_title() {
        return jlbl_title;
    }

    public JLabel getJLBL_version() {
        return jlbl_version;
    }

    public JLabel getJLBL_author() {
        return jlbl_author;
    }


    public JItemInformation addActionListener(ActionItemListener listener) {
        listeners.add(listener);
        return this;
    }

    public JItemInformation removeListeners(ActionItemListener listener) {
        listeners.remove(listener);
        return this;
    }
}

