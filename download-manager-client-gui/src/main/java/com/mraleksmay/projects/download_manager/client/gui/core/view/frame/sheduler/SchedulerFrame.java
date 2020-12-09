/*
 * Created by JFormDesigner on Sun Dec 06 02:55:20 EET 2020
 */

package com.mraleksmay.projects.download_manager.client.gui.core.view.frame.sheduler;

import java.awt.event.*;
import javax.swing.event.*;

import com.mraleksmay.projects.download_manager.client.gui.core.impl.model.download.DownloadEntityInformation;
import com.mraleksmay.projects.download_manager.client.gui.core.manager.ApplicationManager;
import com.mraleksmay.projects.download_manager.common.exception.ThreadAlreadyStartException;
import com.mraleksmay.projects.download_manager.common.view.AFrame;
import com.mraleksmay.projects.download_manager.common.view.DialogResult;
import com.mraleksmay.projects.download_manager.common.view.UIWorker;
import com.mraleksmay.projects.download_manager.client.gui.core.view.frame.sheduler.component.controller.TableController;
import com.mraleksmay.projects.download_manager.client.gui.core.view.frame.sheduler.controller.SchedulerFrameUIWorker;

import java.awt.*;
import java.util.List;
import javax.swing.*;

/**
 * @author unknown
 */
public class SchedulerFrame extends AFrame {
    private UIWorker uiWorker;
    private TableController tableController;
    private ApplicationManager applicationManager;

    public SchedulerFrame(ApplicationManager applicationManager) {
        super();
        initComponents();

        this.applicationManager = applicationManager;
        this.tableController = new TableController(getJTBL_queue(), getApplicationManager());
        this.uiWorker = new SchedulerFrameUIWorker(() -> {
            SwingUtilities.invokeLater((() -> {
                tableController.updateTable();
            }));
        }, 1000);
    }


    @Override
    public void init() {
        tableController.initTable();

        getJSPNR_queueSize().setValue(getApplicationManager().getScheduler().getQueueSize());
    }

    @Override
    public DialogResult showFrame() {
        JFrame frame = this;
        frame.setTitle("Scheduler");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        Dimension size = new Dimension(800, 600);
        frame.setSize(size);
        frame.setPreferredSize(size);
        frame.pack();

        try {
            this.uiWorker.start();
        } catch (ThreadAlreadyStartException threadAlreadyStartException) {
            threadAlreadyStartException.printStackTrace();
        }

        frame.setVisible(true);

        return getResult();
    }

    private void jspnr_queueSizeStateChanged(ChangeEvent e) {
        int value = (int) getJSPNR_queueSize().getValue();

        if (value < 1) {
            getJSPNR_queueSize().setValue(1);
        } else if (value > 8) {
            getJSPNR_queueSize().setValue(8);
        } else {
            getApplicationManager().getScheduler().setQueueSize(value);
        }
    }

    private void jbtn_dowloadUpActionPerformed(ActionEvent e) {
        final int[] selectedRows = this.getTableController().getSelectedRows();
        final List<DownloadEntityInformation> downloadsEntitiesList = getApplicationManager().getScheduler().getDownloads();

        for (int i = 0; i < selectedRows.length; i++) {
            final int rIndex = selectedRows[i];
            final DownloadEntityInformation downloadEntity = downloadsEntitiesList.get(rIndex);

            getApplicationManager().getScheduler().moveUp(downloadEntity);
        }
    }

    private void jbtn_downloadDownActionPerformed(ActionEvent e) {
        final int[] selectedRows = this.getTableController().getSelectedRows();
        final List<DownloadEntityInformation> downloadsEntitiesList = getApplicationManager().getScheduler().getDownloads();

        for (int i = 0; i < selectedRows.length; i++) {
            final int rIndex = selectedRows[i];
            final DownloadEntityInformation downloadEntity = downloadsEntitiesList.get(rIndex);

            getApplicationManager().getScheduler().moveDown(downloadEntity);
        }
    }

    private void jbtn_schedulerStartAllActionPerformed(ActionEvent e) {
        getApplicationManager().getScheduler().start();
    }

