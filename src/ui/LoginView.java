package ui;

import objects.RelationshipController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoginView implements View {
    private JButton registerButton;
    private JButton loginButton;
    private JPasswordField passwordField;
    private JTextField usernameField;
    private RelationshipController controller;

    public LoginView(RelationshipController c) {
        controller = c;

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //do login
                //controller.login(usernameField.getText(), passwordField.getPassword());

            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //do register
                //controller.register()
            }
        });
    }



}
