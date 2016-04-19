package ui;

import objects.RelationshipController;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Clay on 4/12/2016.
 */
class LikedListRenderer extends JLabel implements ListCellRenderer {
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
