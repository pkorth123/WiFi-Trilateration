package WiFi_Trilateration;

import java.awt.*;
import javax.swing.*;

public class BackgroundImage extends JPanel {

    private Image image;
    public BackgroundImage(Image image) {
        this(image, 0);
    }

    public BackgroundImage(Image image, int style) {
        setImage(image);
        setLayout(new BorderLayout());
    }

    public void setImage(Image image) {
        this.image = image;
        repaint();
    }

}
