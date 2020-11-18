package com.mralexmay.projects.download_manager.view;

import com.mralexmay.projects.download_manager.controller.DownloadsController;
import com.mralexmay.projects.download_manager.core.exception.DownloadAlreadyStartException;
import com.mralexmay.projects.download_manager.core.exception.ThreadAlreadyStop;
import com.mralexmay.projects.download_manager.core.model.download.Download;
import com.mralexmay.projects.download_manager.core.model.download.category.Category;
import com.mralexmay.projects.download_manager.core.model.download.meta_information.DownloadMetaInfo;
import com.mralexmay.projects.download_manager.core.view.dialog.AJDialog;
import com.mralexmay.projects.download_manager.core.view.dialog.DialogResult;
import com.mralexmay.projects.download_manager.model.download.CorePreDownloadInfo;
import com.mralexmay.projects.download_manager.model.download.local_group.CoreDownloadsFilter;
import com.mralexmay.projects.download_manager.model.download.meta.CoreDownloadMetaInfo;
import com.mralexmay.projects.download_manager.view.component.table.controller.TableController;
import com.mralexmay.projects.download_manager.view.component.tree.controller.TreeController;
import com.mralexmay.projects.download_manager.view.controller.ViewController;
import com.mralexmay.projects.download_manager.view.dialogs.download.AddCDownloadDialog;
import com.mralexmay.projects.download_manager.view.dialogs.download.PreAddDownloadDialog;
import com.mralexmay.projects.download_manager.controller.LocalGroupsController;
import com.mralexmay.projects.download_manager.util.BasicNetWorker;
import com.mralexmay.projects.download_manager.view.dialogs.category.AddCategoryDialog;
import com.mralexmay.projects.download_manager.view.dialogs.category.EditCategoryDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;


public class MainFrame extends JFrame {
    private ViewController viewController;


    public MainFrame() {
        initComponents();
        this.viewController = new ViewController(this);
    }


    private void jmi_exitActionPerformed(ActionEvent e) {
        // TODO add your code here
        System.exit(0);
    }

