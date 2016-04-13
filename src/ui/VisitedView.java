package ui;

import objects.RelationshipController;
import objects.User;
import tables.UserTable;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Created by Clay on 4/12/2016.
 */
public class VisitedView {
    private JButton backBttn;
    private JList visitedList;
    private JPanel basePane;
    private RelationshipController controller;

    public VisitedView(RelationshipController c) {
        controller = c;
        backBttn.addActionListener(controller.backListener(controller));
        populateVisits(Integer.toString(c.getActiveUser().getLocation()));
        visitedList.setCellRenderer(new UserListRenderer(controller));

        visitedList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ResultListObject resultListObject = (ResultListObject) visitedList.getSelectedValue();
                controller.createVisit(controller.getActiveUser(),
                        UserTable.getUserObject(RelationshipController.getConnection(), resultListObject.getName()));
            }
        });

    }

    public static JFrame init(RelationshipController c) {
        JFrame frame = new JFrame("VisitedView");
        frame.setContentPane(new VisitedView(c).basePane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(700, 700);
        return frame;
    }

    public void populateVisits(String zipCode) {
//        ArrayList<User> results = controller.search(zipCode);
        //TODO Implement getting visits instead of search (search is temporary)
        ArrayList<User> results = controller.search(zipCode);
        DefaultListModel m = new DefaultListModel();
        for (int i = 0; i < results.size(); i++) {
            m.addElement(new ResultListObject(results.get(i)));
        }
        visitedList.setModel(m);
    }

}
