package com.mraleksmay.projects.download_manager.client.gui.core.view.dialog.download.controller;


import com.mraleksmay.projects.download_manager.client.gui.core.impl.model.download.DownloadEntityInformation;
import com.mraleksmay.projects.download_manager.client.gui.core.manager.ApplicationManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.io.IOException;
import java.util.List;

public class TableController {
    private final JTable table;
    private final ApplicationManager applicationManager;


    public TableController(JTable table, ApplicationManager applicationManager) {
        this.table = table;
        this.applicationManager = applicationManager;
    }

    public void initTable() {
        getTable().setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "Name", "Size", "Status", "Path", "Url"
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
            int column = 0;
            final TableColumnModel cm = getTable().getColumnModel();
            final DefaultTableCellRenderer cRenderer = new DefaultTableCellRenderer();
            cRenderer.setHorizontalAlignment(SwingConstants.CENTER);

            column = 0;
            cm.getColumn(column).setResizable(true);
            cm.getColumn(column).setMinWidth(40);
            cm.getColumn(column).setMaxWidth(800);
            cm.getColumn(column).setPreferredWidth(200);

            column++;
            cm.getColumn(column).setResizable(true);
            cm.getColumn(column).setMinWidth(40);
            cm.getColumn(column).setMaxWidth(1000);
            cm.getColumn(column).setPreferredWidth(100);

            column++;
            cm.getColumn(column).setResizable(true);
            cm.getColumn(column).setMinWidth(40);
            cm.getColumn(column).setMaxWidth(1000);
            cm.getColumn(column).setPreferredWidth(100);
        }
        getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getTable().setDoubleBuffered(true);
        getTable().setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

        getTable().updateUI();
    }

    public synchronized void updateTable(List<DownloadEntityInformation> downloadsList) {
        final DefaultTableModel model = (DefaultTableModel) getTable().getModel();

        if (downloadsList.size() >= getTable().getRowCount()) {
            int diff = downloadsList.size() - getTable().getRowCount();

            for (int i = 0; i < diff; i++) {
                int columnsCount = model.getColumnCount();
                model.addRow(new Object[columnsCount]);
            }
        } else {
            int diff = getTable().getRowCount() - downloadsList.size();

            while (diff-- > 0) {
                model.removeRow(0);
            }
        }

        getTable().updateUI();

        for (int i = 0; i < downloadsList.size(); i++) {
            try {
                int column = 0;
                DownloadEntityInformation downloadEntityInformation = downloadsList.get(i);
                model.setValueAt(downloadEntityInformation.getDownload().getFormatter().getName(), i, column++);
                model.setValueAt(downloadEntityInformation.getDownload().getFormatter().getFullSize(), i, column++);
                model.setValueAt(downloadEntityInformation.getDownload().getFormatter().getStatus(), i, column++);
                model.setValueAt(downloadEntityInformation.getDownload().getFormatter().getFullPathToFile(), i, column++);
                model.setValueAt(downloadEntityInformation.getDownload().getFormatter().getUrl(), i, column++);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public int[] getSelectedRows() {
        return getTable().getSelectedRows();
    }


    public JTable getTable() {
        return table;
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }

}