    private void jbtn_addActionPerformed(ActionEvent e) {
        final AJDialog dialog = new PreAddDownloadDialog(this, getLocalGroupsController());
        dialog.setTitle("Download Information");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(new Dimension(600, 250));
        dialog.setPreferredSize(new Dimension(600, 250));
        dialog.pack();
        dialog.setVisible(true);

        final DialogResult result = dialog.getResult();
        if (result == DialogResult.OK) {
            final CorePreDownloadInfo pDownload = ((PreAddDownloadDialog) dialog).getPreDownloadInformation();

            final AJDialog nextDialog = new AddCDownloadDialog(this, pDownload, getLocalGroupsController());
            nextDialog.setTitle("Download Information");
            nextDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            nextDialog.setSize(new Dimension(600, 300));
            nextDialog.setPreferredSize(new Dimension(600, 300));
            nextDialog.pack();
            nextDialog.setVisible(true);

            final DialogResult addResult = nextDialog.getResult();
            if (addResult == DialogResult.OK) {
                final Download download = ((AddCDownloadDialog) nextDialog).getDownload();
                final DownloadMetaInfo mDownload = new CoreDownloadMetaInfo(download, new BasicNetWorker());

                try {
                    getDownloadsController().add(mDownload);
                    getTableController().add(mDownload);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

                getTreeController().updateTree(getDownloadsController().getDownloads());
            }
        }
    }

    private void jbtn_startActionPerformed(ActionEvent e) {
        try {
            final int[] selectedRows = getTableController().getSelectedRows();
            final List<DownloadMetaInfo> mDownloads = new CoreDownloadsFilter(getLocalGroupsController()).getDownloadsToDisplay(getDownloadsController().getDownloads());

            for (int i = 0; i < selectedRows.length; i++) {
                final int rIndex = selectedRows[i];
                final DownloadMetaInfo mDownload = mDownloads.get(rIndex);
                final int dIndex = getDownloadsController().indexOf(mDownload);

                getDownloadsController().start(dIndex);
            }
        } catch (DownloadAlreadyStartException downloadAlreadyStartException) {
            downloadAlreadyStartException.printStackTrace();
        }
    }

    private void jbtn_stopActionPerformed(ActionEvent e) {
        try {
            final int[] selectedRows = getTableController().getSelectedRows();
            final List<DownloadMetaInfo> mDownloads = new CoreDownloadsFilter(getLocalGroupsController()).getDownloadsToDisplay(getDownloadsController().getDownloads());

            for (int i = 0; i < selectedRows.length; i++) {
                final int rIndex = selectedRows[i];
                final DownloadMetaInfo mDownload = mDownloads.get(rIndex);
                final int dIndex = getDownloadsController().indexOf(mDownload);

                getDownloadsController().stop(dIndex);
            }
        } catch (ThreadAlreadyStop threadAlreadyStop) {
            threadAlreadyStop.printStackTrace();
        }
    }

    private void jbtn_stopAllActionPerformed(ActionEvent e) {
        try {
            final List<DownloadMetaInfo> downloads = getDownloadsController().getDownloads();

            for (final DownloadMetaInfo mDownload : downloads) {
                final int dIndex = getDownloadsController().indexOf(mDownload);

                if (mDownload.getDownload().getStatus() == Download.Status.DOWNLOADING) {
                    getDownloadsController().stop(dIndex);
                }
            }
        } catch (ThreadAlreadyStop threadAlreadyStop) {
            threadAlreadyStop.printStackTrace();
        }
    }

    private void jbtn_removeActionPerformed(ActionEvent e) {
        final int[] selectedRows = getTableController().getSelectedRows();
        final List<DownloadMetaInfo> mDownloads = new CoreDownloadsFilter(getLocalGroupsController()).getDownloadsToDisplay(getDownloadsController().getDownloads());

        for (int i = 0; i < selectedRows.length; i++) {
            final int rIndex = selectedRows[i];
            final DownloadMetaInfo mDownload = mDownloads.get(rIndex);
            final int dIndex = getDownloadsController().indexOf(mDownload);

            getDownloadsController().remove(dIndex);
        }

        getTableController().updateTable(getLocalGroupsController().getActiveLocalGroup().filterDownloads(mDownloads));
    }

    private void jmi_categories_addActionPerformed(ActionEvent e) {
        final AJDialog dialog = new AddCategoryDialog(this, getLocalGroupsController());
        dialog.setTitle("Add new category");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(new Dimension(600, 300));
        dialog.setPreferredSize(new Dimension(600, 300));
        dialog.pack();
        dialog.setVisible(true);

        final DialogResult result = dialog.getResult();
        if (result == DialogResult.OK) {
            getTreeController().rebuildTree(getLocalGroupsController());
            getTreeController().setAndSelectCategoryByDefault();
        }
    }

    private void jmi_categories_editActionPerformed(ActionEvent e) {
        if (!getLocalGroupsController().getActiveCategory().isEditable()) {
            JOptionPane.showMessageDialog(this,
                    "Category: " + getLocalGroupsController().getActiveCategory().getName() + " cann`t be edited. Please choose other category!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }


        final AJDialog dialog = new EditCategoryDialog(this, getLocalGroupsController());
        dialog.setTitle("Edit category");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setSize(new Dimension(600, 400));
        dialog.setPreferredSize(new Dimension(600, 400));
        dialog.pack();
        dialog.setVisible(true);

        final DialogResult result = dialog.getResult();
        if (result == DialogResult.OK) {
            getTreeController().rebuildTree(getLocalGroupsController());
            getTreeController().setAndSelectCategoryByDefault();
        }
    }

    private void jmi_categories_removeActionPerformed(ActionEvent e) {
        if (!getLocalGroupsController().getActiveCategory().isRemovable()) {
            JOptionPane.showMessageDialog(this,
                    "Category: " + getLocalGroupsController().getActiveCategory().getName() + " cann`t be removed. Please choose other category!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Category category = getLocalGroupsController().getActiveCategory();
        getLocalGroupsController().getActiveLocalGroup().remove(category);
        getTreeController().rebuildTree(getLocalGroupsController());
        getTreeController().setAndSelectCategoryByDefault();
    }

    private void btn_startSearchActionPerformed(ActionEvent e) {
        String queryStr = jtf_search.getText();

        getViewController().getUiUpdater().setUserSearchQuery(queryStr);
        getViewController().getUiUpdater().setUserSearchActive(true);
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        menuBar1 = new JMenuBar();
        jm_file = new JMenu();
        jmi_exit = new JMenuItem();
        menu1 = new JMenu();
        jmi_categories_add = new JMenuItem();
        jmi_categories_edit = new JMenuItem();
        jmi_categories_remove = new JMenuItem();
        panel3 = new JPanel();
        jbtn_add = new JButton();
        jbtn_start = new JButton();
        jbtn_stop = new JButton();
        jbtn_stopAll = new JButton();
        jbtn_remove = new JButton();
        panel4 = new JPanel();
        jtf_search = new JTextField();
        btn_startSearch = new JButton();
        panel1 = new JPanel();
        scrollPane2 = new JScrollPane();
        jtree_categories = new JTree();
        panel2 = new JPanel();
        scrollPane1 = new JScrollPane();
        jtbl_downloads = new JTable();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout) contentPane.getLayout()).columnWidths = new int[]{200, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).rowHeights = new int[]{0, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).columnWeights = new double[]{0.0, 1.0, 1.0E-4};
        ((GridBagLayout) contentPane.getLayout()).rowWeights = new double[]{0.0, 1.0, 1.0E-4};

        //======== menuBar1 ========
        {

            //======== jm_file ========
            {
                jm_file.setText("File");
                jm_file.addSeparator();

                //---- jmi_exit ----
                jmi_exit.setText("Exit");
                jmi_exit.addActionListener(e -> jmi_exitActionPerformed(e));
                jm_file.add(jmi_exit);
            }
            menuBar1.add(jm_file);

            //======== menu1 ========
            {
                menu1.setText("Categories");

                //---- jmi_categories_add ----
                jmi_categories_add.setText("Add");
                jmi_categories_add.addActionListener(e -> jmi_categories_addActionPerformed(e));
                menu1.add(jmi_categories_add);

                //---- jmi_categories_edit ----
                jmi_categories_edit.setText("Edit");
                jmi_categories_edit.addActionListener(e -> jmi_categories_editActionPerformed(e));
                menu1.add(jmi_categories_edit);

                //---- jmi_categories_remove ----
                jmi_categories_remove.setText("Remove");
                jmi_categories_remove.addActionListener(e -> jmi_categories_removeActionPerformed(e));
                menu1.add(jmi_categories_remove);
            }
            menuBar1.add(menu1);
        }
        setJMenuBar(menuBar1);

        //======== panel3 ========
        {
            panel3.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border
                    .EmptyBorder(0, 0, 0, 0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn", javax.swing.border.TitledBorder.CENTER, javax
                    .swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dia\u006cog", java.awt.Font.BOLD,
                    12), java.awt.Color.red), panel3.getBorder()));
            panel3.addPropertyChangeListener(new java.beans
                    .PropertyChangeListener() {
                @Override
                public void propertyChange(java.beans.PropertyChangeEvent e) {
                    if ("\u0062ord\u0065r".equals(e.
                            getPropertyName())) throw new RuntimeException();
                }
            });
            panel3.setLayout(new GridBagLayout());
            ((GridBagLayout) panel3.getLayout()).columnWidths = new int[]{0, 0, 0, 0, 0, 0, 30, 299, 0};
            ((GridBagLayout) panel3.getLayout()).rowHeights = new int[]{0, 0};
            ((GridBagLayout) panel3.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 1.0E-4};
            ((GridBagLayout) panel3.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};

            //---- jbtn_add ----
            jbtn_add.setText("+");
            jbtn_add.addActionListener(e -> jbtn_addActionPerformed(e));
            panel3.add(jbtn_add, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- jbtn_start ----
            jbtn_start.setText(">...");
            jbtn_start.addActionListener(e -> jbtn_startActionPerformed(e));
            panel3.add(jbtn_start, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- jbtn_stop ----
            jbtn_stop.setText("..|");
            jbtn_stop.addActionListener(e -> jbtn_stopActionPerformed(e));
            panel3.add(jbtn_stop, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- jbtn_stopAll ----
            jbtn_stopAll.setText("*|");
            jbtn_stopAll.addActionListener(e -> jbtn_stopAllActionPerformed(e));
            panel3.add(jbtn_stopAll, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- jbtn_remove ----
            jbtn_remove.setText("v");
            jbtn_remove.addActionListener(e -> jbtn_removeActionPerformed(e));
            panel3.add(jbtn_remove, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //======== panel4 ========
            {
                panel4.setLayout(new GridBagLayout());
                ((GridBagLayout) panel4.getLayout()).columnWidths = new int[]{0, 40, 0};
                ((GridBagLayout) panel4.getLayout()).rowHeights = new int[]{0, 0};
                ((GridBagLayout) panel4.getLayout()).columnWeights = new double[]{1.0, 0.0, 1.0E-4};
                ((GridBagLayout) panel4.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};
                panel4.add(jtf_search, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));

                //---- btn_startSearch ----
                btn_startSearch.setText("Q");
                btn_startSearch.addActionListener(e -> btn_startSearchActionPerformed(e));
                panel4.add(btn_startSearch, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            panel3.add(panel4, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel3, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        //======== panel1 ========
        {
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout) panel1.getLayout()).columnWidths = new int[]{0, 0};
            ((GridBagLayout) panel1.getLayout()).rowHeights = new int[]{0, 0};
            ((GridBagLayout) panel1.getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
            ((GridBagLayout) panel1.getLayout()).rowWeights = new double[]{1.0, 1.0E-4};

            //======== scrollPane2 ========
            {

                //---- jtree_categories ----
                jtree_categories.setRootVisible(false);
                jtree_categories.setModel(new DefaultTreeModel(
                        new DefaultMutableTreeNode("(root)") {
                            {
                            }
                        }));
                scrollPane2.setViewportView(jtree_categories);
            }
            panel1.add(scrollPane2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        //======== panel2 ========
        {
            panel2.setLayout(new GridBagLayout());
            ((GridBagLayout) panel2.getLayout()).columnWidths = new int[]{0, 0};
            ((GridBagLayout) panel2.getLayout()).rowHeights = new int[]{0, 0};
            ((GridBagLayout) panel2.getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
            ((GridBagLayout) panel2.getLayout()).rowWeights = new double[]{1.0, 1.0E-4};

            //======== scrollPane1 ========
            {

                //---- jtbl_downloads ----
                jtbl_downloads.setModel(new DefaultTableModel(
                        new Object[][]{
                                {null, null, null, null, null, null, null},
                                {null, null, null, null, null, null, null},
                        },
                        new String[]{
                                "Id", "Name", "Progress", "Downloaded", "Size", "Path", "Url"
                        }
                ) {
                    boolean[] columnEditable = new boolean[]{
                            false, false, false, false, false, false, false
                    };

                    @Override
                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return columnEditable[columnIndex];
                    }
                });
                {
                    TableColumnModel cm = jtbl_downloads.getColumnModel();
                    cm.getColumn(0).setResizable(false);
                    cm.getColumn(0).setMinWidth(40);
                    cm.getColumn(0).setMaxWidth(60);
                    cm.getColumn(0).setPreferredWidth(60);
                    cm.getColumn(1).setResizable(false);
                    cm.getColumn(2).setResizable(false);
                    cm.getColumn(3).setResizable(false);
                    cm.getColumn(4).setResizable(false);
                    cm.getColumn(5).setResizable(false);
                    cm.getColumn(6).setResizable(false);
                }
                jtbl_downloads.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                jtbl_downloads.setDoubleBuffered(true);
                jtbl_downloads.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
                scrollPane1.setViewportView(jtbl_downloads);
            }
            panel2.add(scrollPane1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JMenuBar menuBar1;
    private JMenu jm_file;
    private JMenuItem jmi_exit;
    private JMenu menu1;
    private JMenuItem jmi_categories_add;
    private JMenuItem jmi_categories_edit;
    private JMenuItem jmi_categories_remove;
    private JPanel panel3;
    private JButton jbtn_add;
    private JButton jbtn_start;
    private JButton jbtn_stop;
    private JButton jbtn_stopAll;
    private JButton jbtn_remove;
    private JPanel panel4;
    private JTextField jtf_search;
    private JButton btn_startSearch;
    private JPanel panel1;
    private JScrollPane scrollPane2;
    private JTree jtree_categories;
    private JPanel panel2;
    private JScrollPane scrollPane1;
    private JTable jtbl_downloads;
    // JFormDesigner - End of variables declaration  //GEN-END:variables


    public JTable getTable() {
        return jtbl_downloads;
    }

    public JTree getTree() {
        return jtree_categories;
    }

    public DownloadsController getDownloadsController() {
        return viewController.getDownloadsController();
    }

    public LocalGroupsController getLocalGroupsController() {
        return viewController.getLocalGroupsController();
    }

    public TableController getTableController() {
        return viewController.getTableController();
    }

    public TreeController getTreeController() {
        return viewController.getTreeController();
    }

    public ViewController getViewController() {
        return this.viewController;
    }
}
