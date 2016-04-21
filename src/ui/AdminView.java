package ui;

import objects.RelationshipController;
import tables.UserTable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class AdminView {
    private JLabel deleteUserLbl;
    private JTextField usernameField;
    private JButton deleteBttn;
    private JPanel basePane;
    private JButton backBttn;
    private JLabel deleteMsg;
    private RelationshipController controller;


    public AdminView(RelationshipController c) {
        controller = c;

        deleteBttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMsg.setText(UserTable.deleteUser(RelationshipController.getConnection(), usernameField.getText(), controller.getActiveUser().getUsername()));
            }
        });

        backBttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.back();
            }
        });
    }

    public static JFrame init(RelationshipController c) {
        JFrame frame = new JFrame("AdminView");
        frame.setContentPane(new AdminView(c).basePane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(450, 200);
        return frame;
    }


}


