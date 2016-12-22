package gameView;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * 
 * @author Matthias Gerzner <matthias.gerzner@stud.hslu.ch>
 */

public class Circle extends JPanel {

    private int color;

    public Circle(int color) {
        super();
        this.color = color;
        setBackground(new java.awt.Color(0, 0, 200));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        switch (color) {
            case 1:
                g.drawRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.red);
                g.fillOval(20, 20, getWidth()-40, getHeight()-40);
                
                break;
            case 2:
                g.drawRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.yellow);
                g.fillOval(20, 20, getWidth()-40, getHeight()-40);
                break;
            default:
                g.drawRect(0, 0, getWidth(), getHeight());
                g.setColor(Color.white);
                g.fillOval(20, 20, getWidth()-40, getHeight()-40);
                
                break;
        }

        //g.setColor(Color.white);
        //g.fillOval(20, 20, getWidth()-40, getHeight()-40);
        //g.drawRect(0, 0, getWidth(), getHeight());
    }

}
   