package signal_parsing;

import java.awt.*;
import javax.swing.*;

public class BackgroundPanel extends JPanel {

    private Image image;
    public BackgroundPanel(Image image) {
        this(image, 0);
    }

    public BackgroundPanel(Image image, int style) {
        setImage(image);
        setLayout(new BorderLayout());
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

}
