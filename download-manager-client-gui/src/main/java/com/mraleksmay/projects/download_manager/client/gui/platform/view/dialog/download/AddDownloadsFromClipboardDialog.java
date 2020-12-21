/*
 * Created by JFormDesigner on Mon Dec 07 17:40:34 EET 2020
 */

package com.mraleksmay.projects.download_manager.client.gui.platform.view.dialog.download;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.*;

import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;
import com.mraleksmay.projects.download_manager.common.view.ADialog;
import com.mraleksmay.projects.download_manager.common.view.DialogResult;

import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author unknown
 */
public class AddDownloadsFromClipboardDialog extends ADialog {
    private ApplicationManager applicationManager;
    private List<String> urlsList;

    public AddDownloadsFromClipboardDialog(Window owner, ApplicationManager applicationManager) {
        super(owner);
        this.applicationManager = applicationManager;
        this.urlsList = null;
        initComponents();
    }

    @Override
    public void init() {
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        try {
            final String text = clipboard.getData(DataFlavor.stringFlavor).toString();
            final StringBuilder sb = new StringBuilder();

            final List<String> urls = Arrays.asList(text.split("\n"));
            urls.stream().filter((urlStr) -> {
                final Pattern pattern = Pattern.compile("(http|https)?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
                final Matcher matcher = pattern.matcher(urlStr);

                if (matcher.find()) {
                    return true;
                }

                return false;
            }).forEach((urlStr) -> {
                sb.append(urlStr);
                sb.append(System.lineSeparator());
            });

            if (sb.length() > 0) {
                sb.delete(sb.length() - System.lineSeparator().length(), sb.length());
            }

            getJTA_urls().setText(sb.toString());
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DialogResult showDialog() {
        final ADialog dialog = this;
        dialog.setTitle("Add downloads");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final Dimension size = new Dimension(450, 300);
        dialog.setSize(size);
        dialog.setPreferredSize(size);
        dialog.setModal(true);
        dialog.pack();
        dialog.setVisible(true);

        return getResult();
    }

    private void jbtn_addAllActionPerformed(ActionEvent e) {
        List<String> urls = Arrays.asList(getJTA_urls().getText().split("\n"));
        urls = urls.stream().filter((urlStr) -> {
            final Pattern pattern = Pattern.compile("(http|https)?:\\/\\/(www\\.)?[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
            final Matcher matcher = pattern.matcher(urlStr);

            if (matcher.find()) {
                return true;
            }

            return false;
        }).collect(Collectors.toUnmodifiableList());

        this.urlsList = urls;

        setResult(DialogResult.OK);
        this.dispose();
    }

    private void jbtn_cancelActionPerformed(ActionEvent e) {
        setResult(DialogResult.CANCEL);
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        jta_urls = new JTextArea();
        jbtn_addAll = new JButton();
        jbtn_cancel = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout) contentPane.getLayout()).columnWidths = new int[]{0, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).rowHeights = new int[]{0, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).columnWeights = new double[]{1.0, 1.0, 1.0E-4};
        ((GridBagLayout) contentPane.getLayout()).rowWeights = new double[]{1.0, 0.0, 1.0E-4};

        //======== panel1 ========
        {
            panel1.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.
                    border.EmptyBorder(0, 0, 0, 0), "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e", javax.swing.border.TitledBorder.CENTER
                    , javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialo\u0067", java.awt.Font
                    .BOLD, 12), java.awt.Color.red), panel1.getBorder()));
            panel1.addPropertyChangeListener(
                    new java.beans.PropertyChangeListener() {
                        @Override
                        public void propertyChange(java.beans.PropertyChangeEvent e) {
                            if ("borde\u0072"
                                    .equals(e.getPropertyName())) throw new RuntimeException();
                        }
                    });
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout) panel1.getLayout()).columnWidths = new int[]{0, 0};
            ((GridBagLayout) panel1.getLayout()).rowHeights = new int[]{0, 0};
            ((GridBagLayout) panel1.getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
            ((GridBagLayout) panel1.getLayout()).rowWeights = new double[]{1.0, 1.0E-4};

            //======== scrollPane1 ========
            {
                scrollPane1.setViewportView(jta_urls);
            }
            panel1.add(scrollPane1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel1, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

        //---- jbtn_addAll ----
        jbtn_addAll.setText("Add all");
        jbtn_addAll.addActionListener(e -> jbtn_addAllActionPerformed(e));
        contentPane.add(jbtn_addAll, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

        //---- jbtn_cancel ----
        jbtn_cancel.setText("Cancel");
        jbtn_cancel.addActionListener(e -> jbtn_cancelActionPerformed(e));
        contentPane.add(jbtn_cancel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public JTextArea getJTA_urls() {
        return jta_urls;
    }

    public List<String> getUrlsList() {
        return urlsList;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel panel1;
    private JScrollPane scrollPane1;
    private JTextArea jta_urls;
    private JButton jbtn_addAll;
    private JButton jbtn_cancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
