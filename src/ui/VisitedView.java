package ui;

import objects.RelationshipController;
import objects.User;
import objects.Visit;
import tables.UserPhotosTable;
import tables.UserTable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;

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
        populateVisits(c.getActiveUser());
        visitedList.setCellRenderer(new VisitListRenderer(controller));

        visitedList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                VisitedListObject visitListObject = (VisitedListObject) visitedList.getSelectedValue();
                if (visitListObject != null) {
                    controller.createVisit(UserTable.getUserObject(RelationshipController.getConnection(),
                            visitListObject.getName()), controller.getActiveUser());
                }
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

    public void populateVisits(User curUser) {
        //TODO Implement getting visits instead of search (search is temporary)
//        ArrayList<User> results = controller.search(zipCode);
        ArrayList<Visit> results = controller.getVisitsForUser(curUser);
        DefaultListModel m = new DefaultListModel();
        for (int i = 0; i < results.size(); i++) {
            m.addElement(new VisitedListObject(results.get(i)));
        }
        visitedList.setModel(m);
    }

    /**
     * Created by Clay on 4/12/2016.
     */
    static class VisitedListObject {
        private ImageIcon icon;
        private Visit visit;

        public VisitedListObject(Visit v) {
            visit = v;
            BufferedImage myPicture = null;
            try {
                Connection conn = RelationshipController.getConnection();
                ArrayList<String> images = UserPhotosTable.getUserPhotos(conn, UserTable.getUserObject(conn,v.getUsername()));
                if (images.size() == 0) {
                    myPicture = ImageIO.read(new File("resources/images/logo.png"));
                } else {

                    URL url = new URL(images.get(0));
                    myPicture = ImageIO.read(url);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            //resize
            double factor = (double) 100 / (double) myPicture.getHeight();

            Image newimg = myPicture.getScaledInstance((int) (myPicture.getWidth() * factor), (int) (myPicture.getHeight() * factor), Image.SCALE_SMOOTH);
            //
            icon = new ImageIcon(newimg);
        }

        public ImageIcon getIcon() {
            return icon;
        }

        public String getName() {
            return visit.getUsername();
        }

        public String getPrintableString() {
            long timestamp = visit.getTimestamp();
            Date myDate = new Date(timestamp);

            return myDate.toString() + " | " + getName();
        }
    }

    /**
     * Created by Clay on 4/12/2016.
     */
    static class VisitListRenderer extends JLabel implements ListCellRenderer {
        private static final Color HIGHLIGHT_COLOR = new Color(88, 130, 255);

        public VisitListRenderer(RelationshipController controller) {
            setOpaque(true);
            setIconTextGap(12);
        }

        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            VisitedListObject entry = (VisitedListObject) value;
            setText(entry.getPrintableString());

            setIcon(entry.getIcon());
            if (isSelected) {
                setBackground(HIGHLIGHT_COLOR);
                setForeground(Color.white);
            } else {
                setBackground(Color.white);
                setForeground(Color.black);
            }
            return this;
        }
    }
}
