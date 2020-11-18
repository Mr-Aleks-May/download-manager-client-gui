package com.mralexmay.projects.download_manager.view.component.table.controller;

import com.mralexmay.projects.download_manager.core.model.download.meta_information.DownloadMetaInfo;
import com.mralexmay.projects.download_manager.controller.LocalGroupsController;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.io.IOException;
import java.util.List;

public class TableController {
    private final JTable table;
    private final LocalGroupsController gController;
    public static boolean reloadTable;


    public TableController(JTable table, LocalGroupsController gController) {
        this.table = table;
        this.gController = gController;
    }

    public void initTable() {
        getTable().setModel(new DefaultTableModel(
                new Object[][]{
                },
                new String[]{
                        "Id", "Name", "Progress", "Downloaded", "Size", "Speed", "Status", "Path", "Url"
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
            cm.getColumn(column).setMaxWidth(60);
            cm.getColumn(column).setPreferredWidth(60);
            cm.getColumn(column).setCellRenderer(cRenderer);

            column = 1;
            cm.getColumn(column).setResizable(true);
            cm.getColumn(column).setMinWidth(40);
            cm.getColumn(column).setMaxWidth(800);
            cm.getColumn(column).setPreferredWidth(200);

            column = 2;
            cm.getColumn(column).setResizable(true);
            cm.getColumn(column).setMinWidth(40);
            cm.getColumn(column).setMaxWidth(100);
            cm.getColumn(column).setPreferredWidth(100);
            cm.getColumn(column).setCellRenderer(cRenderer);

            column = 3;
            cm.getColumn(column).setResizable(true);
            cm.getColumn(column).setMinWidth(40);
            cm.getColumn(column).setMaxWidth(100);
            cm.getColumn(column).setPreferredWidth(100);
            cm.getColumn(column).setCellRenderer(cRenderer);

            column = 4;
            cm.getColumn(column).setResizable(true);
            cm.getColumn(column).setMinWidth(40);
            cm.getColumn(column).setMaxWidth(100);
            cm.getColumn(column).setPreferredWidth(100);
            cm.getColumn(column).setCellRenderer(cRenderer);

            column = 5;
            cm.getColumn(column).setResizable(true);
            cm.getColumn(column).setMinWidth(40);
            cm.getColumn(column).setMaxWidth(1000);
            cm.getColumn(column).setPreferredWidth(100);

            column = 6;
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


    public synchronized void add(DownloadMetaInfo mDownload) throws IOException {
        final DefaultTableModel model = (DefaultTableModel) getTable().getModel();

        model.addRow(new Object[]{
                mDownload.getOutputFormatter().getId(),
                mDownload.getOutputFormatter().getName(),
                mDownload.getOutputFormatter().getProgress(),
                mDownload.getOutputFormatter().getCurrentSize(),
                mDownload.getOutputFormatter().getFullSize(),
                mDownload.getOutputFormatter().getStatus(),
                mDownload.getOutputFormatter().getPath(),
                mDownload.getOutputFormatter().getUrl()});

        getTable().updateUI();
    }


    public synchronized void updateTable(List<DownloadMetaInfo> showInTable) {
        final DefaultTableModel model = (DefaultTableModel) getTable().getModel();

        if (showInTable.size() >= getTable().getRowCount()) {
            int diff = showInTable.size() - getTable().getRowCount();

            for (int i = 0; i < diff; i++) {
                model.addRow(new Object[9]);
            }
        } else {
            int diff = getTable().getRowCount() - showInTable.size();

            while (diff-- > 0) {
                model.removeRow(0);
            }
        }

        for (int i = 0; i < showInTable.size(); i++) {
            try {
                DownloadMetaInfo mDownload = showInTable.get(i);
                model.setValueAt(mDownload.getOutputFormatter().getId(), i, 0);
                model.setValueAt(mDownload.getOutputFormatter().getName(), i, 1);
                model.setValueAt(mDownload.getOutputFormatter().getProgress(), i, 2);
                model.setValueAt(mDownload.getOutputFormatter().getCurrentSize(), i, 3);
                model.setValueAt(mDownload.getOutputFormatter().getFullSize(), i, 4);
                model.setValueAt(mDownload.getOutputFormatter().getSpeed(), i, 5);
                model.setValueAt(mDownload.getOutputFormatter().getStatus(), i, 6);
                model.setValueAt(mDownload.getOutputFormatter().getPath(), i, 7);
                model.setValueAt(mDownload.getOutputFormatter().getUrl(), i, 8);
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

    public LocalGroupsController getLocalGroupsController() {
        return gController;
    }
}
