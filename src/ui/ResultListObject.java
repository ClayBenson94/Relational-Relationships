package ui;

import objects.RelationshipController;
import objects.User;
import tables.UserPhotosTable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Clay on 4/12/2016.
 */
class ResultListObject {
    private ImageIcon icon;
    private User user;

    public ResultListObject(User u) {
        user = u;
        BufferedImage myPicture = null;
        try {
            ArrayList<String> images = UserPhotosTable.getUserPhotos(RelationshipController.getConnection(), user);
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
        return user.getUsername();
    }
}
