package ui;

import objects.RelationshipController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by rob on 4/12/16.
 */
public class LikesView {
    private JButton backButton;
    private JList likesList;
    private JList matchesList;
    private JPanel basePane;

    private RelationshipController controller;

    public LikesView(RelationshipController c) {
        controller = c;

        likesList.setCellRenderer(new UserListRenderer(controller));
        matchesList.setCellRenderer(new UserListRenderer(controller));

        //TODO populate lists


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.back();
            }
        });
    }

    public static JFrame init(RelationshipController c) {
        JFrame frame = new JFrame("LikesView");
        frame.setContentPane(new LikesView(c).basePane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        return frame;
    }

}
