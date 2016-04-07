package ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import objects.RelationshipController;
import tables.LikesTable;
import tables.UserPhotosTable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class VisitingUserView {
    private JList userPhotos;
    private JTextArea userInfo;
    private JPanel basePane;
    private JButton likeBttn;
    private JButton backBttn;


    private RelationshipController controller;

    public VisitingUserView(RelationshipController c) {
        controller = c;

        userPhotos.setCellRenderer(new UserPhotoRenderer());
        likeBttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (likeBttn.getText().equals("Like")) {
                    LikesTable.createLike(RelationshipController.getConnection(),
                      controller.getActiveUser().getUsername(), controller.getVisitingUser().getUsername());
                    likeBttn.setText("Unlike");
                } else {
                    LikesTable.deleteLike(RelationshipController.getConnection(),
                      controller.getActiveUser().getUsername(), controller.getVisitingUser().getUsername());
                    likeBttn.setText("Like");
                }
            }
        });
        backBttn.addActionListener(controller.backListener(controller));
        ArrayList<String> images = UserPhotosTable.getUserPhotos(RelationshipController.getConnection(), controller.getVisitingUser());
        DefaultListModel m = new DefaultListModel();
        if (images.size() == 0) {
            m.addElement(new ResultListPhotoObject("resources/images/logo.png"));
        } else {
            for (String image : images) {
                m.addElement(new ResultListPhotoObject(image));
            }
        }
        userPhotos.setModel(m);
        userInfo.setText(controller.getVisitingUser().getUserString());

        if (LikesTable.doesUserLike(RelationshipController.getConnection(), controller.getActiveUser().getUsername(),
          controller.getVisitingUser().getUsername())) {
            likeBttn.setText("Unlike");
        }
    }

    public static JFrame init(RelationshipController c, JFrame previousWindow) {
        JFrame frame = new JFrame("VisitingUserView");
        frame.setContentPane(new VisitingUserView(c).basePane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(previousWindow);
        frame.setVisible(true);
        return frame;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        basePane = new JPanel();
        basePane.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
        userInfo = new JTextArea();
        basePane.add(userInfo, new GridConstraints(1, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        likeBttn = new JButton();
        likeBttn.setText("Like");
        basePane.add(likeBttn, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backBttn = new JButton();
        backBttn.setText("Back");
        basePane.add(backBttn, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userPhotos = new JList();
        final DefaultListModel defaultListModel1 = new DefaultListModel();
        userPhotos.setModel(defaultListModel1);
        basePane.add(userPhotos, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return basePane;
    }
}

class ResultListPhotoObject {
        private ImageIcon icon;

        public ResultListPhotoObject(String photo) {
            BufferedImage myPicture = null;
            try {
                if (photo.contains("logo.png")) {
                    myPicture = ImageIO.read(new File("resources/images/logo.png"));
                } else {
                    URL url = new URL(photo);
                    myPicture = ImageIO.read(url);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            //resize
            double factor = (double)100/(double)myPicture.getHeight();

            Image newimg = myPicture.getScaledInstance((int)(myPicture.getWidth()*factor),
                    (int)(myPicture.getHeight()*factor),  java.awt.Image.SCALE_SMOOTH);
            //
            icon = new ImageIcon(newimg);
        }
        public ImageIcon getIcon()
        {
            return icon;
        }
    }

class UserPhotoRenderer extends JLabel implements ListCellRenderer {

    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected, boolean cellHasFocus) {
        ResultListPhotoObject entry = (ResultListPhotoObject) value;
        setIcon(entry.getIcon());
        return this;
    }

}