    private void jbtn_schedulerStopAllActionPerformed(ActionEvent e) {
        getApplicationManager().getScheduler().stop();
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        tabbedPane1 = new JTabbedPane();
        panel2 = new JPanel();
        panel3 = new JPanel();
        label1 = new JLabel();
        jspnr_queueSize = new JSpinner();
        jbtn_dowloadUp = new JButton();
        jbtn_downloadDown = new JButton();
        jbtn_schedulerStartAll = new JButton();
        jbtn_schedulerStopAll = new JButton();
        scrollPane1 = new JScrollPane();
        jtbl_queue = new JTable();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout) contentPane.getLayout()).columnWidths = new int[]{0, 0};
        ((GridBagLayout) contentPane.getLayout()).rowHeights = new int[]{0, 0};
        ((GridBagLayout) contentPane.getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
        ((GridBagLayout) contentPane.getLayout()).rowWeights = new double[]{1.0, 1.0E-4};

        //======== tabbedPane1 ========
        {

            //======== panel2 ========
            {
                panel2.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(
                        0, 0, 0, 0), "JF\u006frmDesi\u0067ner Ev\u0061luatio\u006e", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder
                        .BOTTOM, new java.awt.Font("Dialo\u0067", java.awt.Font.BOLD, 12), java.awt.Color.
                        red), panel2.getBorder()));
                panel2.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                    @Override
                    public void propertyChange(java.
                                                       beans.PropertyChangeEvent e) {
                        if ("borde\u0072".equals(e.getPropertyName())) throw new RuntimeException();
                    }
                });
                panel2.setLayout(new GridBagLayout());
                ((GridBagLayout) panel2.getLayout()).columnWidths = new int[]{0, 0, 0};
                ((GridBagLayout) panel2.getLayout()).rowHeights = new int[]{0, 0, 0};
                ((GridBagLayout) panel2.getLayout()).columnWeights = new double[]{1.0, 0.0, 1.0E-4};
                ((GridBagLayout) panel2.getLayout()).rowWeights = new double[]{0.0, 1.0, 1.0E-4};

                //======== panel3 ========
                {
                    panel3.setLayout(new GridBagLayout());
                    ((GridBagLayout) panel3.getLayout()).columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
                    ((GridBagLayout) panel3.getLayout()).rowHeights = new int[]{0, 0};
                    ((GridBagLayout) panel3.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 1.0E-4};
                    ((GridBagLayout) panel3.getLayout()).rowWeights = new double[]{0.0, 1.0E-4};

                    //---- label1 ----
                    label1.setText("Count:");
                    panel3.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                    //---- jspnr_queueSize ----
                    jspnr_queueSize.setModel(new SpinnerNumberModel(8, 1, 10, 1));
                    jspnr_queueSize.addChangeListener(e -> jspnr_queueSizeStateChanged(e));
                    panel3.add(jspnr_queueSize, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                    //---- jbtn_dowloadUp ----
                    jbtn_dowloadUp.setText("Up");
                    jbtn_dowloadUp.addActionListener(e -> jbtn_dowloadUpActionPerformed(e));
                    panel3.add(jbtn_dowloadUp, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                    //---- jbtn_downloadDown ----
                    jbtn_downloadDown.setText("Down");
                    jbtn_downloadDown.addActionListener(e -> jbtn_downloadDownActionPerformed(e));
                    panel3.add(jbtn_downloadDown, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                    //---- jbtn_schedulerStartAll ----
                    jbtn_schedulerStartAll.setText("Start");
                    jbtn_schedulerStartAll.addActionListener(e -> jbtn_schedulerStartAllActionPerformed(e));
                    panel3.add(jbtn_schedulerStartAll, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 5), 0, 0));

                    //---- jbtn_schedulerStopAll ----
                    jbtn_schedulerStopAll.setText("Stop");
                    jbtn_schedulerStopAll.addActionListener(e -> jbtn_schedulerStopAllActionPerformed(e));
                    panel3.add(jbtn_schedulerStopAll, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                }
                panel2.add(panel3, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                //======== scrollPane1 ========
                {

                    //---- jtbl_queue ----
                    jtbl_queue.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                    scrollPane1.setViewportView(jtbl_queue);
                }
                panel2.add(scrollPane1, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
            }
            tabbedPane1.addTab("Queue", panel2);
        }
        contentPane.add(tabbedPane1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    public JSpinner getJSPNR_queueSize() {
        return jspnr_queueSize;
    }

    public JTable getJTBL_queue() {
        return jtbl_queue;
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }

    public TableController getTableController() {
        return tableController;
    }

    public UIWorker getUiWorker() {
        return uiWorker;
    }


    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JTabbedPane tabbedPane1;
    private JPanel panel2;
    private JPanel panel3;
    private JLabel label1;
    private JSpinner jspnr_queueSize;
    private JButton jbtn_dowloadUp;
    private JButton jbtn_downloadDown;
    private JButton jbtn_schedulerStartAll;
    private JButton jbtn_schedulerStopAll;
    private JScrollPane scrollPane1;
    private JTable jtbl_queue;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}