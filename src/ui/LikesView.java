package ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import objects.RelationshipController;
import tables.UserPhotosTable;
import tables.UserTable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import objects.Like;
import java.util.Date;

public class LikesView {
    private JButton backButton;
    private JList likesList;
    private JList matchesList;
    private JPanel basePane;

    private RelationshipController controller;

    public LikesView(RelationshipController c) {
        controller = c;

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.back();
            }
        });

        likesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LikedListObject likedListObject = (LikedListObject) likesList.getSelectedValue();
                if (likedListObject != null) {
                    controller.createVisit(UserTable.getUserObject(RelationshipController.getConnection(),
                            likedListObject.getName()), controller.getActiveUser());
                }

            }
        });

        matchesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                LikedListObject matchesListObject = (LikedListObject) matchesList.getSelectedValue();
                if (matchesListObject != null) {
                    controller.createVisit(UserTable.getUserObject(RelationshipController.getConnection(),
                            matchesListObject.getName()), controller.getActiveUser());
                }

            }
        });
    }

    protected void getLikesAndMatches() {
        likesList.setCellRenderer(new LikedListRenderer(controller));
        matchesList.setCellRenderer(new MatchListRenderer(controller));

        ArrayList<Like> results = controller.getMatches(controller.getActiveUser());
        DefaultListModel m = new DefaultListModel();
        for (int i = 0; i < results.size(); i++) {
            m.addElement(new LikedListObject(results.get(i)));
        }
        matchesList.setModel(m);

        results = controller.getLikes(controller.getActiveUser());
        m = new DefaultListModel();
        for (int i = 0; i < results.size(); i++) {
            m.addElement(new LikedListObject(results.get(i)));
        }
        likesList.setModel(m);
    }

    public static JFrame init(RelationshipController c) {
        JFrame frame = new JFrame("LikesView");
        LikesView likesView = new LikesView(c);
        JPanel basePane = likesView.basePane;
        frame.setContentPane(basePane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                likesView.getLikesAndMatches();
            }
        });
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
        basePane.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        basePane.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Who Likes Me?");
        panel2.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        basePane.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel4 = new JPanel();
        panel4.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel5 = new JPanel();
        panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel4.add(panel5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Likes Me");
        panel5.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        panel4.add(scrollPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        likesList = new JList();
        scrollPane1.setViewportView(likesList);
        final JPanel panel6 = new JPanel();
        panel6.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel3.add(panel6, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JPanel panel7 = new JPanel();
        panel7.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel6.add(panel7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Matches");
        panel7.add(label3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        panel6.add(scrollPane2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        matchesList = new JList();
        scrollPane2.setViewportView(matchesList);
        final JPanel panel8 = new JPanel();
        panel8.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        basePane.add(panel8, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        backButton = new JButton();
        backButton.setText("Back");
        panel8.add(backButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel8.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return basePane;
    }

    static class LikedListObject {
        private ImageIcon icon;
        private Like like;

        public LikedListObject(Like l) {
            like = l;
            BufferedImage myPicture = null;
            try {
                Connection conn = RelationshipController.getConnection();
                ArrayList<String> images = UserPhotosTable.getUserPhotos(conn, UserTable.getUserObject(conn, l.getUsername()));
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
            Long time = like.getTimestamp();
            Date date = new Date(time);
            return date.toString() + " | " + getName();
        }
    }


    static class LikedListRenderer extends JLabel implements ListCellRenderer {
        private static final Color HIGHLIGHT_COLOR = new Color(88, 130, 255);

        public LikedListRenderer(RelationshipController controller) {
            setOpaque(true);
            setIconTextGap(12);
        }

        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            LikedListObject entry = (LikedListObject) value;
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

    static class MatchListRenderer extends JLabel implements ListCellRenderer {
        private static final Color HIGHLIGHT_COLOR = new Color(88, 130, 255);

        public MatchListRenderer(RelationshipController controller) {
            setOpaque(true);
            setIconTextGap(12);
        }

        public Component getListCellRendererComponent(JList list, Object value,
                                                      int index, boolean isSelected, boolean cellHasFocus) {
            LikedListObject entry = (LikedListObject) value;
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
