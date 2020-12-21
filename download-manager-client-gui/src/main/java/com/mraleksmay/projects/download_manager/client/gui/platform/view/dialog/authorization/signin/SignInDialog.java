/*
 * Created by JFormDesigner on Mon Dec 07 15:26:56 EET 2020
 */

package com.mraleksmay.projects.download_manager.client.gui.platform.view.dialog.authorization.signin;

import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.ApplicationManager;
import com.mraleksmay.projects.download_manager.client.gui.platform.subsystem.communication.RESTClient;
import com.mraleksmay.projects.download_manager.common.exception.UserNotFoundException;
import com.mraleksmay.projects.download_manager.plugin.model.user.User;
import com.mraleksmay.projects.download_manager.common.util.security.SecurityUtil;
import com.mraleksmay.projects.download_manager.common.view.ADialog;
import com.mraleksmay.projects.download_manager.common.view.DialogResult;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author unknown
 */
public class SignInDialog extends ADialog {
    private ApplicationManager applicationManager;


    public SignInDialog(Window window, ApplicationManager applicationManager) {
        super(window);
        this.applicationManager = applicationManager;
        initComponents();
    }


    @Override
    public void init() {

    }

    @Override
    public DialogResult showDialog() {
        final ADialog dialog = this;
        dialog.setTitle("Sign In");
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        final Dimension size = new Dimension(300, 200);
        dialog.setPreferredSize(size);
        dialog.setSize(size);
        dialog.setModal(true);
        dialog.pack();
        dialog.setVisible(true);

        return getResult();
    }

    private void jbtn_loginActionPerformed(ActionEvent e) {
        final String login = getJTF_login().getText().trim();
        final String password = SecurityUtil.md5Java(getJTF_password().getText());

        try {
            final String token = RESTClient.UserService.signIn(login, password);
            User.getCurrentUser().setToken(token);

            this.setResult(DialogResult.OK);
            this.dispose();
        } catch (UserNotFoundException userNotFoundException) {
            JOptionPane.showMessageDialog(this, "User not exists!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void jbtn_cancelActionPerformed(ActionEvent e) {
        this.setResult(DialogResult.CANCEL);
        this.dispose();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        // Generated using JFormDesigner Evaluation license - unknown
        panel1 = new JPanel();
        label1 = new JLabel();
        jtf_login = new JTextField();
        label2 = new JLabel();
        jtf_password = new JPasswordField();
        jbtn_login = new JButton();
        jbtn_cancel = new JButton();

        //======== this ========
        var contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout) contentPane.getLayout()).columnWidths = new int[]{0, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).rowHeights = new int[]{0, 0, 0, 0};
        ((GridBagLayout) contentPane.getLayout()).columnWeights = new double[]{1.0, 1.0, 1.0E-4};
        ((GridBagLayout) contentPane.getLayout()).rowWeights = new double[]{0.0, 1.0, 0.0, 1.0E-4};

        //======== panel1 ========
        {
            panel1.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.
                    swing.border.EmptyBorder(0, 0, 0, 0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn", javax.swing.border
                    .TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dia\u006cog"
                    , java.awt.Font.BOLD, 12), java.awt.Color.red), panel1.getBorder
                    ()));
            panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
                @Override
                public void propertyChange(java
                                                   .beans.PropertyChangeEvent e) {
                    if ("\u0062ord\u0065r".equals(e.getPropertyName())) throw new RuntimeException
                            ();
                }
            });
            panel1.setLayout(new GridBagLayout());
            ((GridBagLayout) panel1.getLayout()).columnWidths = new int[]{0, 0};
            ((GridBagLayout) panel1.getLayout()).rowHeights = new int[]{0, 0, 0, 0, 0};
            ((GridBagLayout) panel1.getLayout()).columnWeights = new double[]{1.0, 1.0E-4};
            ((GridBagLayout) panel1.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0E-4};

            //---- label1 ----
            label1.setText("Login:");
            panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));
            panel1.add(jtf_login, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

            //---- label2 ----
            label2.setText("Password:");
            panel1.add(label2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));
            panel1.add(jtf_password, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
        }
        contentPane.add(panel1, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

        //---- jbtn_login ----
        jbtn_login.setText("Login");
        jbtn_login.addActionListener(e -> jbtn_loginActionPerformed(e));
        contentPane.add(jbtn_login, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

        //---- jbtn_cancel ----
        jbtn_cancel.setText("Cancel");
        jbtn_cancel.addActionListener(e -> jbtn_cancelActionPerformed(e));
        contentPane.add(jbtn_cancel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }


    public JTextField getJTF_login() {
        return jtf_login;
    }

    public JPasswordField getJTF_password() {
        return jtf_password;
    }

    public ApplicationManager getApplicationManager() {
        return applicationManager;
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - unknown
    private JPanel panel1;
    private JLabel label1;
    private JTextField jtf_login;
    private JLabel label2;
    private JPasswordField jtf_password;
    private JButton jbtn_login;
    private JButton jbtn_cancel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
