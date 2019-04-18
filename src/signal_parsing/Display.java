/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signal_parsing;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author Pat
 */
public class Display extends JFrame {

    private ImageIcon image1;
    private JLabel label1;

    public Display() {
        super("Display");
        image1 = new ImageIcon(getClass().getResource("burning.jpg"));
        label1 = new JLabel(image1);
        setLayout(new BorderLayout());
        setSize(1920, 1080);
        setContentPane(label1);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);


    }

}

