package com.mraleksmay.projects.download_manager.client.gui.platform.view.frame.downloader;

import com.mraleksmay.projects.download_manager.client.gui.platform.model.download.DownloadEntityInformation;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.IORESTManager;
import com.mraleksmay.projects.download_manager.client.gui.platform.util.downloader.BasicNetWorker;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.dialog.authorization.signin.SignInDialog;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.dialog.authorization.signup.SignUpDialog;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.dialog.category.AddCategoryDialog;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.dialog.category.EditCategoryDialog;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.dialog.download.AddDisplayDownloadsDialog;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.dialog.download.AddDownloadFromUrlDialog;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.dialog.download.AddDownloadsFromClipboardDialog;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.dialog.store.PluginMarketPlace;
import com.mraleksmay.projects.download_manager.client.gui.platform.view.frame.sheduler.SchedulerFrame;
import com.mraleksmay.projects.download_manager.common.exception.DownloadAlreadyStartException;
import com.mraleksmay.projects.download_manager.common.exception.DownloadAlreadyStopException;
import com.mraleksmay.projects.download_manager.plugin.model.category.Category;
import com.mraleksmay.projects.download_manager.plugin.model.plugin.Plugin;
import com.mraleksmay.projects.download_manager.plugin.model.download.Download;
import com.mraleksmay.projects.download_manager.plugin.model.user.User;
import com.mraleksmay.projects.download_manager.common.view.ADialog;
import com.mraleksmay.projects.download_manager.common.view.AFrame;
import com.mraleksmay.projects.download_manager.common.view.DialogResult;
import com.mraleksmay.projects.download_manager.common.view.DownloadDialog;
import com.mraleksmay.projects.download_manager.plugin.manager.PluginDataManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;


public class MainFrame extends AFrame {
    private MainFrame frame = this;
    private ApplicationManager applicationManager;
    private boolean isUserSearchActive = false;
    private String userSearchQuery = "";


    public MainFrame() {
        initComponents();
    }


