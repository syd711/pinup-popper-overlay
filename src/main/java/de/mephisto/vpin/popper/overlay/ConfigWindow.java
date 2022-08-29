package de.mephisto.vpin.popper.overlay;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ConfigWindow extends JFrame implements KeyListener {

  public ConfigWindow() throws Exception {
    UIManager.setLookAndFeel(
        UIManager.getCrossPlatformLookAndFeelClassName());

    this.addKeyListener(this);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


    // creating a button
    JButton b = new JButton("Click Me!!");

    // setting button position on screen
    b.setBounds(30,100,80,30);

    // adding button into frame
    add(b);

    // frame size 300 width and 300 height
    setSize(300,300);
    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
    int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
    setLocation(x, y);

    // setting the title of Frame
    setTitle("This is our basic AWT example");

    // no layout manager
    setLayout(null);

    // now frame will be visible, by default it is not visible
    setVisible(true);
  }

  public static void main(String[] args) throws Exception {
    new ConfigWindow();
  }

  @Override
  public void keyTyped(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      System.exit(0);
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {

  }

  @Override
  public void keyReleased(KeyEvent e) {

  }
}
