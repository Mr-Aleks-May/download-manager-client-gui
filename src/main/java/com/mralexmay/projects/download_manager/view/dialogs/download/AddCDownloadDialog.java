package com.mralexmay.projects.download_manager.view.dialogs.download;


import com.mralexmay.projects.download_manager.core.annotation.NotNull;
import com.mralexmay.projects.download_manager.core.events.ListDataAdapter;
import com.mralexmay.projects.download_manager.core.exception.RetrieveInformationThreadAlreadyRunning;
import com.mralexmay.projects.download_manager.core.model.download.Download;
import com.mralexmay.projects.download_manager.core.model.download.category.Category;
import com.mralexmay.projects.download_manager.core.model.download.group.Group;
import com.mralexmay.projects.download_manager.core.view.dialog.AJDialog;
import com.mralexmay.projects.download_manager.core.view.dialog.DialogResult;
import com.mralexmay.projects.download_manager.model.download.CommonDownload;
import com.mralexmay.projects.download_manager.model.download.CorePreDownloadInfo;
import com.mralexmay.projects.download_manager.controller.LocalGroupsController;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.ListDataEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;


public class AddCDownloadDialog extends AJDialog {
    private CommonDownload download;
    private final CorePreDownloadInfo pDownload;
    private final UpdateUI updateUIWorker;
    private final LocalGroupsController gController;
    private boolean isUserChangeName;
    private boolean isUserSelectCategory;


    public AddCDownloadDialog(Window owner,
                              @NotNull CorePreDownloadInfo pDownload,
                              @NotNull LocalGroupsController gController) {
        super(owner);
        initComponents();

        this.download = null;
        this.pDownload = pDownload;
        this.gController = gController;
        this.isUserChangeName = false;
        this.isUserSelectCategory = false;

        postInit();

        this.updateUIWorker = new UpdateUI(this, pDownload);

        try {
            updateUIWorker.start();
        } catch (RetrieveInformationThreadAlreadyRunning retrieveInformationThreadAlreadyRunning) {
            retrieveInformationThreadAlreadyRunning.printStackTrace();
        }
    }

    private void postInit() {
        final JComboBox comboBox = this.jcb_category;
        final DefaultComboBoxModel cbModel = (DefaultComboBoxModel) comboBox.getModel();

        final Group group = getLocalGroupsController().getGroupBy("CORE", "UNFINISHED");
        final List<Category> categories = group.getCategories();

        cbModel.removeAllElements();
        for (final Category category : categories) {
            if (!category.getName().equalsIgnoreCase("all")) {
                cbModel.addElement(category);
            }
        }

        final Category defaultCategory = getLocalGroupsController().getCategoryBy("CORE", "UNFINISHED", "OTHER");
        comboBox.setSelectedItem(defaultCategory);

        cbModel.addListDataListener(new ListDataAdapter() {
            @Override
            public void contentsChanged(ListDataEvent listDataEvent) {
                jcb_categoryContentsChanged(listDataEvent);
            }
        });
    }