    public void init() {
        List<JMenuItem> jmis = new ArrayList<>();

        UIManager.LookAndFeelInfo[] ilafs = UIManager.getInstalledLookAndFeels();

        for (final UIManager.LookAndFeelInfo ilaf : ilafs) {
            JMenuItem jmi = new JMenuItem(ilaf.getName());
            jmi.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        UIManager.setLookAndFeel(ilaf.getClassName());
                        SwingUtilities.updateComponentTreeUI(frame);
                    } catch (ClassNotFoundException | InstantiationException
                            | IllegalAccessException | UnsupportedLookAndFeelException exception) {
                        exception.printStackTrace();
                    }
                }
            });
            jmis.add(jmi);
        }

        for (JMenuItem jmi : jmis) {
            getJMN_skins().add(jmi);
        }
    }


    public DialogResult showFrame() {
        JFrame frame = this;
        frame.setTitle("J Downloader");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension size = new Dimension(1000, 650);
        frame.setSize(size);
        frame.setPreferredSize(size);
        frame.pack();
        frame.setVisible(true);
        return DialogResult.NONE;
    }


    private void jmi_exitActionPerformed(ActionEvent e) {
        // TODO add your code here
        System.exit(0);
    }

    private void jbtn_addActionPerformed(ActionEvent e) {
        addDownloadFromUrl();
    }

    private void jbtn_startActionPerformed(ActionEvent e) {
        startSelectedDownloads();
    }

    private void jbtn_stopActionPerformed(ActionEvent e) {
        stopSelectedDownloads();
    }

    private void jbtn_stopAllActionPerformed(ActionEvent e) {
        stopAllDownloads();
    }

    private void jbtn_removeActionPerformed(ActionEvent e) {
        removeSelectedDownloads();
    }

    private void jmi_categories_addActionPerformed(ActionEvent e) {
        final ADialog dialog = new AddCategoryDialog(this, getApplicationManager());
        dialog.init();
        final DialogResult result = dialog.showDialog();

        if (result == DialogResult.OK) {
            getApplicationManager().getMainViewController().getTreeController().rebuildTree();
            getApplicationManager().getMainViewController().getTreeController().setAndSelectCategoryByDefault();
        }
    }

    private void jmi_categories_editActionPerformed(ActionEvent e) {
        if (!getApplicationManager().getPluginsManager().getActiveCategory().isEditable()) {
            JOptionPane.showMessageDialog(this,
                    "Category: " + getApplicationManager().getPluginsManager().getActiveCategory().getName() + " cann`t be edited. Please choose other category!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }


        final ADialog dialog = new EditCategoryDialog(this, getApplicationManager());
        dialog.init();
        final DialogResult result = dialog.showDialog();

        if (result == DialogResult.OK) {
            getApplicationManager().getMainViewController().getTreeController().rebuildTree();
            getApplicationManager().getMainViewController().getTreeController().setAndSelectCategoryByDefault();
        }
    }

    private void jmi_categories_removeActionPerformed(ActionEvent e) {
        if (!getApplicationManager().getPluginsManager().getActiveCategory().isRemovable()) {
            JOptionPane.showMessageDialog(this,
                    "Category: " + getApplicationManager().getPluginsManager().getActiveCategory().getName() + " cann`t be removed. Please choose other category!",
                    "Warning",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Category category = getApplicationManager().getPluginsManager().getActiveCategory();
        getApplicationManager().getPluginsManager().getActivePlugin().remove(category);

        getApplicationManager().getMainViewController().getTreeController().rebuildTree();
        getApplicationManager().getMainViewController().getTreeController().setAndSelectCategoryByDefault();
    }

    private void btn_startSearchActionPerformed(ActionEvent e) {
        String queryStr = jtf_search.getText();

        userSearchQuery = queryStr;
        isUserSearchActive = true;
    }

    private void jmi_importFromFileActionPerformed(ActionEvent e) {
        try {
            final File path = new File("./").getCanonicalFile();
            final JFileChooser fileChooser = new JFileChooser(path);
            final int result = fileChooser.showDialog(this, "Open");

            if (result == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = fileChooser.getSelectedFile().getCanonicalFile();
                getApplicationManager().getIOManager().restore(selectedFile);

                getApplicationManager().getMainViewController().getTreeController().rebuildTree();
                getApplicationManager().getMainViewController().getTreeController().setAndSelectCategoryByDefault();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", ioException.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jmi_exportToFileActionPerformed(ActionEvent e) {
        try {
            final File path = new File("./backup_" + System.currentTimeMillis() + ".jd").getCanonicalFile();
            final JFileChooser fileChooser = new JFileChooser(path);
            fileChooser.setSelectedFile(path);
            final int result = fileChooser.showSaveDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                final File selectedFile = fileChooser.getSelectedFile().getCanonicalFile();
                getApplicationManager().getIOManager().save(selectedFile);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error", ioException.getMessage(), JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jmi_signInActionPerformed(ActionEvent e) {
        final ADialog dialog = new SignInDialog(this, getApplicationManager());
        dialog.init();
        final DialogResult result = dialog.showDialog();

        if (result == DialogResult.OK) {
            updateAccountMenu();
        }
    }

    private void jmi_signUpActionPerformed(ActionEvent e) {
        final ADialog dialog = new SignUpDialog(this, getApplicationManager());
        dialog.init();
        final DialogResult result = dialog.showDialog();

        if (result == DialogResult.OK) {
            System.out.println("signup");
            updateAccountMenu();
        }
    }

    private void jbtn_schedulerActionPerformed(ActionEvent e) {
        AFrame frame = new SchedulerFrame(getApplicationManager());
        frame.init();
        frame.showFrame();
    }

    private void jmi_addDownloadFromUrlActionPerformed(ActionEvent e) {
        addDownloadFromUrl();
    }

    private void jmi_addDownloadFromClipboardActionPerformed(ActionEvent e) {
        addDownloadsFromClipboard();
    }


    private void jmi_editDownloadActionPerformed(ActionEvent e) {
        editDownload();
    }

    private void jmi_removeSelectedDownloadsActionPerformed(ActionEvent e) {
        removeSelectedDownloads();
    }

    private void jmi_removeAllDownloadsActionPerformed(ActionEvent e) {
        removeAllDownloads();
    }

    private void jmi_stopSelectedDownloadsActionPerformed(ActionEvent e) {
        stopSelectedDownloads();
    }

    private void jmi_stopAllDownloadsActionPerformed(ActionEvent e) {
        stopAllDownloads();
    }


    private void addDownloadFromUrl() {
        final ADialog dialog = new AddDownloadFromUrlDialog(this, getApplicationManager());
        final DialogResult result = dialog.showDialog();

        if (result == DialogResult.LATER) {
            final Download download = ((DownloadDialog) dialog).getDownload();
            final DownloadEntityInformation downloadEntityInfo = new DownloadEntityInformation(download, new BasicNetWorker());

            getApplicationManager().add(downloadEntityInfo);
        } else if (result == DialogResult.NOW) {
            final Download downloadInformation = ((DownloadDialog) dialog).getDownload();
            final DownloadEntityInformation downloadEntityInfo = new DownloadEntityInformation(downloadInformation, new BasicNetWorker());
            final List<Plugin> downloadHandlers = getApplicationManager().
                    getPluginsManager().
                    getPlugins().
                    stream().
                    filter((p) -> {
                        try {
                            return p.canHandle(downloadEntityInfo.getDownload().getUrl());
                        } catch (MalformedURLException malformedURLException) {
                            malformedURLException.printStackTrace();
                        }

                        return false;
                    }).
                    sorted(Comparator.comparing(Plugin::getPriority).reversed()).
                    collect(toUnmodifiableList());

            if (downloadHandlers.size() > 0) {
                final Plugin handler = downloadHandlers.get(0);

                try {
                    final Class<?>[] constructorArgsTypes = new Class<?>[]{AFrame.class, PluginDataManager.class};
                    final Object[] constructorParams = new Object[]{this, getApplicationManager().getPluginDataManager()};

                    final ADialog downloadInformationDialog = (ADialog) handler.getAddDownloadDialogWindow(constructorArgsTypes, constructorParams);
                    ((DownloadDialog) downloadInformationDialog).setDownloadInformation(downloadInformation);
                    downloadInformationDialog.init();
                    final DialogResult addResult = downloadInformationDialog.showDialog();

                    if (addResult == DialogResult.OK) {
                        final Download download = ((DownloadDialog) downloadInformationDialog).getDownload();
                        final DownloadEntityInformation downloadEntity = new DownloadEntityInformation(download, new BasicNetWorker());

                        getApplicationManager().add(downloadEntityInfo);
                    }
                } catch (final ClassNotFoundException |
                        IllegalAccessException |
                        InstantiationException |
                        NoSuchMethodException |
                        InvocationTargetException exception) {
                    JOptionPane.showMessageDialog(this,
                            "Error: Cann`t create download dialog!",
                            "Error!",
                            JOptionPane.ERROR_MESSAGE);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Error: Cann`t create download dialog!",
                            "Error!",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                try {
                    JOptionPane.showMessageDialog(this, "Cann`t handle url: " + downloadInformation.getUrl() + "", "Error!", JOptionPane.ERROR_MESSAGE);
                } catch (MalformedURLException malformedURLException) {
                }
            }
        }
    }

    private void addDownloadsFromClipboard() {
        ADialog dialog = new AddDownloadsFromClipboardDialog(this, getApplicationManager());
        dialog.init();
        DialogResult result = dialog.showDialog();

        if (result == DialogResult.OK) {
            final List<String> urlsList = ((AddDownloadsFromClipboardDialog) dialog).getUrlsList();

            dialog = new AddDisplayDownloadsDialog(this, getApplicationManager());
            ((AddDisplayDownloadsDialog) dialog).setUrlsList(urlsList);
            dialog.init();
            result = dialog.showDialog();

            if (result == DialogResult.OK) {
                final List<DownloadEntityInformation> downloadsList = ((AddDisplayDownloadsDialog) dialog).getDownloadsList();
                getApplicationManager().getDownloadsManager().addAll(downloadsList);
            }
        }
    }

    private void editDownload() {
        // TODO
    }

    private void removeSelectedDownloads() {
        try {
            final int[] selectedRows = getApplicationManager().getMainViewController().getTableController().getSelectedRows();
            final Category activeCategory = getApplicationManager().getPluginsManager().getActiveCategory();
            final List<DownloadEntityInformation> downloadsEntitiesList = getApplicationManager().getMainViewController().getTableController().getDownloadsInCategory(activeCategory);

            for (int i = 0; i < selectedRows.length; i++) {
                final int rIndex = selectedRows[i];
                final DownloadEntityInformation downloadEntity = downloadsEntitiesList.get(rIndex);
                final int dIndex = getApplicationManager().getDownloadsManager().indexOf(downloadEntity);

                getApplicationManager().getDownloadsManager().remove(dIndex);
            }
        } catch (Exception exception) {
        }
    }

    private void removeAllDownloads() {
        getApplicationManager().removeAll();
    }

    private void startSelectedDownloads() {
        try {
            final int[] selectedRows = getApplicationManager().getMainViewController().getTableController().getSelectedRows();
            final Category activeCategory = getApplicationManager().getPluginsManager().getActiveCategory();
            final List<DownloadEntityInformation> downloadsEntitiesList = getApplicationManager().getMainViewController().getTableController().getDownloadsInCategory(activeCategory);

            for (int i = 0; i < selectedRows.length; i++) {
                final int rIndex = selectedRows[i];
                final DownloadEntityInformation downloadEntity = downloadsEntitiesList.get(rIndex);
                final int dIndex = getApplicationManager().getDownloadsManager().indexOf(downloadEntity);

                getApplicationManager().getDownloadsManager().start(dIndex);
            }
        } catch (DownloadAlreadyStartException downloadAlreadyStartException) {
        }
    }

    private void stopSelectedDownloads() {
        try {
            final int[] selectedRows = getApplicationManager().getMainViewController().getTableController().getSelectedRows();
            final Category activeCategory = getApplicationManager().getPluginsManager().getActiveCategory();
            final List<DownloadEntityInformation> downloadsEntitiesList = getApplicationManager().getMainViewController().getTableController().getDownloadsInCategory(activeCategory);

            for (int i = 0; i < selectedRows.length; i++) {
                final int rIndex = selectedRows[i];
                final DownloadEntityInformation downloadEntity = downloadsEntitiesList.get(rIndex);
                final int dIndex = getApplicationManager().getDownloadsManager().indexOf(downloadEntity);

                getApplicationManager().getDownloadsManager().stop(dIndex);
            }
        } catch (DownloadAlreadyStopException downloadAlreadyStartException) {
        }
    }

    private void stopAllDownloads() {
        try {
            final List<DownloadEntityInformation> downloadsEntitiesList = getApplicationManager().getDownloadsManager().getDownloads();

            for (DownloadEntityInformation downloadEntity : downloadsEntitiesList) {
                downloadEntity.stop();
            }
        } catch (DownloadAlreadyStopException downloadAlreadyStartException) {
        }
    }


    private void updateAccountMenu() {
        final JMenu menu = getJMN_account();
        menu.removeAll();

        final JMenuItem mi_sync = new JMenuItem("Sync");
        final JMenuItem mi_exit = new JMenuItem("Exit");

        // Synchronize downloads list with server. Event handler.
        mi_sync.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    IORESTManager.UserService.sync(User.getCurrentUser().getToken(), getApplicationManager());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
        mi_exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                final JMenu menu = getJMN_account();
                menu.removeAll();

                final JMenuItem jmi_signin = new JMenuItem("Sign In");
                final JMenuItem jmi_signup = new JMenuItem("Sign Up");

                jmi_signin.addActionListener((e) -> jmi_signInActionPerformed(e));
                jmi_signup.addActionListener((e) -> jmi_signUpActionPerformed(e));

                menu.add(jmi_signin);
                menu.add(jmi_signup);
            }
        });

        menu.add(mi_sync);
        menu.add(new JSeparator());
        menu.add(mi_exit);
    }

    private void jmi_plugins_getListActionPerformed(ActionEvent e) {
        ADialog dialog = new PluginMarketPlace(this, getApplicationManager());
        dialog.init();
        DialogResult result = dialog.showDialog();

        if (result == DialogResult.OK) {

        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        menuBar1 = new JMenuBar();
        jm_file = new JMenu();
        jmi_importFromFile = new JMenuItem();
        jmi_exportToFile = new JMenuItem();
        jmi_exit = new JMenuItem();
        menu2 = new JMenu();
        menuItem1 = new JMenu();
        jmi_addDownloadFromUrl = new JMenuItem();
        jmi_addDownloadFromClipboard = new JMenuItem();
        jmi_editDownload = new JMenuItem();
        menuItem5 = new JMenu();
        jmi_removeSelectedDownloads = new JMenuItem();
        jmi_removeAllDownloads = new JMenuItem();
        menuItem6 = new JMenu();
        jmi_stopSelectedDownloads = new JMenuItem();
        jmi_stopAllDownloads = new JMenuItem();
        menu1 = new JMenu();
        jmi_categories_add = new JMenuItem();
        jmi_categories_edit = new JMenuItem();
        jmi_categories_remove = new JMenuItem();
        jmn_account = new JMenu();
        jmi_signIn = new JMenuItem();
        jmi_signUp = new JMenuItem();
        menu3 = new JMenu();
        jmn_skins = new JMenu();
        menu4 = new JMenu();
        jmi_plugins_load = new JMenuItem();
        jmi_plugins_remove = new JMenuItem();
        jmi_plugins_getList = new JMenuItem();
        menu5 = new JMenu();
        menuItem2 = new JMenuItem();
        panel3 = new JPanel();
        jbtn_add = new JButton();
        jbtn_start = new JButton();
        jbtn_stop = new JButton();
        jbtn_stopAll = new JButton();
        jbtn_remove = new JButton();
        jbtn_scheduler = new JButton();
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
        var contentPane = getContentPane();
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

                //---- jmi_importFromFile ----
                jmi_importFromFile.setText("Import...");
                jmi_importFromFile.addActionListener(e -> jmi_importFromFileActionPerformed(e));
                jm_file.add(jmi_importFromFile);

                //---- jmi_exportToFile ----
                jmi_exportToFile.setText("Export...");
                jmi_exportToFile.addActionListener(e -> jmi_exportToFileActionPerformed(e));
                jm_file.add(jmi_exportToFile);
                jm_file.addSeparator();

                //---- jmi_exit ----
                jmi_exit.setText("Exit");
                jmi_exit.addActionListener(e -> jmi_exitActionPerformed(e));
                jm_file.add(jmi_exit);
            }
            menuBar1.add(jm_file);

            //======== menu2 ========
            {
                menu2.setText("Downloads");

                //======== menuItem1 ========
                {
                    menuItem1.setText("Add");

                    //---- jmi_addDownloadFromUrl ----
                    jmi_addDownloadFromUrl.setText("From url");
                    jmi_addDownloadFromUrl.addActionListener(e -> jmi_addDownloadFromUrlActionPerformed(e));
                    menuItem1.add(jmi_addDownloadFromUrl);

                    //---- jmi_addDownloadFromClipboard ----
                    jmi_addDownloadFromClipboard.setText("From clipboard");
                    jmi_addDownloadFromClipboard.addActionListener(e -> jmi_addDownloadFromClipboardActionPerformed(e));
                    menuItem1.add(jmi_addDownloadFromClipboard);
                }
                menu2.add(menuItem1);

                //---- jmi_editDownload ----
                jmi_editDownload.setText("Edit");
                jmi_editDownload.addActionListener(e -> jmi_editDownloadActionPerformed(e));
                menu2.add(jmi_editDownload);

                //======== menuItem5 ========
                {
                    menuItem5.setText("Remove");

                    //---- jmi_removeSelectedDownloads ----
                    jmi_removeSelectedDownloads.setText("Remove Selected");
                    jmi_removeSelectedDownloads.addActionListener(e -> jmi_removeSelectedDownloadsActionPerformed(e));
                    menuItem5.add(jmi_removeSelectedDownloads);

                    //---- jmi_removeAllDownloads ----
                    jmi_removeAllDownloads.setText("Remove All");
                    jmi_removeAllDownloads.addActionListener(e -> jmi_removeAllDownloadsActionPerformed(e));
                    menuItem5.add(jmi_removeAllDownloads);
                }
                menu2.add(menuItem5);

                //======== menuItem6 ========
                {
                    menuItem6.setText("Stop");

                    //---- jmi_stopSelectedDownloads ----
                    jmi_stopSelectedDownloads.setText("Stop selected");
                    jmi_stopSelectedDownloads.addActionListener(e -> jmi_stopSelectedDownloadsActionPerformed(e));
                    menuItem6.add(jmi_stopSelectedDownloads);

                    //---- jmi_stopAllDownloads ----
                    jmi_stopAllDownloads.setText("Stop all");
                    jmi_stopAllDownloads.addActionListener(e -> jmi_stopAllDownloadsActionPerformed(e));
                    menuItem6.add(jmi_stopAllDownloads);
                }
                menu2.add(menuItem6);
            }
            menuBar1.add(menu2);

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

            //======== jmn_account ========
            {
                jmn_account.setText("Account");

                //---- jmi_signIn ----
                jmi_signIn.setText("Sign In");
                jmi_signIn.addActionListener(e -> jmi_signInActionPerformed(e));
                jmn_account.add(jmi_signIn);

                //---- jmi_signUp ----
                jmi_signUp.setText("Sign Up");
                jmi_signUp.addActionListener(e -> jmi_signUpActionPerformed(e));
                jmn_account.add(jmi_signUp);
            }
            menuBar1.add(jmn_account);

            //======== menu3 ========
            {
                menu3.setText("View");

                //======== jmn_skins ========
                {
                    jmn_skins.setText("Skins");
                }
                menu3.add(jmn_skins);
            }
            menuBar1.add(menu3);

            //======== menu4 ========
            {
                menu4.setText("Plugins");

                //---- jmi_plugins_load ----
                jmi_plugins_load.setText("Add");
                menu4.add(jmi_plugins_load);

                //---- jmi_plugins_remove ----
                jmi_plugins_remove.setText("Delete");
                menu4.add(jmi_plugins_remove);

                //---- jmi_plugins_getList ----
                jmi_plugins_getList.setText("Installed");
                jmi_plugins_getList.addActionListener(e -> jmi_plugins_getListActionPerformed(e));
                menu4.add(jmi_plugins_getList);
            }
            menuBar1.add(menu4);

            //======== menu5 ========
            {
                menu5.setText("Help");

                //---- menuItem2 ----
                menuItem2.setText("About");
                menu5.add(menuItem2);
            }
            menuBar1.add(menu5);
        }
        setJMenuBar(menuBar1);

        //======== panel3 ========
        {
            panel3.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.
                    EmptyBorder(0, 0, 0, 0), "JF\u006frmDes\u0069gner \u0045valua\u0074ion", javax.swing.border.TitledBorder.CENTER, javax.swing
                    .border.TitledBorder.BOTTOM, new java.awt.Font("D\u0069alog", java.awt.Font.BOLD, 12),
                    java.awt.Color.red), panel3.getBorder()));
            panel3.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                @Override
                public void propertyChange(java.beans.PropertyChangeEvent e) {
                    if ("\u0062order".equals(e.getPropertyName()))
                        throw new RuntimeException();
                }
            });
            panel3.setLayout(new GridBagLayout());
            ((GridBagLayout) panel3.getLayout()).columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 30, 299, 0};
            ((GridBagLayout) panel3.getLayout()).rowHeights = new int[]{0, 0};
            ((GridBagLayout) panel3.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 1.0E-4};
            ((GridBagLayout) panel3.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};

            //---- jbtn_add ----
            jbtn_add.setText("Add");
            jbtn_add.addActionListener(e -> jbtn_addActionPerformed(e));
            panel3.add(jbtn_add, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- jbtn_start ----
            jbtn_start.setText("Start");
            jbtn_start.addActionListener(e -> jbtn_startActionPerformed(e));
            panel3.add(jbtn_start, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- jbtn_stop ----
            jbtn_stop.setText("Stop");
            jbtn_stop.addActionListener(e -> jbtn_stopActionPerformed(e));
            panel3.add(jbtn_stop, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- jbtn_stopAll ----
            jbtn_stopAll.setText("Stop all");
            jbtn_stopAll.addActionListener(e -> jbtn_stopAllActionPerformed(e));
            panel3.add(jbtn_stopAll, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- jbtn_remove ----
            jbtn_remove.setText("Remove");
            jbtn_remove.addActionListener(e -> jbtn_removeActionPerformed(e));
            panel3.add(jbtn_remove, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.BASELINE, GridBagConstraints.HORIZONTAL,
                    new Insets(0, 0, 0, 0), 0, 0));

            //---- jbtn_scheduler ----
            jbtn_scheduler.setText("Schedule");
            jbtn_scheduler.addActionListener(e -> jbtn_schedulerActionPerformed(e));
            panel3.add(jbtn_scheduler, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
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
                btn_startSearch.setText("Search");
                btn_startSearch.addActionListener(e -> btn_startSearchActionPerformed(e));
                panel4.add(btn_startSearch, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            panel3.add(panel4, new GridBagConstraints(8, 0, 1, 1, 0.0, 0.0,
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
    private JMenuItem jmi_importFromFile;
    private JMenuItem jmi_exportToFile;
    private JMenuItem jmi_exit;
    private JMenu menu2;
    private JMenu menuItem1;
    private JMenuItem jmi_addDownloadFromUrl;
    private JMenuItem jmi_addDownloadFromClipboard;
    private JMenuItem jmi_editDownload;
    private JMenu menuItem5;
    private JMenuItem jmi_removeSelectedDownloads;
    private JMenuItem jmi_removeAllDownloads;
    private JMenu menuItem6;
    private JMenuItem jmi_stopSelectedDownloads;
    private JMenuItem jmi_stopAllDownloads;
    private JMenu menu1;
    private JMenuItem jmi_categories_add;
    private JMenuItem jmi_categories_edit;
    private JMenuItem jmi_categories_remove;
    private JMenu jmn_account;
    private JMenuItem jmi_signIn;
    private JMenuItem jmi_signUp;
    private JMenu menu3;
    private JMenu jmn_skins;
    private JMenu menu4;
    private JMenuItem jmi_plugins_load;
    private JMenuItem jmi_plugins_remove;
    private JMenuItem jmi_plugins_getList;
    private JMenu menu5;
    private JMenuItem menuItem2;
    private JPanel panel3;
    private JButton jbtn_add;
    private JButton jbtn_start;
    private JButton jbtn_stop;
    private JButton jbtn_stopAll;
    private JButton jbtn_remove;
    private JButton jbtn_scheduler;
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

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }

    public void setApplicationManager(ApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    public boolean isUserSearchActive() {
        return isUserSearchActive;
    }

    public void setUserSearchActive(boolean userSearchActive) {
        isUserSearchActive = userSearchActive;
    }

    public String getUserSearchQuery() {
        return userSearchQuery;
    }

    public void setUserSearchQuery(String userSearchQuery) {
        this.userSearchQuery = userSearchQuery;
    }

    public JButton getJBTN_scheduler() {
        return jbtn_scheduler;
    }

    public JMenu getJMN_account() {
        return jmn_account;
    }

    public JMenu getJMN_skins() {
        return jmn_skins;
    }
}
