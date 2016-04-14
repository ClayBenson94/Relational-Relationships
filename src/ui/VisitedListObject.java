package ui;

import objects.RelationshipController;
import objects.User;
import objects.Visit;
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

/**
 * Created by Clay on 4/12/2016.
 */
class VisitedListObject {
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
        return visit.getTimestamp() + " | " + getName();
    }
}
