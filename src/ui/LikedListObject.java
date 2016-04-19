package ui;

import objects.RelationshipController;
import objects.User;
import objects.Like;
import tables.UserPhotosTable;
import tables.UserTable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;

class LikedListObject {
    private ImageIcon icon;
    private Like like;

    public LikedListObject(Like l) {
        like = l;
        BufferedImage myPicture = null;
        try {
            Connection conn = RelationshipController.getConnection();
            ArrayList<String> images = UserPhotosTable.getUserPhotos(conn, UserTable.getUserObject(conn,l.getUsername()));
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
        return like.getUsername();
    }

    public String getPrintableString() {
        return like.getTimestamp() + " | " + getName();
    }
}
