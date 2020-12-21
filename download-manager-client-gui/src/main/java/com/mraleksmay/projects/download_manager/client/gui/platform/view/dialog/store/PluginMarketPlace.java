/*
 * Created by JFormDesigner on Sun Dec 20 15:58:29 EET 2020
 */

package com.mraleksmay.projects.download_manager.client.gui.platform.view.dialog.store;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.RESTClient;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.service.marketplace.dto.PluginSearchResultDto;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.dialog.store.component.*;
import com.mraleksmay.projects.download_manager.common.exception.ThreadAlreadyStartException;
import com.mraleksmay.projects.download_manager.common.view.ADialog;
import com.mraleksmay.projects.download_manager.common.view.DialogResult;
import com.mraleksmay.projects.download_manager.common.view.UIWorker;
import com.mraleksmay.projects.download_manager.plugin.model.user.User;
import org.jdesktop.swingx.*;

public class PluginMarketPlace extends ADialog {
    private ApplicationManager applicationManager;
    private PluginMarketplaceController pluginMarketplaceController;
    private UIWorker uiUpdater;
    BufferedImage image = null;
    private PluginMarketPlace dialog = this;

    {
        try {
            image = ImageIO.read(new File("./resources/image/youtube_1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public PluginMarketPlace(Window owner, ApplicationManager applicationManager) {
        super(owner);
        initComponents();

        this.applicationManager = applicationManager;
        pluginMarketplaceController = new PluginMarketplaceController(applicationManager);

        uiUpdater = new MarketplaceUIUpdater(() -> {
            try {
                String token = User.getCurrentUser().getToken();
                List<PluginSearchResultDto> allPlugins = RESTClient.MarketplaceService.getAllPlugins(token);

                getJPNL_plugins_mp().removeAll();

                for (int i = 0; i < allPlugins.size(); i++) {
                    PluginSearchResultDto plugin = allPlugins.get(i);
                    String title = plugin.getName();
                    String version = plugin.getVersion();
                    String author = plugin.getAuthor().get();
                    String description = plugin.getDescription();
                    String tags = plugin.getTags().stream()
                            .reduce((t1, t2) -> t1 + ",#" + t2)
                            .get();


                    JItemInformation item = new JItemInformation(title, version, author, image, description, tags);
                    getJPNL_plugins_mp().add(item);

                    // item.getJPNL_item().setBackground(Color.GRAY);
                    item.addActionListener(new ActionItemListener() {
                        @Override
                        public void actionPerformed(Object sender) {
                            System.out.println("test");
                            JItemInformation obj = (JItemInformation) sender;
                            dialog.getJlbl_title().setText(obj.getTitle());
                            dialog.getJta_mp_description().setText(obj.getDescription());
                            dialog.getJlbl_version().setText(obj.getVersion());
                            dialog.getJlbl_author().setText(obj.getAuthor());
                            dialog.getJlbl_tags().setText("#" + obj.getTags());
                            obj.setBackground(Color.GRAY);
                        }
                    });
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }, 10000);
    }


    @Override
    public void init() {
        JComboBox jcb = new JComboBox<>();
        jcb.addItem("Name");
        getJcb_mpst().add(jcb);

        try {
            uiUpdater.start();
        } catch (ThreadAlreadyStartException e) {
        }
    }

    @Override
    public DialogResult showDialog() {
        try {
            final ADialog dialog = this;
            dialog.setTitle("Marketplace");
            dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            final Dimension size = new Dimension(700, 500);
            dialog.setPreferredSize(size);
            dialog.setSize(size);
            dialog.setModal(true);
            dialog.pack();
            dialog.setVisible(true);
        } catch (Exception e) {
        }

        return getResult();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        tabbedPane1 = new JTabbedPane();
        panel1 = new JPanel();
        panel3 = new JPanel();
        jtf_marketplace_search = new JTextField();
        jcb_mpst = new JComboBox();
        jbtn_marketplace_search = new JButton();
        scrollPane2 = new JScrollPane();
        jpnl_plugins_mp = new JPanel();
        panel6 = new JPanel();
        panel15 = new JPanel();
        panel16 = new JPanel();
        label1 = new JLabel();
        jlbl_title = new JLabel();
        label2 = new JLabel();
        jlbl_version = new JLabel();
        label3 = new JLabel();
        jlbl_author = new JLabel();
        label4 = new JLabel();
        jlbl_tags = new JLabel();
        scrollPane1 = new JScrollPane();
        jta_mp_description = new JTextArea();
        panel2 = new JPanel();
        panel4 = new JPanel();
        jtf_plugin_search = new JTextField();
        jcb_plugin_search_type = new JComboBox();
        jbtn_plugin_search = new JButton();
        panel14 = new JPanel();
        panel17 = new JPanel();
        panel18 = new JPanel();
        label5 = new JLabel();
        jlbl_title2 = new JLabel();
        label6 = new JLabel();
        jlbl_version2 = new JLabel();
        label7 = new JLabel();
        jlbl_author2 = new JLabel();
        label8 = new JLabel();
        jlbl_tags2 = new JLabel();
        scrollPane4 = new JScrollPane();
        jta_installed_description = new JTextArea();
        scrollPane3 = new JScrollPane();
        jpnl_plugins_mp2 = new JPanel();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        var contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout) contentPane.getLayout()).columnWidths = new int[]{0, 0};
        ((GridBagLayout) contentPane.getLayout()).rowHeights = new int[]{0, 0};
        ((GridBagLayout) contentPane.getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
        ((GridBagLayout) contentPane.getLayout()).rowWeights = new double[]{1.0, 1.0E-4};

        //======== tabbedPane1 ========
        {

            //======== panel1 ========
            {
                panel1.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing
                        .border.EmptyBorder(0, 0, 0, 0), "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax.swing.border.TitledBorder
                        .CENTER, javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("D\u0069alog", java.
                        awt.Font.BOLD, 12), java.awt.Color.red), panel1.getBorder()))
                ;
                panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                    @Override
                    public void propertyChange(java.beans.PropertyChangeEvent e
                    ) {
                        if ("\u0062order".equals(e.getPropertyName())) throw new RuntimeException();
                    }
                })
                ;
                panel1.setLayout(new GridBagLayout());
                ((GridBagLayout) panel1.getLayout()).columnWidths = new int[]{245, 0, 0};
                ((GridBagLayout) panel1.getLayout()).rowHeights = new int[]{0, 0, 0};
                ((GridBagLayout) panel1.getLayout()).columnWeights = new double[]{0.0, 1.0, 1.0E-4};
                ((GridBagLayout) panel1.getLayout()).rowWeights = new double[]{0.0, 1.0, 1.0E-4};

                //======== panel3 ========
                {
                    panel3.setLayout(new GridBagLayout());
                    ((GridBagLayout) panel3.getLayout()).columnWidths = new int[]{0, 0, 0, 0};
                    ((GridBagLayout) panel3.getLayout()).rowHeights = new int[]{0, 0};
                    ((GridBagLayout) panel3.getLayout()).columnWeights = new double[]{1.0, 0.0, 0.0, 1.0E-4};
                    ((GridBagLayout) panel3.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};
                    panel3.add(jtf_marketplace_search, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));
                    panel3.add(jcb_mpst, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                    //---- jbtn_marketplace_search ----
                    jbtn_marketplace_search.setText("Search");
                    panel3.add(jbtn_marketplace_search, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                }
                panel1.add(panel3, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                //======== scrollPane2 ========
                {

                    //======== jpnl_plugins_mp ========
                    {
                        jpnl_plugins_mp.setLayout(new VerticalLayout());
                    }
                    scrollPane2.setViewportView(jpnl_plugins_mp);
                }
                panel1.add(scrollPane2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //======== panel6 ========
                {
                    panel6.setBackground(new Color(102, 102, 102));
                    panel6.setLayout(new GridBagLayout());
                    ((GridBagLayout) panel6.getLayout()).columnWidths = new int[]{0, 0, 0};
                    ((GridBagLayout) panel6.getLayout()).rowHeights = new int[]{100, 0, 0};
                    ((GridBagLayout) panel6.getLayout()).columnWeights = new double[]{0.0, 1.0, 1.0E-4};
                    ((GridBagLayout) panel6.getLayout()).rowWeights = new double[]{0.0, 1.0, 1.0E-4};

                    //======== panel15 ========
                    {
                        panel15.setLayout(new GridBagLayout());
                        ((GridBagLayout) panel15.getLayout()).columnWidths = new int[]{141, 0};
                        ((GridBagLayout) panel15.getLayout()).rowHeights = new int[]{0, 0};
                        ((GridBagLayout) panel15.getLayout()).columnWeights = new double[]{0.0, 1.0E-4};
                        ((GridBagLayout) panel15.getLayout()).rowWeights = new double[]{1.0, 1.0E-4};
                    }
                    panel6.add(panel15, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));

                    //======== panel16 ========
                    {
                        panel16.setLayout(new GridBagLayout());
                        ((GridBagLayout) panel16.getLayout()).columnWidths = new int[]{0, 0, 0};
                        ((GridBagLayout) panel16.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0};
                        ((GridBagLayout) panel16.getLayout()).columnWeights = new double[]{0.0, 1.0, 1.0E-4};
                        ((GridBagLayout) panel16.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0E-4};

                        //---- label1 ----
                        label1.setText("Title:");
                        panel16.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));
                        panel16.add(jlbl_title, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));

                        //---- label2 ----
                        label2.setText("Version:");
                        panel16.add(label2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));
                        panel16.add(jlbl_version, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));

                        //---- label3 ----
                        label3.setText("Author:");
                        panel16.add(label3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));
                        panel16.add(jlbl_author, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));

                        //---- label4 ----
                        label4.setText("Tags:");
                        panel16.add(label4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 0, 5), 0, 0));
                        panel16.add(jlbl_tags, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 0), 0, 0));
                    }
                    panel6.add(panel16, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));

                    //======== scrollPane1 ========
                    {

                        //---- jta_mp_description ----
                        jta_mp_description.setEditable(false);
                        scrollPane1.setViewportView(jta_mp_description);
                    }
                    panel6.add(scrollPane1, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                }
                panel1.add(panel6, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            tabbedPane1.addTab("Marketplace", panel1);

            //======== panel2 ========
            {
                panel2.setLayout(new GridBagLayout());
                ((GridBagLayout) panel2.getLayout()).columnWidths = new int[]{245, 0, 0};
                ((GridBagLayout) panel2.getLayout()).rowHeights = new int[]{0, 0, 0};
                ((GridBagLayout) panel2.getLayout()).columnWeights = new double[]{0.0, 1.0, 1.0E-4};
                ((GridBagLayout) panel2.getLayout()).rowWeights = new double[]{0.0, 1.0, 1.0E-4};

                //======== panel4 ========
                {
                    panel4.setLayout(new GridBagLayout());
                    ((GridBagLayout) panel4.getLayout()).columnWidths = new int[]{0, 0, 0, 0};
                    ((GridBagLayout) panel4.getLayout()).rowHeights = new int[]{0, 0};
                    ((GridBagLayout) panel4.getLayout()).columnWeights = new double[]{1.0, 0.0, 0.0, 1.0E-4};
                    ((GridBagLayout) panel4.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};
                    panel4.add(jtf_plugin_search, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));
                    panel4.add(jcb_plugin_search_type, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                    //---- jbtn_plugin_search ----
                    jbtn_plugin_search.setText("Search");
                    panel4.add(jbtn_plugin_search, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                }
                panel2.add(panel4, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                //======== panel14 ========
                {
                    panel14.setBackground(new Color(102, 102, 102));
                    panel14.setLayout(new GridBagLayout());
                    ((GridBagLayout) panel14.getLayout()).columnWidths = new int[]{140, 0, 0};
                    ((GridBagLayout) panel14.getLayout()).rowHeights = new int[]{100, 0, 0};
                    ((GridBagLayout) panel14.getLayout()).columnWeights = new double[]{0.0, 1.0, 1.0E-4};
                    ((GridBagLayout) panel14.getLayout()).rowWeights = new double[]{0.0, 1.0, 1.0E-4};

                    //======== panel17 ========
                    {
                        panel17.setLayout(new GridBagLayout());
                        ((GridBagLayout) panel17.getLayout()).columnWidths = new int[]{141, 0};
                        ((GridBagLayout) panel17.getLayout()).rowHeights = new int[]{0, 0};
                        ((GridBagLayout) panel17.getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
                        ((GridBagLayout) panel17.getLayout()).rowWeights = new double[]{1.0, 1.0E-4};
                    }
                    panel14.add(panel17, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));

                    //======== panel18 ========
                    {
                        panel18.setLayout(new GridBagLayout());
                        ((GridBagLayout) panel18.getLayout()).columnWidths = new int[]{0, 0, 0};
                        ((GridBagLayout) panel18.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0};
                        ((GridBagLayout) panel18.getLayout()).columnWeights = new double[]{0.0, 1.0, 1.0E-4};
                        ((GridBagLayout) panel18.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0E-4};

                        //---- label5 ----
                        label5.setText("Title:");
                        panel18.add(label5, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));
                        panel18.add(jlbl_title2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));

                        //---- label6 ----
                        label6.setText("Version:");
                        panel18.add(label6, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));
                        panel18.add(jlbl_version2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));

                        //---- label7 ----
                        label7.setText("Author:");
                        panel18.add(label7, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));
                        panel18.add(jlbl_author2, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));

                        //---- label8 ----
                        label8.setText("Tags:");
                        panel18.add(label8, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 0, 5), 0, 0));
                        panel18.add(jlbl_tags2, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 0), 0, 0));
                    }
                    panel14.add(panel18, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));

                    //======== scrollPane4 ========
                    {

                        //---- jta_installed_description ----
                        jta_installed_description.setEditable(false);
                        scrollPane4.setViewportView(jta_installed_description);
                    }
                    panel14.add(scrollPane4, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                }
                panel2.add(panel14, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //======== scrollPane3 ========
                {

                    //======== jpnl_plugins_mp2 ========
                    {
                        jpnl_plugins_mp2.setLayout(new VerticalLayout());
                    }
                    scrollPane3.setViewportView(jpnl_plugins_mp2);
                }
                panel2.add(scrollPane3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));
            }
            tabbedPane1.addTab("Installed", panel2);
        }
        contentPane.add(tabbedPane1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JTabbedPane tabbedPane1;
    private JPanel panel1;
    private JPanel panel3;
    private JTextField jtf_marketplace_search;
    private JComboBox jcb_mpst;
    private JButton jbtn_marketplace_search;
    private JScrollPane scrollPane2;
    private JPanel jpnl_plugins_mp;
    private JPanel panel6;
    private JPanel panel15;
    private JPanel panel16;
    private JLabel label1;
    private JLabel jlbl_title;
    private JLabel label2;
    private JLabel jlbl_version;
    private JLabel label3;
    private JLabel jlbl_author;
    private JLabel label4;
    private JLabel jlbl_tags;
    private JScrollPane scrollPane1;
    private JTextArea jta_mp_description;
    private JPanel panel2;
    private JPanel panel4;
    private JTextField jtf_plugin_search;
    private JComboBox jcb_plugin_search_type;
    private JButton jbtn_plugin_search;
    private JPanel panel14;
    private JPanel panel17;
    private JPanel panel18;
    private JLabel label5;
    private JLabel jlbl_title2;
    private JLabel label6;
    private JLabel jlbl_version2;
    private JLabel label7;
    private JLabel jlbl_author2;
    private JLabel label8;
    private JLabel jlbl_tags2;
    private JScrollPane scrollPane4;
    private JTextArea jta_installed_description;
    private JScrollPane scrollPane3;
    private JPanel jpnl_plugins_mp2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    public JPanel getJPNL_plugins_mp() {
        return jpnl_plugins_mp;
    }

    public JTextField getJtf_marketplace_search() {
        return jtf_marketplace_search;
    }

    public JComboBox getJcb_mpst() {
        return jcb_mpst;
    }

    public JButton getJbtn_marketplace_search() {
        return jbtn_marketplace_search;
    }

    public JPanel getJpnl_plugins_mp() {
        return jpnl_plugins_mp;
    }

    public JLabel getJlbl_title() {
        return jlbl_title;
    }

    public JLabel getJlbl_version() {
        return jlbl_version;
    }

    public JLabel getJlbl_author() {
        return jlbl_author;
    }

    public JLabel getJlbl_tags() {
        return jlbl_tags;
    }

    public JTextField getJtf_plugin_search() {
        return jtf_plugin_search;
    }

    public JComboBox getJcb_plugin_search_type() {
        return jcb_plugin_search_type;
    }

    public JButton getJbtn_plugin_search() {
        return jbtn_plugin_search;
    }

    public JLabel getJlbl_title2() {
        return jlbl_title2;
    }

    public JLabel getJlbl_version2() {
        return jlbl_version2;
    }

    public JLabel getJlbl_author2() {
        return jlbl_author2;
    }

    public JLabel getJlbl_tags2() {
        return jlbl_tags2;
    }

    public JPanel getJpnl_plugins_mp2() {
        return jpnl_plugins_mp2;
    }

    public JTextArea getJta_mp_description() {
        return jta_mp_description;
    }

    public JTextArea getJta_installed_description() {
        return jta_installed_description;
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }
}

class MarketplaceUIUpdater extends UIWorker {
    public MarketplaceUIUpdater(Runnable uiUpdateAction, int millis) {
        super(uiUpdateAction, millis);
    }
}