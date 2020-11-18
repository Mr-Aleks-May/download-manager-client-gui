package com.mralexmay.projects.download_manager.view.dialogs.category;

import com.mralexmay.projects.download_manager.controller.LocalGroupsController;
import com.mralexmay.projects.download_manager.core.events.ListDataAdapter;
import com.mralexmay.projects.download_manager.core.exception.UpdateThreadAlreadyRunningException;
import com.mralexmay.projects.download_manager.core.exception.UpdateThreadAlreadyStopException;
import com.mralexmay.projects.download_manager.core.model.download.category.Category;
import com.mralexmay.projects.download_manager.core.model.download.group.Group;
import com.mralexmay.projects.download_manager.core.model.download.local_group.LocalGroup;
import com.mralexmay.projects.download_manager.core.view.dialog.AJDialog;
import com.mralexmay.projects.download_manager.core.view.dialog.DialogResult;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import javax.swing.event.*;

public class EditCategoryDialog extends AJDialog {
    private LocalGroupsController localGroupsController;
    private volatile boolean isUserChangeId = false;
    private volatile boolean isUserChangePluginId = false;
    private volatile boolean isUserChangeGroupId = false;
    private volatile boolean isUserChangeCategoryId = false;
    private UIUpdater uiUpdater;


    public EditCategoryDialog(Window owner, LocalGroupsController localGroupsController) {
        super(owner);
        this.localGroupsController = localGroupsController;
        initComponents();

        postInit();

        uiUpdater = new UIUpdater();
        try {
            uiUpdater.start();
        } catch (UpdateThreadAlreadyRunningException e) {
            e.printStackTrace();
        }
    }


