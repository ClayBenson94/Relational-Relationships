package ui;

import objects.RelationshipController;
import objects.Interest;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddInterestView {
    private JButton addButton;
    private JButton backButton;
    private JTextField categoryField;
    private JTextField nameField;
    private JTextArea descArea;

    private RelationshipController controller;

    public AddInterestView(RelationshipController c) {
        controller = c;

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.back();
            }
        });
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String category = categoryField.getText().toLowerCase();
                String name = nameField.getText().toLowerCase();
                String desc = descArea.getText().toLowerCase();

                Interest i = new Interest(name, desc, category);

            }
        });
    }
}
