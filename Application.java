import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

public class Application extends JFrame
{   
    float i = 0;
    final BufferStrategy bufferStrategy = getBufferStrategy();
    JSplitPane splitPane;
    public Application()
    {
        initUI();
    }

    private void initUI()
    {
        Board board = new Board(i, this);
        JPanel infoPanel = new JPanel();
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, board, infoPanel);
        splitPane.setDividerSize(0);
        add(splitPane);
        // infoPanel.add(new JButton("End turn"));

        setSize(500, 500);

        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    @Override
    public void paint(Graphics g)
    {
        BufferStrategy bs = getBufferStrategy();
        super.paint(getGraphics());
        splitPane.setDividerLocation(getHeight() - 80);
    }
    
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            Application ex = new Application();
            ex.setVisible(true);
        });

        Random rand = new Random();
        NoiseGenerator noise = new NoiseGenerator(0);
        // for(int i = 0; i < 1000; i++)
        // {
        //     System.out.print((float)Math.round(noise.smoothNoise(rand.nextDouble() * 100, rand.nextDouble() * 100, rand.nextDouble() * 100) * 1000) / 1000);
        //     System.out.print(',');
        // }
    }
}