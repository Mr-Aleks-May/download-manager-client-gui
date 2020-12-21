package com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.view.dialog;



import com.mraleksmay.projects.download_manager.client.gui.platform.model.plugin.model.download.BaseDownload;
import com.mraleksmay.projects.download_manager.common.util.DownloadUtil;
import com.mraleksmay.projects.download_manager.common.event.ListDataAdapter;
import com.mraleksmay.projects.download_manager.common.exception.ThreadAlreadyStartException;
import com.mraleksmay.projects.download_manager.common.exception.ThreadAlreadyStopException;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;
import com.mraleksmay.projects.download_manager.plugin.model.group.Group;
import com.mraleksmay.projects.download_manager.common.view.*;
import com.mraleksmay.projects.download_manager.plugin.manager.PluginDataManager;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.ListDataEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class AddCommonDownloadDialog extends ADialog implements DownloadDialog {
    private DialogResult dialogResult = DialogResult.NONE;
    private boolean isUserChangeName;
    private boolean isUserSelectCategory;
    private Download downloadInformation;
    private Download download;
    private PluginDataManager pluginDataManager;
    private UIWorker uiUpdater;


    public AddCommonDownloadDialog(AFrame owner, PluginDataManager pluginDataManager) {
        super(owner);
        initComponents();
        this.isUserChangeName = false;
        this.isUserSelectCategory = false;
        this.pluginDataManager = pluginDataManager;

        uiUpdater = new AddCommonDialogUIWorker(() -> {
            HttpURLConnection conn = null;

            try {
                final URL url = getDownloadInformation().getUrl();

                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
                conn.setRequestProperty("Upgrade-Insecure-Requests", "1");
                conn.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.123 Safari/537.36");
                conn.setInstanceFollowRedirects(true);

                final int cLength = conn.getContentLength();
                final String cType = conn.getContentType().trim();
                final String nameStr = DownloadUtil.suggestNameFrom(conn.getURL() + "");
                final Category category = getPluginDataManager().tryDeterminateCategoryBy(url, nameStr, cType);

                if (!isUserChangeFileName()) {
                    getDownloadInformation().setFileName(nameStr);
                    getDownloadInformation().setOutputDir(category.getOutputDir());
                }
                getDownloadInformation().setFullSize(cLength);
                if (!isUserSelectCategory())
                    getDownloadInformation().setCategory(category);


                getJTF_name().setText(getDownloadInformation().getFormatter().getName());
                getJLBL_size().setText(getDownloadInformation().getFormatter().getFullSize());
                getJLBL_type().setText(cType);
                getJCB_category().setSelectedItem(getDownloadInformation().getCategory());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn == null) {
                    conn.disconnect();
                }
            }
        }, -1);
    }

    @Override
    public DialogResult showDialog() {
        ADialog dialog = this;
        dialog.setTitle("Download Information");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(new Dimension(600, 300));
        dialog.setPreferredSize(new Dimension(600, 300));
        dialog.pack();
        dialog.setModal(true);

        try {
            getUIUpdater().start();
        } catch (ThreadAlreadyStartException threadAlreadyStartException) {
            threadAlreadyStartException.printStackTrace();
        }

        dialog.setVisible(true);

        return dialogResult;
    }

    @Override
    public void init() {
        final JComboBox comboBox = this.jcb_category;
        final DefaultComboBoxModel cbModel = (DefaultComboBoxModel) comboBox.getModel();

        final Group group = getPluginDataManager().getGroupBy("CORE", "UNFINISHED");
        final List<Category> categories = group.getCategories();

        cbModel.removeAllElements();
        for (final Category category : categories) {
            if (!category.getName().equalsIgnoreCase("all")) {
                cbModel.addElement(category);
            }
        }

        final Category defaultCategory = getPluginDataManager().getCategoryBy("CORE", "UNFINISHED", "OTHER");
        comboBox.setSelectedItem(defaultCategory);

        cbModel.addListDataListener(new ListDataAdapter() {
            @Override
            public void contentsChanged(ListDataEvent listDataEvent) {
                jcb_categoryContentsChanged(listDataEvent);
            }
        });

        try {
            this.getJTF_url().setText(getDownloadInformation().getUrl() + "");
            this.getJTF_name().setText(getDownloadInformation().getFileName());
            this.getJTF_fullPath().setText(getDownloadInformation().getFullPathToFile().getCanonicalPath());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }


    private void jbtn_choosePathActionPerformed(ActionEvent e) {
        try {
            try {
                if (!getDownloadInformation().getOutputDir().exists()) {
                    getDownloadInformation().getOutputDir().mkdirs();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            final JFileChooser fileChooser = new JFileChooser(getDownloadInformation().getOutputDir());
            fileChooser.setSelectedFile(getDownloadInformation().getFullPathToFile());
            final int result = fileChooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = fileChooser.getSelectedFile().getCanonicalFile();
                getDownloadInformation().setOutputDir(selectedFile.getParentFile().getCanonicalFile());
                getDownloadInformation().setFileName(selectedFile.getName());

                getJTF_fullPath().setText(getDownloadInformation().getFormatter().getFullPathToFile());
                getJTF_name().setText(getDownloadInformation().getFormatter().getName());
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", ioException.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jtf_nameCaretUpdate(CaretEvent e) {
        try {
            final String nameStr = jtf_name.getText();

            if (!nameStr.equals("") && !nameStr.equals(getDownloadInformation().getFileName())) {
                isUserChangeName = true;
                getDownloadInformation().setFileName(nameStr);

                getJTF_fullPath().setText(getDownloadInformation().getFormatter().getFullPathToFile());
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
        getDownloadInformation().setCategory(category);

        try {
            getDownloadInformation().setOutputDir(category.getOutputDir());

            String outputDir = getDownloadInformation().getOutputDir() + "";
            String fileName = getDownloadInformation().getFileName();

            jtf_fullPath.setText(outputDir + "/" + fileName);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void jbtn_addActionPerformed(ActionEvent e) {
        try {
            getUIUpdater().stop();
        } catch (ThreadAlreadyStopException threadAlreadyStopException) {
        }

        try {
            setDownload(new BaseDownload(getDownloadInformation()));

            dialogResult = DialogResult.OK;
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
        try {
            getUIUpdater().stop();
        } catch (ThreadAlreadyStopException threadAlreadyStopException) {
        }
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
        var contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0};
        ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0};
        ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
        ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {1.0, 0.0, 1.0E-4};

        //======== panel1 ========
        {
            panel1.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.
            EmptyBorder(0,0,0,0), "JFor\u006dDesi\u0067ner \u0045valu\u0061tion",javax.swing.border.TitledBorder.CENTER,javax.swing
            .border.TitledBorder.BOTTOM,new java.awt.Font("Dia\u006cog",java.awt.Font.BOLD,12),
            java.awt.Color.red),panel1. getBorder()));panel1. addPropertyChangeListener(new java.beans.PropertyChangeListener()
            {@Override public void propertyChange(java.beans.PropertyChangeEvent e){if("bord\u0065r".equals(e.getPropertyName()))
            throw new RuntimeException();}});
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {361, 0, 92, 0};
            ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
            ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 1.0E-4};
            ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

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
                ((GridBagLayout)panel5.getLayout()).columnWidths = new int[] {215, 104, 0, 0};
                ((GridBagLayout)panel5.getLayout()).rowHeights = new int[] {0, 0, 0};
                ((GridBagLayout)panel5.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0, 1.0E-4};
                ((GridBagLayout)panel5.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

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
                jcb_category.setModel(new DefaultComboBoxModel<>(new String[] {
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
                ((GridBagLayout)panel3.getLayout()).columnWidths = new int[] {0, 0};
                ((GridBagLayout)panel3.getLayout()).rowHeights = new int[] {0, 0};
                ((GridBagLayout)panel3.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
                ((GridBagLayout)panel3.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

                //======== panel4 ========
                {
                    panel4.setLayout(new GridBagLayout());
                    ((GridBagLayout)panel4.getLayout()).columnWidths = new int[] {0};
                    ((GridBagLayout)panel4.getLayout()).rowHeights = new int[] {0};
                    ((GridBagLayout)panel4.getLayout()).columnWeights = new double[] {1.0E-4};
                    ((GridBagLayout)panel4.getLayout()).rowWeights = new double[] {1.0E-4};
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
            ((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {0, 0, 0};
            ((GridBagLayout)panel2.getLayout()).rowHeights = new int[] {0, 0};
            ((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0E-4};
            ((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

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


    protected boolean isUserChangeFileName() {
        return isUserChangeName;
    }

    protected boolean isUserSelectCategory() {
        return isUserSelectCategory;
    }


    public PluginDataManager getPluginDataManager() {
        return pluginDataManager;
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

    public Download getDownloadInformation() {
        return downloadInformation;
    }

    @Override
    public void setDownloadInformation(Download downloadInformation) {
        this.downloadInformation = downloadInformation;
    }


    @Override
    public Download getDownload() {
        return this.download;
    }

    public void setDownload(Download download) {
        this.download = download;
    }

    public UIWorker getUIUpdater() {
        return uiUpdater;
    }
}

class AddCommonDialogUIWorker extends UIWorker {
    public AddCommonDialogUIWorker(Runnable uiUpdateAction, int millis) {
        super(uiUpdateAction, millis);
    }
}