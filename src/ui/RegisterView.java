package ui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import objects.RelationshipController;
import objects.RelationshipController.Gender;
import objects.RelationshipController.Sexuality;
import objects.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.*;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import tables.LocationTable;
import tables.UserTable;

public class RegisterView {
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private JPanel basePane;
    private JButton backButton;
    private JTextField nameTextField;
    private JTextField email;
    private JTextField dateOfBirth;
    private JComboBox genderComboBox;
    private JComboBox sexualityComboBox;
    private JTextField zipCodeTextField;
    private JButton registerButton;
    private JComboBox preferredSexuality;
    private JTextField preferredAgeMinTextField;
    private JTextField preferredAgeMaxTextField;
    private JTextArea biographyTextArea;
    private RelationshipController controller;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public RegisterView(RelationshipController controller, String username, String password) {
        this.controller = controller;
        $$$setupUI$$$();
        if (!username.isEmpty()) {
            usernameTextField.setText(username);
        }
        if (!password.isEmpty()) {
            passwordTextField.setText(password);
        }

        backButton.addActionListener(controller.backListener(controller));
        registerButton.addActionListener(registerListener());
    }

    public static JFrame init(RelationshipController controller, String username, String password) {
        JFrame frame = new JFrame("RegisterView");
        frame.setContentPane(new RegisterView(controller, username, password).basePane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        return frame;
    }

    private ActionListener registerListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //Input checks
                String errorString = "<html><body>";
                boolean foundError = false;

                String username = usernameTextField.getText();
                if (!UserTable.isAvailableUsername(controller.getConnection(), username)) {
                    errorString += "Username " + username + " is already taken. Please choose another.<br><br>";
                    foundError = true;
                }
                if (username.length() > 20) {
                    errorString += "Username must be 20 characters or less.<br><br>";
                    foundError = true;
                }
                if (username.length() == 0) {
                    errorString += "Username cannot be empty.<br><br>";
                    foundError = true;
                }

                String password = new String(passwordTextField.getPassword());
                if (password.length() > 32) {
                    errorString += "Password must be 32 characters or less.<br><br>";
                    foundError = true;
                }
                if (password.length() == 0) {
                    errorString += "Password cannot be empty.<br><br>";
                    foundError = true;
                }

                String name = nameTextField.getText();
                if (name.length() > 255) {
                    errorString += "Wow, that's a long name.<br><br>";
                    foundError = true;
                }

                String emailString = email.getText();
                Pattern p = Pattern.compile(EMAIL_PATTERN);
                Matcher m = p.matcher(emailString);
                if (!m.matches()) {
                    errorString += "Invalid email address.<br><br>";
                    foundError = true;
                }
                if (emailString.length() > 255) {
                    errorString += "Wow, that's a long email.<br><br>";
                    foundError = true;
                }

                Date dob;
                try {
                    dob = new Date(dateFormat.parse(dateOfBirth.getText()).getTime());
                    LocalDate tDate = dob.toLocalDate();
                    LocalDate today = LocalDate.now();
                    long years = Period.between(tDate, today).getYears();
                    if (years < 18) {
                        errorString += "You must be over 18 to register.<br><br>";
                        foundError = true;
                    }

                } catch (ParseException e1) {
                    //e1.printStackTrace();
                    dob = null;
                    errorString += "Invalid date. Use yyyy-MM-dd.<br><br>";
                    foundError = true;
                }

                Integer zip;
                try {
                    zip = Integer.parseInt(zipCodeTextField.getText());
                    if (!LocationTable.isValidZip(controller.getConnection(), zip)) {
                        errorString += "Invalid zipcode.<br><br>";
                        foundError = true;
                    }
                } catch (NumberFormatException e1) {
                    //e1.printStackTrace();
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
                    //e1.printStackTrace();
                    min = -1;
                    errorString += "Invalid minimum age.<br><br>";
                    foundError = true;
                }

                Integer max;
                try {
                    max = Integer.parseInt(preferredAgeMaxTextField.getText());
                    if (max < min) {
                        errorString += "Maximum age must be greater than minimum age.<br><br>";
                        foundError = true;
                    }
                } catch (NumberFormatException e1) {
                    //e1.printStackTrace();
                    max = -1;
                    errorString += "Invalid maximum age.<br><br>";
                    foundError = true;
                }

                if (foundError) {
                    errorString += "</body></html>";
                    controller.createErrorView(errorString);
                    return;
                }

                controller.register(new User(
                        username,
                        password,
                        name,
                        biographyTextArea.getText(),
                        emailString,
                        dob,
                        (Gender) genderComboBox.getSelectedItem(),
                        (Sexuality) sexualityComboBox.getSelectedItem(),
                        zip,
                        min,
                        max,
                        (Sexuality) preferredSexuality.getSelectedItem(),
                        false));
            }
        };
    }

    private void createUIComponents() {
        genderComboBox = new JComboBox(Gender.values());
        sexualityComboBox = new JComboBox(Sexuality.values());
        preferredSexuality = new JComboBox(Sexuality.values());
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
        basePane.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(12, 2, new Insets(0, 0, 0, 0), -1, -1));
        basePane.add(panel1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Username");
        panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        usernameTextField = new JTextField();
        panel1.add(usernameTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Password");
        panel1.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Name");
        panel1.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nameTextField = new JTextField();
        panel1.add(nameTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Email");
        panel1.add(label4, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        email = new JTextField();
        panel1.add(email, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Date of Birth");
        panel1.add(label5, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        dateOfBirth = new JTextField();
        dateOfBirth.setText("yyyy-mm-dd");
        panel1.add(dateOfBirth, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Gender");
        panel1.add(label6, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1.add(genderComboBox, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Sexuality");
        panel1.add(label7, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1.add(sexualityComboBox, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Zip Code");
        panel1.add(label8, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        zipCodeTextField = new JTextField();
        panel1.add(zipCodeTextField, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        passwordTextField = new JPasswordField();
        panel1.add(passwordTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Preferred Sexuality");
        panel1.add(label9, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panel1.add(preferredSexuality, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Preferred Age Minimum");
        panel1.add(label10, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Preferred Age Maximum");
        panel1.add(label11, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        preferredAgeMinTextField = new JTextField();
        panel1.add(preferredAgeMinTextField, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        preferredAgeMaxTextField = new JTextField();
        panel1.add(preferredAgeMaxTextField, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Biography");
        panel1.add(label12, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        biographyTextArea = new JTextArea();
        panel1.add(biographyTextArea, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        basePane.add(backButton, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        registerButton = new JButton();
        registerButton.setText("Register");
        basePane.add(registerButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return basePane;
    }
}

