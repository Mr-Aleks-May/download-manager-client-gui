package com.mraleksmay.projects.download_manager.client.gui.core.view.dialog.download;

import com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.download.Base64AuthenticationData;
import com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.download.CommonDownload;
import com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.download.CommonDownloadFormatter;
import com.mraleksmay.projects.download_manager.client.gui.core.manager.ApplicationManager;
import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.download.AuthenticationData;
import com.mraleksmay.projects.download_manager.common.model.download.Download;
import com.mraleksmay.projects.download_manager.common.view.ADialog;
import com.mraleksmay.projects.download_manager.common.view.DialogResult;
import com.mraleksmay.projects.download_manager.common.view.DownloadDialog;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddDownloadFromUrlDialog extends ADialog implements DownloadDialog {
    private Download download;
    private ApplicationManager applicationManager;


    public AddDownloadFromUrlDialog(Window owner, ApplicationManager applicationManager) {
        super(owner);
        this.download = null;
        this.applicationManager = applicationManager;
        initComponents();
        setUrlFromClipboard();
    }

    @Override
    public void init() {
    }

    @Override
    public DialogResult showDialog() {
        ADialog dialog = this;
        dialog.setTitle("Download Information");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(new Dimension(600, 250));
        dialog.setPreferredSize(new Dimension(600, 250));
        dialog.pack();
        dialog.setVisible(true);

        return this.getResult();
    }

    private void setUrlFromClipboard() {
        jcb_authRequired.setSelected(false);
        jtf_login.setEnabled(false);
        jtf_password.setEnabled(false);

        try {
            final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            final String text = clipboard.getData(DataFlavor.stringFlavor).toString();

            final Pattern pattern = Pattern.compile("(http|https)?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
            final Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                jtf_url.setText(text);
            }
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void jcb_authRequiredStateChanged(ChangeEvent e) {
        final boolean isAuthRequired = jcb_authRequired.isSelected();

        if (isAuthRequired) {
            jtf_login.setEnabled(true);
            jtf_password.setEnabled(true);
        } else {
            jtf_login.setEnabled(false);
            jtf_password.setEnabled(false);
        }
    }


    private void jbtn_downloadLaterActionPerformed(ActionEvent e) {
        final String urlStr = jtf_url.getText();
        final String loginStr = jtf_login.getText();
        final String passwordStr = jtf_password.getText();
        final boolean isAuthRequired = jcb_authRequired.isSelected();

        String name;
        final URL url;
        AuthenticationData authData = null;
        final Category category;

        try {
            name = Download.suggestNameFrom(urlStr);
            if (name.length() > 100) name = name.substring(0, 100);
            url = new URL(urlStr);
            if (isAuthRequired == true) authData = new Base64AuthenticationData(loginStr, passwordStr);
            category = getApplicationManager().getPluginsManager().getCategoryBy("CORE", "UNFINISHED", "OTHER");

            this.download = new CommonDownload(name, url, category.getOutputDir(), "mp4", authData, category, 0);
            this.download.setFormatter(new CommonDownloadFormatter(this.download));

            this.setResult(DialogResult.LATER);
            this.dispose();
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
            JOptionPane.showMessageDialog(this, malformedURLException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            JOptionPane.showMessageDialog(this, ioException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jbtn_downloadNowActionPerformed(ActionEvent e) {
        final String urlStr = jtf_url.getText();
        final String loginStr = jtf_login.getText();
        final String passwordStr = jtf_password.getText();
        final boolean isAuthRequired = jcb_authRequired.isSelected();

        String name;
        final URL url;
        AuthenticationData authData = null;
        final Category category;

        try {
            name = Download.suggestNameFrom(urlStr);
            if (name.length() > 100) name = name.substring(0, 100);
            url = new URL(urlStr);
            if (isAuthRequired == true) authData = new Base64AuthenticationData(loginStr, passwordStr);
            category = getApplicationManager().getPluginsManager().getCategoryBy("CORE", "UNFINISHED", "OTHER");

            this.download = new CommonDownload(name, url, category.getOutputDir(), "mp4", authData, category, 0);
            download.setFormatter(new CommonDownloadFormatter(download));

            this.setResult(DialogResult.NOW);
            this.dispose();
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
            JOptionPane.showMessageDialog(this, malformedURLException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            JOptionPane.showMessageDialog(this, ioException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jbtn_cancelActionPerformed(ActionEvent e) {
        this.setResult(DialogResult.CANCEL);
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        label1 = new JLabel();
        jtf_url = new JTextField();
        panel1 = new JPanel();
        jcb_updateDownloadInformation = new JCheckBox();
        jcb_authRequired = new JCheckBox();
        label2 = new JLabel();
        label3 = new JLabel();
        jtf_login = new JTextField();
        jtf_password = new JTextField();
        jbtn_downloadLater = new JButton();
        jbtn_downloadNow = new JButton();
        jbtn_cancel = new JButton();

        //======== this ========
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        var contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout) contentPane.getLayout()).columnWidths = new int[]{0, 0, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).columnWeights = new double[]{1.0, 1.0, 1.0, 1.0E-4};
        ((GridBagLayout) contentPane.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0, 0.0, 1.0, 0.0, 1.0E-4};

        //---- label1 ----
        label1.setText("Url:");
        contentPane.add(label1, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 6, 6), 0, 0));
        contentPane.add(jtf_url, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0,
                GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 6, 0), 0, 0));

        //======== panel1 ========
        {
            panel1.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new
                    javax.swing.border.EmptyBorder(0, 0, 0, 0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn", javax
                    .swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java
                    .awt.Font("Dia\u006cog", java.awt.Font.BOLD, 12), java.awt
                    .Color.red), panel1.getBorder()));
            panel1.addPropertyChangeListener(new java.beans.
                    PropertyChangeListener() {
                @Override
                public void propertyChange(java.beans.PropertyChangeEvent e) {
                    if ("\u0062ord\u0065r".
                            equals(e.getPropertyName())) throw new RuntimeException();
                }
            });
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout) panel1.getLayout()).columnWidths = new int[]{0, 0, 0};
            ((GridBagLayout) panel1.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0};
            ((GridBagLayout) panel1.getLayout()).columnWeights = new double[]{1.0, 1.0, 1.0E-4};
            ((GridBagLayout) panel1.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0E-4};

            //---- jcb_updateDownloadInformation ----
            jcb_updateDownloadInformation.setText("Retrive download infomation");
            panel1.add(jcb_updateDownloadInformation, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

            //---- jcb_authRequired ----
            jcb_authRequired.setText("Auth required");
            jcb_authRequired.addChangeListener(e -> jcb_authRequiredStateChanged(e));
            panel1.add(jcb_authRequired, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

            //---- label2 ----
            label2.setText("Login:");
            panel1.add(label2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- label3 ----
            label3.setText("Password:");
            panel1.add(label3, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));
            panel1.add(jtf_login, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));
            panel1.add(jtf_password, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel1, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0,
                GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                new Insets(0, 0, 6, 0), 0, 0));

        //---- jbtn_downloadLater ----
        jbtn_downloadLater.setText("Download Later");
        jbtn_downloadLater.addActionListener(e -> jbtn_downloadLaterActionPerformed(e));
        contentPane.add(jbtn_downloadLater, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 6), 0, 0));

        //---- jbtn_downloadNow ----
        jbtn_downloadNow.setText("Download Now");
        jbtn_downloadNow.addActionListener(e -> jbtn_downloadNowActionPerformed(e));
        contentPane.add(jbtn_downloadNow, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 6), 0, 0));

        //---- jbtn_cancel ----
        jbtn_cancel.setText("Cancel");
        jbtn_cancel.addActionListener(e -> jbtn_cancelActionPerformed(e));
        contentPane.add(jbtn_cancel, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JLabel label1;
    private JTextField jtf_url;
    private JPanel panel1;
    private JCheckBox jcb_updateDownloadInformation;
    private JCheckBox jcb_authRequired;
    private JLabel label2;
    private JLabel label3;
    private JTextField jtf_login;
    private JTextField jtf_password;
    private JButton jbtn_downloadLater;
    private JButton jbtn_downloadNow;
    private JButton jbtn_cancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    @Override
    public void setDownloadInformation(Download downloadInformation) {

    }

    @Override
    public Download getDownload() {
        return download;
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }
}
