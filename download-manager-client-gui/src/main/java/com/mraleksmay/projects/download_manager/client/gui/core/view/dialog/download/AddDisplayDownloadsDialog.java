/*
 * Created by JFormDesigner on Tue Dec 08 15:06:04 EET 2020
 */

package com.mraleksmay.projects.download_manager.client.gui.core.view.dialog.download;

import java.awt.event.*;

import com.mraleksmay.projects.download_manager.client.gui.core.impl.model.download.DownloadEntityInformation;
import com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.download.CommonDownload;
import com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.download.CommonDownloadFormatter;
import com.mraleksmay.projects.download_manager.client.gui.core.manager.ApplicationManager;
import com.mraleksmay.projects.download_manager.client.gui.core.util.downloader.BasicNetWorker;
import com.mraleksmay.projects.download_manager.client.gui.core.view.dialog.download.controller.TableController;
import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.download.AuthenticationData;
import com.mraleksmay.projects.download_manager.common.model.download.Download;
import com.mraleksmay.projects.download_manager.common.view.ADialog;
import com.mraleksmay.projects.download_manager.common.view.DialogResult;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.swing.*;

/**
 * @author unknown
 */
public class AddDisplayDownloadsDialog extends ADialog {
    private ApplicationManager applicationManager;
    private TableController tableController;
    private List<String> urlsList;
    private List<DownloadEntityInformation> downloadsList;


    public AddDisplayDownloadsDialog(Window owner, ApplicationManager applicationManager) {
        super(owner);
        initComponents();

        this.applicationManager = applicationManager;
        this.tableController = new TableController(getJTBL_downloads(), getApplicationManager());
    }


    @Override
    public void init() {
        final List<String> urls = getUrlsList().stream().filter((urlStr) -> {
            final Pattern pattern = Pattern.compile("(http|https)?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
            final Matcher matcher = pattern.matcher(urlStr);

            if (matcher.find()) {
                return true;
            }

            return false;
        }).collect(Collectors.toList());

        downloadsList = getDownloadsFromUrls(urls);

        getTableController().initTable();
        getTableController().updateTable(downloadsList);
    }

    @Override
    public DialogResult showDialog() {
        final ADialog dialog = this;
        dialog.setTitle("Downloads Information");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(new Dimension(600, 250));
        dialog.setPreferredSize(new Dimension(600, 250));
        dialog.pack();
        dialog.setModal(true);

        dialog.setVisible(true);

        return this.getResult();
    }


    private List<DownloadEntityInformation> getDownloadsFromUrls(List<String> urlsList) {
        final List<DownloadEntityInformation> downloadsList = new ArrayList<>();

        for (String urlStr : urlsList) {
            String name;
            final URL url;
            AuthenticationData authData = null;
            final Category category;

            try {
                name = Download.suggestNameFrom(urlStr);
                if (name.length() > 100) name = name.substring(0, 100);
                url = new URL(urlStr);
                category = getApplicationManager().getPluginsManager().getCategoryBy("CORE", "UNFINISHED", "OTHER");

                final Download download = new CommonDownload(name, url, category.getOutputDir(), "file", authData, category, 0);
                download.setFormatter(new CommonDownloadFormatter());
                final DownloadEntityInformation downloadEntityInformation = new DownloadEntityInformation(download, new BasicNetWorker());
                downloadsList.add(downloadEntityInformation);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return downloadsList;
    }

    private void jbtn_addAllActionPerformed(ActionEvent e) {
        this.setResult(DialogResult.OK);
        this.dispose();
    }

    private void jbtn_cancelActionPerformed(ActionEvent e) {
        this.setResult(DialogResult.CANCEL);
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        jtbl_downloads = new JTable();
        panel2 = new JPanel();
        jbtn_addAll = new JButton();
        jbtn_cancel = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout) contentPane.getLayout()).columnWidths = new int[]{0, 0};
        ((GridBagLayout) contentPane.getLayout()).rowHeights = new int[]{0, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
        ((GridBagLayout) contentPane.getLayout()).rowWeights = new double[]{1.0, 0.0, 1.0E-4};

        //======== panel1 ========
        {
            panel1.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax
                    .swing.border.EmptyBorder(0, 0, 0, 0), "JF\u006frm\u0044es\u0069gn\u0065r \u0045va\u006cua\u0074io\u006e", javax.swing
                    .border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java.awt.
                    Font("D\u0069al\u006fg", java.awt.Font.BOLD, 12), java.awt.Color.red
            ), panel1.getBorder()));
            panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                @Override
                public void propertyChange(java.beans.PropertyChangeEvent e) {
                    if ("\u0062or\u0064er".equals(e.getPropertyName(
                    ))) throw new RuntimeException();
                }
            });
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout) panel1.getLayout()).columnWidths = new int[]{0, 0};
            ((GridBagLayout) panel1.getLayout()).rowHeights = new int[]{0, 0};
            ((GridBagLayout) panel1.getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
            ((GridBagLayout) panel1.getLayout()).rowWeights = new double[]{1.0, 1.0E-4};

            //======== scrollPane1 ========
            {
                scrollPane1.setViewportView(jtbl_downloads);
            }
            panel1.add(scrollPane1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

        //======== panel2 ========
        {
            panel2.setLayout(new GridBagLayout());
            ((GridBagLayout) panel2.getLayout()).columnWidths = new int[]{0, 0, 0};
            ((GridBagLayout) panel2.getLayout()).rowHeights = new int[]{0, 0};
            ((GridBagLayout) panel2.getLayout()).columnWeights = new double[]{1.0, 1.0, 1.0E-4};
            ((GridBagLayout) panel2.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};

            //---- jbtn_addAll ----
            jbtn_addAll.setText("Add all");
            jbtn_addAll.addActionListener(e -> jbtn_addAllActionPerformed(e));
            panel2.add(jbtn_addAll, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- jbtn_cancel ----
            jbtn_cancel.setText("Cancel");
            jbtn_cancel.addActionListener(e -> jbtn_cancelActionPerformed(e));
            panel2.add(jbtn_cancel, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }

    public TableController getTableController() {
        return tableController;
    }

    public List<String> getUrlsList() {
        return urlsList;
    }

    public void setUrlsList(List<String> urlsList) {
        this.urlsList = urlsList;
    }

    public List<DownloadEntityInformation> getDownloadsList() {
        return downloadsList;
    }

    public JTable getJTBL_downloads() {
        return jtbl_downloads;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel panel1;
    private JScrollPane scrollPane1;
    private JTable jtbl_downloads;
    private JPanel panel2;
    private JButton jbtn_addAll;
    private JButton jbtn_cancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