    private void postInit() {
        try {
            LocalGroup localGroup = getLocalGroupsController().getActiveLocalGroup();
            Group group = getLocalGroupsController().getActiveGroup();
            Category category = getLocalGroupsController().getActiveCategory();

            displayAllLocalGroups(getLocalGroupsController().getLocalGroups());
            displayGroupsInActiveLocalGroup(localGroup.getGroups());
            displayCategoriesInActiveGroup(group.getCategories());

            updateFields(category);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void updateFields(Category category) throws IOException {
        jtf_id.setText(category.getId().toUpperCase());
        jtf_newName.setText(category.getName());
        jtf_contentType.setText(category.getContentType());
        jta_extensions.setText(category.getExtensions());
        jtf_outputDir.setText(category.getOutputDir().getCanonicalPath());
        jta_comments.setText(category.getComments());
    }

    private void displayAllLocalGroups(java.util.List<LocalGroup> localGroups) {
        final JComboBox comboBox = this.jcb_localGroup;
        final DefaultComboBoxModel cbModel = (DefaultComboBoxModel) comboBox.getModel();

        cbModel.removeAllElements();

        for (LocalGroup localGroup : localGroups) {
            cbModel.addElement(localGroup);
        }

        final LocalGroup defaultLocalGroup = getLocalGroupsController().getActiveLocalGroup();
        comboBox.setSelectedItem(defaultLocalGroup);

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

        final Group defaultGroup = getLocalGroupsController().getActiveGroup();
        comboBox.setSelectedItem(defaultGroup);

        cbModel.addListDataListener(new ListDataAdapter() {
            @Override
            public void contentsChanged(ListDataEvent listDataEvent) {
                jcb_groupContentsChanged(listDataEvent);
            }
        });
    }

    public void displayCategoriesInActiveGroup(List<Category> categories) {
        final JComboBox comboBox = this.jcb_categories;
        final DefaultComboBoxModel cbModel = (DefaultComboBoxModel) comboBox.getModel();

        cbModel.removeAllElements();

        for (Category category : categories) {
            if (!category.getId().equals("ALL")) {
                cbModel.addElement(category);
            }
        }

        final Category defaultCategory = getLocalGroupsController().getActiveCategory();
        comboBox.setSelectedItem(defaultCategory);

        cbModel.addListDataListener(new ListDataAdapter() {
            @Override
            public void contentsChanged(ListDataEvent listDataEvent) {
                jcb_categoryContentsChanged(listDataEvent);
            }
        });
    }


    private void jcb_localGroupContentsChanged(ListDataEvent listDataEvent) {
        isUserChangePluginId = true;
    }

    private void jcb_groupContentsChanged(ListDataEvent listDataEvent) {
        isUserChangeGroupId = true;
    }

    private void jcb_categoryContentsChanged(ListDataEvent listDataEvent) {
        isUserChangeCategoryId = true;
    }

    private void jtf_idCaretUpdate(CaretEvent e) {
        isUserChangeId = true;
    }

    private void jbtn_saveActionPerformed(ActionEvent e) {
        try {
            Category category = (Category) jcb_categories.getModel().getSelectedItem();

            String idStr = jtf_id.getText().trim().toUpperCase();
            String nameStr = jtf_newName.getText().trim().trim();
            String contentTypeStr = jtf_contentType.getText().trim();
            String extensionsStr = jta_extensions.getText().trim();
            String outputDirStr = jtf_outputDir.getText().trim();
            File outputDir = new File(outputDirStr).getCanonicalFile();
            String commentStr = jta_comments.getText().trim();

            category.setId(idStr);
            category.setName(nameStr);
            category.setContentType(contentTypeStr);
            category.setExtensions(extensionsStr);
            category.setOutputDir(outputDir);
            category.setComments(commentStr);

            this.setResult(DialogResult.OK);
            this.dispose();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void btn_cancelActionPerformed(ActionEvent e) {
        try {
            this.uiUpdater.stop();
        } catch (UpdateThreadAlreadyStopException updateThreadAlreadyStopException) {
            updateThreadAlreadyStopException.printStackTrace();
        }

        setResult(DialogResult.CANCEL);
        this.dispose();
    }


    private void jbtn_choosePathToOutputDirActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        panel1 = new JPanel();
        panel2 = new JPanel();
        label3 = new JLabel();
        label4 = new JLabel();
        jtf_id = new JTextField();
        jcb_localGroup = new JComboBox();
        label5 = new JLabel();
        label6 = new JLabel();
        jtf_newName = new JTextField();
        jcb_groups = new JComboBox();
        label7 = new JLabel();
        label1 = new JLabel();
        jtf_contentType = new JTextField();
        jcb_categories = new JComboBox();
        scrollPane1 = new JScrollPane();
        jta_extensions = new JTextArea();
        label9 = new JLabel();
        label8 = new JLabel();
        scrollPane2 = new JScrollPane();
        jta_comments = new JTextArea();
        label10 = new JLabel();
        jtf_outputDir = new JTextField();
        jbtn_choosePathToOutputDir = new JButton();
        jbtn_save = new JButton();
        btn_cancel = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout) contentPane.getLayout()).columnWidths = new int[]{0, 0};
        ((GridBagLayout) contentPane.getLayout()).rowHeights = new int[]{0, 0};
        ((GridBagLayout) contentPane.getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
        ((GridBagLayout) contentPane.getLayout()).rowWeights = new double[]{1.0, 1.0E-4};

        //======== panel1 ========
        {
            panel1.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border
                    .EmptyBorder(0, 0, 0, 0), "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e", javax.swing.border.TitledBorder.CENTER, javax
                    .swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialo\u0067", java.awt.Font.BOLD,
                    12), java.awt.Color.red), panel1.getBorder()));
            panel1.addPropertyChangeListener(new java.beans
                    .PropertyChangeListener() {
                @Override
                public void propertyChange(java.beans.PropertyChangeEvent e) {
                    if ("borde\u0072".equals(e.
                            getPropertyName())) throw new RuntimeException();
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

                //---- label3 ----
                label3.setText("Category id:");
                panel2.add(label3, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- label4 ----
                label4.setText("Local group:");
                panel2.add(label4, new GridBagConstraints(2, 0, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- jtf_id ----
                jtf_id.setEditable(false);
                jtf_id.setForeground(Color.white);
                jtf_id.setBackground(new Color(153, 153, 153));
                jtf_id.addCaretListener(e -> jtf_idCaretUpdate(e));
                panel2.add(jtf_id, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                panel2.add(jcb_localGroup, new GridBagConstraints(2, 1, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- label5 ----
                label5.setText("Name:");
                panel2.add(label5, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- label6 ----
                label6.setText("Groups:");
                panel2.add(label6, new GridBagConstraints(2, 2, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                panel2.add(jtf_newName, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                panel2.add(jcb_groups, new GridBagConstraints(2, 3, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- label7 ----
                label7.setText("Content type:");
                panel2.add(label7, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- label1 ----
                label1.setText("Category:");
                panel2.add(label1, new GridBagConstraints(2, 4, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                panel2.add(jtf_contentType, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                panel2.add(jcb_categories, new GridBagConstraints(2, 5, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //======== scrollPane1 ========
                {

                    //---- jta_extensions ----
                    jta_extensions.setWrapStyleWord(true);
                    scrollPane1.setViewportView(jta_extensions);
                }
                panel2.add(scrollPane1, new GridBagConstraints(2, 7, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- label9 ----
                label9.setText("Comments:");
                panel2.add(label9, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- label8 ----
                label8.setText("File extensions:");
                panel2.add(label8, new GridBagConstraints(2, 6, 2, 1, 0.0, 0.0,
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

                //---- label10 ----
                label10.setText("Download to directory:");
                panel2.add(label10, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
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
                    new Insets(0, 0, 5, 0), 0, 0));

            //---- jbtn_save ----
            jbtn_save.setText("Save");
            jbtn_save.addActionListener(e -> jbtn_saveActionPerformed(e));
            panel1.add(jbtn_save, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- btn_cancel ----
            btn_cancel.setText("Cancel");
            btn_cancel.addActionListener(e -> btn_cancelActionPerformed(e));
            panel1.add(btn_cancel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
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
    private JLabel label3;
    private JLabel label4;
    private JTextField jtf_id;
    private JComboBox jcb_localGroup;
    private JLabel label5;
    private JLabel label6;
    private JTextField jtf_newName;
    private JComboBox jcb_groups;
    private JLabel label7;
    private JLabel label1;
    private JTextField jtf_contentType;
    private JComboBox jcb_categories;
    private JScrollPane scrollPane1;
    private JTextArea jta_extensions;
    private JLabel label9;
    private JLabel label8;
    private JScrollPane scrollPane2;
    private JTextArea jta_comments;
    private JLabel label10;
    private JTextField jtf_outputDir;
    private JButton jbtn_choosePathToOutputDir;
    private JButton jbtn_save;
    private JButton btn_cancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    public LocalGroupsController getLocalGroupsController() {
        return localGroupsController;
    }


    private class UIUpdater {
        private Thread backgroundThread;
        private final int sleep = 500;
        private String previousValueStr = "";
        private LocalGroup previousPlugin;
        private Group previousGroup;
        private Category previousCategory;


        public UIUpdater() {
        }


        protected synchronized void start() throws UpdateThreadAlreadyRunningException {
            if (backgroundThread != null && backgroundThread.isAlive()) {
                throw new UpdateThreadAlreadyRunningException();
            }

            this.backgroundThread = new Thread(() -> {
                while (true) {
                    String currentValueStr = jtf_id.getText().trim().toUpperCase();
                    LocalGroup currentPlugin = (LocalGroup) jcb_localGroup.getModel().getSelectedItem();
                    Group currentGroup = (Group) jcb_groups.getModel().getSelectedItem();
                    Category currentCategory = (Category) jcb_categories.getModel().getSelectedItem();

                    try {
                        if (isUserChangeId && !previousValueStr.equals(currentValueStr)) {
                            previousValueStr = currentValueStr;

                            SwingUtilities.invokeAndWait(() -> {
                                String idStr = currentValueStr;
                                jtf_id.setText(idStr);

                                isUserChangeId = false;
                            });
                        }
                        if (isUserChangePluginId && previousPlugin != currentPlugin) {
                            previousPlugin = currentPlugin;
                        }
                        if (isUserChangeGroupId && previousGroup != currentGroup) {
                            previousGroup = currentGroup;
                        }
                        if (isUserChangeCategoryId && previousCategory != currentCategory) {
                            previousCategory = currentCategory;

                            updateFields(currentCategory);
                        }

                        Thread.sleep(sleep);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });

            this.backgroundThread.start();
        }

        protected synchronized void stop() throws UpdateThreadAlreadyStopException {
            if (backgroundThread == null || !backgroundThread.isAlive()) {
                throw new UpdateThreadAlreadyStopException();
            }

            backgroundThread.stop();
        }
    }
}