    private void jbtn_choosePathActionPerformed(ActionEvent e) {
        try {
            try {
                if (!getPreDownloadInfo().getOutputDir().exists()) {
                    getPreDownloadInfo().getOutputDir().mkdirs();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            final JFileChooser fileChooser = new JFileChooser(getPreDownloadInfo().getOutputDir());
            fileChooser.setSelectedFile(getPreDownloadInfo().getFullPathToFile());
            final int result = fileChooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = fileChooser.getSelectedFile().getCanonicalFile();
                getPreDownloadInfo().setOutputDir(selectedFile.getParentFile().getCanonicalFile());
                getPreDownloadInfo().setFileName(selectedFile.getName());

                getJTF_fullPath().setText(getPreDownloadInfo().getFormatter().getFullPathToFile());
                getJTF_name().setText(getPreDownloadInfo().getFormatter().getName());
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", ioException.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jtf_nameCaretUpdate(CaretEvent e) {
        try {
            final String nameStr = jtf_name.getText();

            if (!nameStr.equals("") && !nameStr.equals(getPreDownloadInfo().getFileName())) {
                isUserChangeName = true;
                getPreDownloadInfo().setFileName(nameStr);

                getJTF_fullPath().setText(getPreDownloadInfo().getFormatter().getFullPathToFile());
                getJTF_fullPath().setCaretPosition(getJTF_fullPath().getText().length());
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void jcb_categoryContentsChanged(ListDataEvent listDataEvent) {
        final JComboBox comboBox = this.jcb_category;
        final Category category = (Category) comboBox.getSelectedItem();

        isUserSelectCategory = true;
        pDownload.setCategory(category);

        try {
            pDownload.setOutputDir(category.getOutputDir());

            String outputDir = this.pDownload.getOutputDir() + "";
            String fileName = this.pDownload.getFileName();

            jtf_fullPath.setText(outputDir + "/" + fileName);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void jbtn_addActionPerformed(ActionEvent e) {
        try {
            getUpdateUIWorker().stop();
            setDownload(new CommonDownload(getPreDownloadInfo()));

            this.setResult(DialogResult.OK);
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
        getUpdateUIWorker().stop();
        setDownload(null);

        this.setResult(DialogResult.CANCEL);
        this.dispose();
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        panel1 = new JPanel();
        label1 = new JLabel();
        jtf_url = new JTextField();
        panel5 = new JPanel();
        label4 = new JLabel();
        label5 = new JLabel();
        jlbl_size = new JLabel();
        jcb_category = new JComboBox<>();
        label6 = new JLabel();
        jlbl_type = new JLabel();
        label2 = new JLabel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        jtf_name = new JTextField();
        label3 = new JLabel();
        jtf_fullPath = new JTextField();
        jbtn_choosePath = new JButton();
        panel2 = new JPanel();
        jbtn_add = new JButton();
        jbtn_cancel = new JButton();

        //======== this ========
        setModal(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout) contentPane.getLayout()).columnWidths = new int[]{0, 0};
        ((GridBagLayout) contentPane.getLayout()).rowHeights = new int[]{0, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
        ((GridBagLayout) contentPane.getLayout()).rowWeights = new double[]{1.0, 0.0, 1.0E-4};

        //======== panel1 ========
        {
            panel1.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.
                    EmptyBorder(0, 0, 0, 0), "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax.swing.border.TitledBorder.CENTER, javax.swing
                    .border.TitledBorder.BOTTOM, new Font("D\u0069alog", Font.BOLD, 12),
                    Color.red), panel1.getBorder()));
            panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                @Override
                public void propertyChange(java.beans.PropertyChangeEvent e) {
                    if ("\u0062order".equals(e.getPropertyName()))
                        throw new RuntimeException();
                }
            });
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout) panel1.getLayout()).columnWidths = new int[]{361, 0, 92, 0};
            ((GridBagLayout) panel1.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
            ((GridBagLayout) panel1.getLayout()).columnWeights = new double[]{1.0, 0.0, 0.0, 1.0E-4};
            ((GridBagLayout) panel1.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

            //---- label1 ----
            label1.setText("Url:");
            panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- jtf_url ----
            jtf_url.setEditable(false);
            jtf_url.setDoubleBuffered(true);
            panel1.add(jtf_url, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

            //======== panel5 ========
            {
                panel5.setLayout(new GridBagLayout());
                ((GridBagLayout) panel5.getLayout()).columnWidths = new int[]{215, 104, 0, 0};
                ((GridBagLayout) panel5.getLayout()).rowHeights = new int[]{0, 0, 0};
                ((GridBagLayout) panel5.getLayout()).columnWeights = new double[]{0.0, 0.0, 1.0, 1.0E-4};
                ((GridBagLayout) panel5.getLayout()).rowWeights = new double[]{0.0, 0.0, 1.0E-4};

                //---- label4 ----
                label4.setText("Category:");
                panel5.add(label4, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- label5 ----
                label5.setText("Size:");
                panel5.add(label5, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 5, 5), 0, 0));

                //---- jlbl_size ----
                jlbl_size.setText("size");
                jlbl_size.setDoubleBuffered(true);
                panel5.add(jlbl_size, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                //---- jcb_category ----
                jcb_category.setModel(new DefaultComboBoxModel<>(new String[]{
                        "t"
                }));
                panel5.add(jcb_category, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                //---- label6 ----
                label6.setText("Type:");
                panel5.add(label6, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 0, 5), 0, 0));

                //---- jlbl_type ----
                jlbl_type.setText("type");
                jlbl_type.setDoubleBuffered(true);
                panel5.add(jlbl_type, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            panel1.add(panel5, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- label2 ----
            label2.setText("Name:");
            panel1.add(label2, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //======== panel3 ========
            {
                panel3.setLayout(new GridBagLayout());
                ((GridBagLayout) panel3.getLayout()).columnWidths = new int[]{0, 0};
                ((GridBagLayout) panel3.getLayout()).rowHeights = new int[]{0, 0};
                ((GridBagLayout) panel3.getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
                ((GridBagLayout) panel3.getLayout()).rowWeights = new double[]{1.0, 1.0E-4};

                //======== panel4 ========
                {
                    panel4.setForeground(new Color(153, 255, 204));
                    panel4.setBackground(new Color(153, 255, 204));
                    panel4.setLayout(new GridBagLayout());
                    ((GridBagLayout) panel4.getLayout()).columnWidths = new int[]{0};
                    ((GridBagLayout) panel4.getLayout()).rowHeights = new int[]{0};
                    ((GridBagLayout) panel4.getLayout()).columnWeights = new double[]{1.0E-4};
                    ((GridBagLayout) panel4.getLayout()).rowWeights = new double[]{1.0E-4};
                }
                panel3.add(panel4, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            panel1.add(panel3, new GridBagConstraints(2, 2, 1, 3, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

            //---- jtf_name ----
            jtf_name.setDoubleBuffered(true);
            jtf_name.addCaretListener(e -> jtf_nameCaretUpdate(e));
            panel1.add(jtf_name, new GridBagConstraints(0, 4, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- label3 ----
            label3.setText("Path:");
            panel1.add(label3, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- jtf_fullPath ----
            jtf_fullPath.setEditable(false);
            jtf_fullPath.setDoubleBuffered(true);
            panel1.add(jtf_fullPath, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- jbtn_choosePath ----
            jbtn_choosePath.setText("...");
            jbtn_choosePath.addActionListener(e -> jbtn_choosePathActionPerformed(e));
            panel1.add(jbtn_choosePath, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));
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

            //---- jbtn_add ----
            jbtn_add.setText("Add");
            jbtn_add.addActionListener(e -> jbtn_addActionPerformed(e));
            panel2.add(jbtn_add, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
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

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel panel1;
    private JLabel label1;
    private JTextField jtf_url;
    private JPanel panel5;
    private JLabel label4;
    private JLabel label5;
    private JLabel jlbl_size;
    private JComboBox<String> jcb_category;
    private JLabel label6;
    private JLabel jlbl_type;
    private JLabel label2;
    private JPanel panel3;
    private JPanel panel4;
    private JTextField jtf_name;
    private JLabel label3;
    private JTextField jtf_fullPath;
    private JButton jbtn_choosePath;
    private JPanel panel2;
    private JButton jbtn_add;
    private JButton jbtn_cancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    public Download getDownload() {
        return download;
    }

    public void setDownload(CommonDownload download) {
        this.download = download;
    }

    public UpdateUI getUpdateUIWorker() {
        return updateUIWorker;
    }

    public CorePreDownloadInfo getPreDownloadInfo() {
        return pDownload;
    }

    public LocalGroupsController getLocalGroupsController() {
        return gController;
    }

    protected boolean isUserChangeFileName() {
        return isUserChangeName;
    }

    protected boolean isUserSelectCategory() {
        return isUserSelectCategory;
    }

    public JTextField getJTF_url() {
        return jtf_url;
    }

    public JLabel getJLBL_size() {
        return jlbl_size;
    }

    public JComboBox<String> getJCB_category() {
        return jcb_category;
    }

    public JLabel getJLBL_type() {
        return jlbl_type;
    }

    public JTextField getJTF_name() {
        return jtf_name;
    }

    public JTextField getJTF_fullPath() {
        return jtf_fullPath;
    }
}