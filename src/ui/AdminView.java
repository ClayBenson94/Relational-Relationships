package ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import objects.RelationshipController;
import tables.UserTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class AdminView {
    private JLabel deleteUserLbl;
    private JTextField usernameField;
    private JButton deleteBttn;
    private JPanel basePane;
    private JButton backBttn;
    private JLabel deleteMsg;
    private JLabel statBttn;
    private JLabel likesLbl;
    private JLabel visitsLbl;
    private JLabel matchesLbl;
    private JComboBox<String> timeCombo;
    private JLabel visitsValueLbl;
    private JLabel likesValueLbl;
    private JLabel matchesValueLbl;
    private RelationshipController controller;


    public AdminView(RelationshipController c) {
        controller = c;

        String[] timeSelections = {"Hour", "Day", "Week", "Month", "Year"};
        timeCombo.setModel(new DefaultComboBoxModel<>(timeSelections));
        updateStatLabels();

        ActionListener deleteListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        };

        deleteBttn.addActionListener(deleteListener);
        usernameField.addActionListener(deleteListener);

        backBttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.back();
            }
        });

        timeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStatLabels();
            }
        });
    }

    public void deleteUser() {
        deleteMsg.setText(UserTable.deleteUser(RelationshipController.getConnection(), usernameField.getText(), controller.getActiveUser().getUsername()));
    }

    public static JFrame init(RelationshipController c) {
        JFrame frame = new JFrame("AdminView");
        frame.setContentPane(new AdminView(c).basePane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500, 300);
        return frame;
    }

    private void updateStatLabels(){
        visitsValueLbl.setText(Integer.toString(controller.getVisitCount((String) timeCombo.getSelectedItem())));
        likesValueLbl.setText(Integer.toString(controller.getLikeCount((String) timeCombo.getSelectedItem())));
        matchesValueLbl.setText(Integer.toString(controller.getMatchCount((String) timeCombo.getSelectedItem())));
    }
}


