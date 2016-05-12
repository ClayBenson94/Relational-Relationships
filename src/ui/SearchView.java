package ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import objects.RelationshipController;
import objects.User;
import tables.UserPhotosTable;
import tables.UserTable;
import tables.LocationTable;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.net.URL;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Search view allows users to search for other users based on preferences
 */
public class SearchView {
    private JList resultsList;
    private JButton searchButton;
    private JTextField zipcodeField;
    private JPanel basePane;
    private JButton visitedButton;
    private JButton preferencesButton;
    private JButton likesButton;
    private JButton adminButton;
    private JButton logoutButton;
    private JButton nextSearchPageButton;
    private JButton prevSearchPageButton;

    private RelationshipController controller;
    private String validZip;

    private static int currentOffset = 0;
    private static final ImageIcon IMAGE_ICON = new ImageIcon("resources/images/no_profile.gif");

    /**
     * Creates a search view
     * @param c the relationship controller
     */
    public SearchView(RelationshipController c) {
        controller = c;
        validZip = null;

        $$$setupUI$$$();
        if (!controller.getActiveUser().getIsAdmin()) {
            adminButton.setVisible(false);
        }

        ActionListener searchListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        };

        resultsList.setCellRenderer(new UserListRenderer(controller));
        searchButton.addActionListener(searchListener);
        zipcodeField.addActionListener(searchListener);
        visitedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.openVisitPage();
            }
        });


        resultsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ResultListObject resultListObject = (ResultListObject) resultsList.getSelectedValue();
                controller.createVisit(UserTable.getUserObject(RelationshipController.getConnection(),
                        resultListObject.getName()), controller.getActiveUser());
            }
        });
        likesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.openLikedPage();
            }
        });

        preferencesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.openPreferencesPage();
            }
        });

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.openAdminPage();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.logout();
            }
        });

        nextSearchPageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentOffset += RelationshipController.OFFSET_COUNT;
                prevSearchPageButton.setVisible(true);
                performSearch();
            }
        });

        prevSearchPageButton.addActionListener(e -> {
            currentOffset = Math.max(0, currentOffset -= RelationshipController.OFFSET_COUNT);
            if (currentOffset == 0) {
                prevSearchPageButton.setVisible(false);
            }
            performSearch();
        });
    }

    /**
     * Static method to create an instance of the search view
     * @param c the relationship controller
     * @return the search view JFrame
     */
    public static JFrame init(RelationshipController c) {
        JFrame frame = new JFrame("SearchView");
        SearchView searchView = new SearchView(c);
        frame.setContentPane(searchView.basePane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(400, 600);
        searchView.performSearch();
        return frame;
    }

    /**
     * gets the user location as a string
     * @return the user zip string
     */
    private String getMyZip() {
        return Integer.toString(controller.getActiveUser().getLocation());
    }

    /**
     * performs a search using the zipcode in the zipcode field or the default zipcode
     */
    public void performSearch() {
        String zipCode = zipcodeField.getText().equals("") ? getMyZip() : zipcodeField.getText();

        if (zipCode != validZip) {
            try {
                Integer zip = Integer.parseInt(zipCode);
                if (!LocationTable.isValidZip(controller.getConnection(), zip)) {
                    String errorString = "<html><body>Invalid zipcode.<br><br></body></html>";
                    controller.createErrorView(errorString);
                    return;
                }
            } catch (NumberFormatException e1) {
                String errorString = "<html><body>Invalid zipcode.<br><br></body></html>";
                controller.createErrorView(errorString);
                return;
            }
        }
        validZip = zipCode;

        ArrayList<User> results = controller.search(zipCode, currentOffset);
        DefaultListModel m = new DefaultListModel();
        for (int i = 0; i < results.size(); i++) {
            m.addElement(new ResultListObject(results.get(i)));
        }

        resultsList.setModel(m);
        if (m.size() < RelationshipController.OFFSET_COUNT) {
            nextSearchPageButton.setVisible(false);
        } else {
            nextSearchPageButton.setVisible(true);
        }
    }

    /**
     * creates the previous and next buttons
     */
    private void createUIComponents() {
        nextSearchPageButton = new JButton();
        prevSearchPageButton = new JButton();
        nextSearchPageButton.setVisible(false);
        prevSearchPageButton.setVisible(false);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        basePane = new JPanel();
        basePane.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        basePane.add(panel1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Search Options");
        panel2.add(label1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        adminButton = new JButton();
        adminButton.setText("Admin");
        panel2.add(adminButton, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        logoutButton = new JButton();
        logoutButton.setText("Logout");
        panel2.add(logoutButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Zip Code:");
        panel3.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        zipcodeField = new JTextField();
        panel3.add(zipcodeField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        searchButton = new JButton();
        searchButton.setText("Search");
        panel3.add(searchButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        visitedButton = new JButton();
        visitedButton.setText("Visits");
        panel4.add(visitedButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        preferencesButton = new JButton();
        preferencesButton.setText("Preferences");
        panel4.add(preferencesButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        likesButton = new JButton();
        likesButton.setText("My Likes");
        panel4.add(likesButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        basePane.add(panel5, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel5.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        resultsList = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        resultsList.setModel(defaultListModel1);
        scrollPane1.setViewportView(resultsList);
        nextSearchPageButton.setText("Next Page");
        basePane.add(nextSearchPageButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        prevSearchPageButton.setText("Prev Page");
        basePane.add(prevSearchPageButton, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return basePane;
    }

    /**
     * List object to display a user in the result list
     */
    static class ResultListObject {
        private ImageIcon icon;
        private User user;

        /**
         * Creates a list object from a given user
         * @param u the user
         */
        public ResultListObject(User u) {
            user = u;
            ImageIcon imageIcon = null;
            try {
                ArrayList<String> images = UserPhotosTable.getUserPhotos(RelationshipController.getConnection(), user);
                if (images.size() == 0) {
                    imageIcon = IMAGE_ICON;
                } else {
                    URL url = new URL(images.get(0));
                    imageIcon = new ImageIcon(url);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (imageIcon != null) {
                Image newImg = imageIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
                icon = new ImageIcon(newImg);
            }
        }

        public ImageIcon getIcon() {
            return icon;
        }

        public String getName() {
            return user.getUsername();
        }
    }

    /**
     * User list renderer renders users in the search result list
     */
    static class UserListRenderer extends JLabel implements ListCellRenderer {
        private static final Color HIGHLIGHT_COLOR = new Color(88, 130, 255);

        /**
         * creates the user list renderer
         * @param controller the relationship controller
         */
        public UserListRenderer(RelationshipController controller) {
            setOpaque(true);
            setIconTextGap(12);
        }

        /**
         * formats and returns the renderer for a given list object
         * @param list the list
         * @param value the object to be rendered
         * @param index objects index in the list
         * @param isSelected boolean representing whether the object is selected
         * @param cellHasFocus boolean representing whether the cell has focus
         * @return the formatted renderer
         */
        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            ResultListObject entry = (ResultListObject) value;
            setText(entry.getName());

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
