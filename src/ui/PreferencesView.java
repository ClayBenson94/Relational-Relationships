package ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import objects.*;
import tables.LocationTable;
import tables.UserPhotosTable;
import tables.UserTable;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

/**
 * The user preferences view. Users can change preferences,
 * add and remove interests, and add and remove photos
 */
public class PreferencesView {
    private JPanel basePane;
    private JButton applyButton;
    private JButton backButton;
    private JTextArea biographyTextArea;
    private JComboBox sexualityComboBox;
    private JTextField zipCodeTextField;
    private JComboBox preferredSexuality;
    private JTextField preferredAgeMinTextField;
    private JTextField preferredAgeMaxTextField;
    private JList interestList;
    private JButton addInterestButton;
    private JButton removeInterestButton;
    private JList photoList;
    private JButton addPhotoButton;
    private JButton removePhotoButton;
    private JButton createInterestButton;
    private RelationshipController controller;

    /**
     * Creates the view, adds button listeners, and populates fields
     *
     * @param c the relationship controller
     */
    public PreferencesView(RelationshipController c) {
        controller = c;
        $$$setupUI$$$();
        setFieldsFromUser();

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.back();
            }
        });

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyChanges();
            }
        });

        addInterestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.openInterestSubmissionPage();
            }
        });

        removeInterestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InterestListObject o = (InterestListObject) interestList.getSelectedValue();
                if (o != null) {
                    controller.removeInterestFromUser(controller.getActiveUser(), o.getInterest());
                    refreshLists();
                }
            }
        });

        createInterestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.openInterestCreationPage();
            }
        });

        addPhotoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.openPhotoSubmissionPage();
            }
        });

        removePhotoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResultListPhotoObject o = (ResultListPhotoObject) photoList.getSelectedValue();
                if (o != null) {
                    controller.deleteUserPhoto(controller.getActiveUser(), o.getUrl());
                    refreshLists();
                }
            }
        });

    }

    /**
     * Refreshes the contents of the interest and photo lists
     */
    public void refreshLists() {
        interestList.setCellRenderer(new InterestListRenderer(controller));

        ArrayList<Interest> results = controller.getUserInterests(controller.getActiveUser());
        DefaultListModel m = new DefaultListModel();
        for (int i = 0; i < results.size(); i++) {
            m.addElement(new InterestListObject(results.get(i)));
        }
        interestList.setModel(m);

        photoList.setCellRenderer(new PreferencesPhotoRenderer());
        ArrayList<String> images = UserPhotosTable.getUserPhotos(RelationshipController.getConnection(), controller.getActiveUser());
        DefaultListModel n = new DefaultListModel();
        for (String image : images) {
            n.addElement(new ResultListPhotoObject(image));
        }
        photoList.setModel(n);
    }

    /**
     * Static method to create an instance of the preferences view
     *
     * @param c the relationship controller
     * @return the view JFrame
     */
    public static JFrame init(RelationshipController c) {
        JFrame frame = new JFrame("PreferencesView");
        PreferencesView p = new PreferencesView(c);
        frame.setContentPane(p.basePane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setSize(500, 700);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                p.refreshLists();
            }
        });
        return frame;
    }

    /**
     * Updates the current user with information from the fields on the preferences page
     */
    private void applyChanges() {
        User userToUpdate = controller.getActiveUser();

        //Input checks
        String errorString = "<html><body>";
        boolean foundError = false;


        Integer zip;
        try {
            zip = Integer.parseInt(zipCodeTextField.getText());
            if (!LocationTable.isValidZip(controller.getConnection(), zip)) {
                errorString += "Invalid zipcode.<br><br>";
                foundError = true;
            }
        } catch (NumberFormatException e1) {
            zip = null;
            errorString += "Invalid zipcode.<br><br>";
            foundError = true;
        }

        Integer min;
        try {
            min = Integer.parseInt(preferredAgeMinTextField.getText());
            if (min < 18) {
                errorString += "Minimum age must be at least 18.<br><br>";
                foundError = true;
            }
        } catch (NumberFormatException e1) {
            min = -1;
            errorString += "Invalid minimum age.<br><br>";
            foundError = true;
        }

        Integer max;
        try {
            max = Integer.parseInt(preferredAgeMaxTextField.getText());
            if (max < min) {
                errorString += "Maximum age must be greater than or equal to minimum age.<br><br>";
                foundError = true;
            }
        } catch (NumberFormatException e1) {
            max = -1;
            errorString += "Invalid maximum age.<br><br>";
            foundError = true;
        }

        if (foundError) {
            errorString += "</body></html>";
            controller.createErrorView(errorString);
            return;
        }

        userToUpdate.setBio(biographyTextArea.getText());
        userToUpdate.setLocation(Integer.parseInt(zipCodeTextField.getText()));
        UserPreferences preferencesToUpdate = new UserPreferences(min, max, (RelationshipController.Sexuality) preferredSexuality.getSelectedItem());
        userToUpdate.setUserPreferences(preferencesToUpdate);

        userToUpdate.setSexuality((RelationshipController.Sexuality) sexualityComboBox.getSelectedItem());

        UserTable.updateUser(controller.getConnection(), userToUpdate);
        controller.back();
    }

    /**
     * Sets the fields in the preference page with the information from
     * the current user
     */
    private void setFieldsFromUser() {
        User myUser = controller.getActiveUser();
        UserPreferences myPreferences = myUser.getUserPreferences();

        biographyTextArea.setText(myUser.getBio());
        zipCodeTextField.setText(Integer.toString(myUser.getLocation()));
        preferredAgeMinTextField.setText(myPreferences.getPreferredAgeMin().toString());
        preferredAgeMaxTextField.setText(myPreferences.getPreferredAgeMax().toString());

        preferredSexuality.setSelectedItem(myPreferences.getPreferredSexuality());
        sexualityComboBox.setSelectedItem(myUser.getSexuality());
    }

    /**
     * Creates dropdown boxes for sexuality and preferred sexuality
     */
    private void createUIComponents() {
        sexualityComboBox = new JComboBox(RelationshipController.Sexuality.values());
        preferredSexuality = new JComboBox(RelationshipController.Sexuality.values());
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
        basePane.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        basePane.add(panel1, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        panel1.add(backButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        applyButton = new JButton();
        applyButton.setText("Apply");
        panel1.add(applyButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(6, 2, new Insets(0, 0, 0, 0), -1, -1));
        basePane.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Biography");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        biographyTextArea = new JTextArea();
        panel2.add(biographyTextArea, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        panel2.add(sexualityComboBox, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Sexuality");
        panel2.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        zipCodeTextField = new JTextField();
        panel2.add(zipCodeTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Zip Code");
        panel2.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Preferred Sexuality");
        panel2.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Preferred Age Minimum");
        panel2.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Preferred Age Maximum");
        panel2.add(label6, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel2.add(preferredSexuality, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        preferredAgeMinTextField = new JTextField();
        panel2.add(preferredAgeMinTextField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        preferredAgeMaxTextField = new JTextField();
        panel2.add(preferredAgeMaxTextField, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        basePane.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 120), null, null, 0, false));
        interestList = new JList();
        scrollPane1.setViewportView(interestList);
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel5, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        addInterestButton = new JButton();
        addInterestButton.setText("Add Interest");
        panel5.add(addInterestButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeInterestButton = new JButton();
        removeInterestButton.setText("Remove Interest");
        panel5.add(removeInterestButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        createInterestButton = new JButton();
        createInterestButton.setText("Create Interest");
        panel5.add(createInterestButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel6, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Edit Interests");
        panel6.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        basePane.add(panel7, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel8, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel8.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 120), null, null, 0, false));
        photoList = new JList();
        photoList.setLayoutOrientation(2);
        scrollPane2.setViewportView(photoList);
        final JPanel panel9 = new JPanel();
        panel9.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel9, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        addPhotoButton = new JButton();
        addPhotoButton.setText("Add Photo");
        panel9.add(addPhotoButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removePhotoButton = new JButton();
        removePhotoButton.setText("Remove Photo");
        panel9.add(removePhotoButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel10 = new JPanel();
        panel10.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel7.add(panel10, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Edit Photos");
        panel10.add(label8, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Note: Changing preferences will not take effect until a new search is performed.");
        basePane.add(label9, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return basePane;
    }
}

/**
 * Object that holds an interest for display in a list
 */
class InterestListObject {
    private Interest interest;

    /**
     * Creates a list object from an interest
     * @param i the interest
     */
    public InterestListObject(Interest i) {
        interest = i;

    }

    /**
     * Gets a formatted string to display in the list
     * @return the formatted string
     */
    public String getPrintableString() {
        String name = interest.getName();
        String cat  = interest.getCategory();
        String des  = interest.getDescription();
        return name + "(" + cat + ") - " + des;
    }

    public Interest getInterest() {
        return interest;
    }
}

/**
 * Custom renderer for interest list objects
 */
class InterestListRenderer extends JLabel implements ListCellRenderer {
    private static final Color HIGHLIGHT_COLOR = new Color(88, 130, 255);

    /**
     * creates a interest list renderer
     * @param controller the relationship controller
     */
    public InterestListRenderer(RelationshipController controller) {
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
        InterestListObject entry = (InterestListObject) value;
        setText(entry.getPrintableString());

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

/**
 * Creates renderer for photo list objects. Only shows photo.
 */
class PreferencesPhotoRenderer extends JLabel implements ListCellRenderer {

    private static final Color HIGHLIGHT_COLOR = new Color(88, 130, 255);

    /**
     * Constructs a photo renderer
     */
    public PreferencesPhotoRenderer() {
        setOpaque(true);
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
        ResultListPhotoObject entry = (ResultListPhotoObject) value;
        setIcon(entry.getIcon());

        Border border = BorderFactory.createLineBorder(HIGHLIGHT_COLOR, 3);

        if (isSelected) {
            setBorder(border);
            setBackground(HIGHLIGHT_COLOR);
            setForeground(Color.white);
        } else {
            setBorder(null);
            setBackground(Color.white);
            setForeground(Color.black);
        }

        return this;
    }

}