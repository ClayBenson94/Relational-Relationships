package ui;

import objects.RelationshipController;
import objects.User;
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


    private RelationshipController controller;

    public VisitingUserView(RelationshipController c) {
        controller = c;

        userPhotos.setCellRenderer(new UserPhotoRenderer());
        likeBttn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO like the user and change the button text OR unlike the user and change the button text
            }
        });
        ArrayList<String> images = UserPhotosTable.getUserPhotos(RelationshipController.getConnection(), controller.getVisitingUser());
        DefaultListModel m = new DefaultListModel();
        if (images.size() == 0) {
            m.addElement(new ResultListPhotoObject("resources/images/logo.png"));
        } else {
            for (String image : images){
                m.addElement(new ResultListPhotoObject(image));
            }
        }
        userPhotos.setModel(m);
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

