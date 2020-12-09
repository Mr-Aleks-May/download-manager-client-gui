package com.mraleksmay.projects.download_manager.client.gui.core.view.dialog.category;

import com.mraleksmay.projects.download_manager.client.gui.core.impl.plugin.common.model.category.CommonCategory;
import com.mraleksmay.projects.download_manager.client.gui.core.manager.ApplicationManager;
import com.mraleksmay.projects.download_manager.common.event.ListDataAdapter;
import com.mraleksmay.projects.download_manager.common.exception.ThreadAlreadyStartException;
import com.mraleksmay.projects.download_manager.common.exception.ThreadAlreadyStopException;
import com.mraleksmay.projects.download_manager.common.model.category.Category;
import com.mraleksmay.projects.download_manager.common.model.group.Group;
import com.mraleksmay.projects.download_manager.common.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.common.view.ADialog;
import com.mraleksmay.projects.download_manager.common.view.DialogResult;
import com.mraleksmay.projects.download_manager.common.view.UIWorker;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.ListDataEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class AddCategoryDialog extends ADialog {
    private String previousValueStr = "";
    private volatile boolean isUserChangeId = false;
    private ApplicationManager applicationManager;
    private UIWorker uiUpdater;

    public AddCategoryDialog(Window owner, ApplicationManager applicationManager) {
        super(owner);
        this.applicationManager = applicationManager;
        initComponents();

        uiUpdater = new AddCategoryDialogUIWorker(() -> {
            String currentValueStr = jtf_id.getText().trim().toUpperCase();

            if (isUserChangeId && !previousValueStr.equals(currentValueStr)) {
                previousValueStr = currentValueStr;

                SwingUtilities.invokeLater(() -> {
                    String idStr = currentValueStr;
                    jtf_id.setText(idStr);

                    isUserChangeId = false;
                });
            }
        }, 1000);
    }


    @Override
    public void init() {
        displayAllPlugins(getApplicationManager().getPluginsManager().getPlugins());
        displayGroupsInActiveLocalGroup(getApplicationManager().getPluginsManager().getActivePlugin().getGroups());
    }

    @Override
    public DialogResult showDialog() {
        ADialog dialog = this;
        dialog.setTitle("Add new category");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(new Dimension(600, 300));
        dialog.setPreferredSize(new Dimension(600, 300));
        dialog.pack();
        dialog.setModal(true);

        try {
            uiUpdater.start();
        } catch (ThreadAlreadyStartException threadAlreadyStartException) {
        }

        dialog.setVisible(true);
        return getResult();
    }


    private void displayAllPlugins(List<Plugin> plugins) {
        final JComboBox comboBox = this.jcb_localGroup;
        final DefaultComboBoxModel cbModel = (DefaultComboBoxModel) comboBox.getModel();

        cbModel.removeAllElements();

        for (Plugin plugin : plugins) {
            cbModel.addElement(plugin);
        }

        final Plugin activePlugin = getApplicationManager().getPluginsManager().getActivePlugin();
        comboBox.setSelectedItem(activePlugin);

        cbModel.addListDataListener(new ListDataAdapter() {
            @Override
            public void contentsChanged(ListDataEvent listDataEvent) {
                jcb_localGroupContentsChanged(listDataEvent);
            }
        });
    }

    private void displayGroupsInActiveLocalGroup(List<Group> groups) {
        final JComboBox comboBox = this.jcb_groups;
        final DefaultComboBoxModel cbModel = (DefaultComboBoxModel) comboBox.getModel();

        cbModel.removeAllElements();

        for (Group group : groups) {
            cbModel.addElement(group);
        }

        final Group activeGroup = getApplicationManager().getPluginsManager().getActiveGroup();
        comboBox.setSelectedItem(activeGroup);

        cbModel.addListDataListener(new ListDataAdapter() {
            @Override
            public void contentsChanged(ListDataEvent listDataEvent) {
                jcb_groupContentsChanged(listDataEvent);
            }
        });
    }


    private void jcb_localGroupContentsChanged(ListDataEvent listDataEvent) {
    }

    private void jcb_groupContentsChanged(ListDataEvent listDataEvent) {
    }

    private void jtf_idCaretUpdate(CaretEvent e) {
        isUserChangeId = true;
    }

    private void jbtn_choosePathToOutputDirActionPerformed(ActionEvent e) {
        try {
            String currentOutputPathStr = jtf_outputDir.getText();
            File currentOutputPath = new File(currentOutputPathStr).getCanonicalFile();

            if (!currentOutputPath.exists()) {
                String userHomeStr = System.getenv("HOME");

                currentOutputPathStr = userHomeStr;
                currentOutputPath = new File(currentOutputPathStr).getCanonicalFile();
                jtf_outputDir.setText(userHomeStr);
            }

            final JFileChooser directoryChooser = new JFileChooser(currentOutputPath);
            directoryChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            directoryChooser.setAcceptAllFileFilterUsed(false);
            final int result = directoryChooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = directoryChooser.getSelectedFile().getCanonicalFile();
                currentOutputPath = selectedFile.getCanonicalFile();

                jtf_outputDir.setText(currentOutputPath.getCanonicalPath());
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", ioException.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jbtn_addActionPerformed(ActionEvent e) {
        try {
            Plugin plugin = (Plugin) jcb_localGroup.getSelectedItem();
            Group group = (Group) jcb_groups.getSelectedItem();

            String idStr = jtf_id.getText().trim();
            String nameStr = jtf_name.getText().trim();
            String contentTypeStr = jtf_contentType.getText().trim();
            String extensionsStr = jtf_extensions.getText().trim();
            String outputDirStr = jtf_outputDir.getText().trim();
            File outputDir = new File(outputDirStr).getCanonicalFile();
            String commentStr = jta_comments.getText().trim();

            Category category = new CommonCategory(idStr, nameStr, contentTypeStr, extensionsStr, outputDir, commentStr, plugin);
            group.add(category);

            for (Group gr0up : plugin.getGroups()) {
                if (!gr0up.equals(group)) {
                    gr0up.add(category);
                }
            }

            this.uiUpdater.stop();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            JOptionPane.showMessageDialog(this, ioException.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ThreadAlreadyStopException threadAlreadyStopException) {
        }

        setResult(DialogResult.OK);
        this.dispose();
    }

    private void jbtn_cancelActionPerformed(ActionEvent e) {
        try {
            this.uiUpdater.stop();
        } catch (ThreadAlreadyStopException threadAlreadyStopException) {
        }

        setResult(DialogResult.CANCEL);
        this.dispose();
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        panel1 = new JPanel();
        panel2 = new JPanel();
        label1 = new JLabel();
        label3 = new JLabel();
        jtf_id = new JTextField();
        jcb_localGroup = new JComboBox();
        label4 = new JLabel();
        label2 = new JLabel();
        jtf_name = new JTextField();
        jcb_groups = new JComboBox();
        label5 = new JLabel();
        label7 = new JLabel();
        jtf_contentType = new JTextField();
        scrollPane1 = new JScrollPane();
        jtf_extensions = new JTextArea();
        label6 = new JLabel();
        scrollPane2 = new JScrollPane();
        jta_comments = new JTextArea();
        label8 = new JLabel();
        jtf_outputDir = new JTextField();
        jbtn_choosePathToOutputDir = new JButton();
        jbtn_add = new JButton();
        jbtn_cancel = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setTitle("Add new category");
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout) contentPane.getLayout()).columnWidths = new int[]{0, 0};
        ((GridBagLayout) contentPane.getLayout()).rowHeights = new int[]{0, 0};
        ((GridBagLayout) contentPane.getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
        ((GridBagLayout) contentPane.getLayout()).rowWeights = new double[]{1.0, 1.0E-4};

        //======== panel1 ========
        {
            panel1.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.
                    border.EmptyBorder(0, 0, 0, 0), "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax.swing.border.TitledBorder.CENTER
                    , javax.swing.border.TitledBorder.BOTTOM, new Font("D\u0069alog", Font
                    .BOLD, 12), Color.red), panel1.getBorder()));
            panel1.addPropertyChangeListener(
                    new java.beans.PropertyChangeListener() {
                        @Override
                        public void propertyChange(java.beans.PropertyChangeEvent e) {
                            if ("\u0062order"
                                    .equals(e.getPropertyName())) throw new RuntimeException();
                        }
                    });
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout) panel1.getLayout()).columnWidths = new int[]{0, 0, 0};
            ((GridBagLayout) panel1.getLayout()).rowHeights = new int[]{0, 0, 0};
            ((GridBagLayout) panel1.getLayout()).columnWeights = new double[]{1.0, 1.0, 1.0E-4};
            ((GridBagLayout) panel1.getLayout()).rowWeights = new double[]{1.0, 0.0, 1.0E-4};

            //======== panel2 ========
            {
                panel2.setLayout(new GridBagLayout());
                ((GridBagLayout) panel2.getLayout()).columnWidths = new int[]{0, 0, 0, 0, 0};
                ((GridBagLayout) panel2.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                ((GridBagLayout) panel2.getLayout()).columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 1.0E-4};
                ((GridBagLayout) panel2.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0E-4};

                //---- label1 ----
                label1.setText("Category id:");
                panel2.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- label3 ----
                label3.setText("Local group:");
                panel2.add(label3, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- jtf_id ----
                jtf_id.addCaretListener(e -> jtf_idCaretUpdate(e));
                panel2.add(jtf_id, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                panel2.add(jcb_localGroup, new GridBagConstraints(2, 1, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- label4 ----
                label4.setText("Name:");
                panel2.add(label4, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- label2 ----
                label2.setText("Groups:");
                panel2.add(label2, new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                panel2.add(jtf_name, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                panel2.add(jcb_groups, new GridBagConstraints(2, 3, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- label5 ----
                label5.setText("Content type:");
                panel2.add(label5, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- label7 ----
                label7.setText("File extensions:");
                panel2.add(label7, new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                panel2.add(jtf_contentType, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //======== scrollPane1 ========
                {

                    //---- jtf_extensions ----
                    jtf_extensions.setWrapStyleWord(true);
                    scrollPane1.setViewportView(jtf_extensions);
                }
                panel2.add(scrollPane1, new GridBagConstraints(2, 5, 2, 3, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- label6 ----
                label6.setText("Comments:");
                panel2.add(label6, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //======== scrollPane2 ========
                {

                    //---- jta_comments ----
                    jta_comments.setWrapStyleWord(true);
                    scrollPane2.setViewportView(jta_comments);
                }
                panel2.add(scrollPane2, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- label8 ----
                label8.setText("Download to directory:");
                panel2.add(label8, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                panel2.add(jtf_outputDir, new GridBagConstraints(0, 9, 3, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- jbtn_choosePathToOutputDir ----
                jbtn_choosePathToOutputDir.setText("...");
                jbtn_choosePathToOutputDir.addActionListener(e -> jbtn_choosePathToOutputDirActionPerformed(e));
                panel2.add(jbtn_choosePathToOutputDir, new GridBagConstraints(3, 9, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            panel1.add(panel2, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- jbtn_add ----
            jbtn_add.setText("Add");
            jbtn_add.addActionListener(e -> jbtn_addActionPerformed(e));
            panel1.add(jbtn_add, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- jbtn_cancel ----
            jbtn_cancel.setText("Cancel");
            jbtn_cancel.addActionListener(e -> jbtn_cancelActionPerformed(e));
            panel1.add(jbtn_cancel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel panel1;
    private JPanel panel2;
    private JLabel label1;
    private JLabel label3;
    private JTextField jtf_id;
    private JComboBox jcb_localGroup;
    private JLabel label4;
    private JLabel label2;
    private JTextField jtf_name;
    private JComboBox jcb_groups;
    private JLabel label5;
    private JLabel label7;
    private JTextField jtf_contentType;
    private JScrollPane scrollPane1;
    private JTextArea jtf_extensions;
    private JLabel label6;
    private JScrollPane scrollPane2;
    private JTextArea jta_comments;
    private JLabel label8;
    private JTextField jtf_outputDir;
    private JButton jbtn_choosePathToOutputDir;
    private JButton jbtn_add;
    private JButton jbtn_cancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    public String getPreviousValueStr() {
        return previousValueStr;
    }

    public boolean isUserChangeId() {
        return isUserChangeId;
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }

    public UIWorker getUiUpdater() {
        return uiUpdater;
    }
}

class AddCategoryDialogUIWorker extends UIWorker {
    public AddCategoryDialogUIWorker(Runnable uiUpdateAction, int millis) {
        super(uiUpdateAction, millis);
    }
}
