import java.awt.EventQueue;
import java.awt.Graphics;
import java.util.Random;
import javax.swing.JFrame;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

public class Application extends JFrame
{   
    float i = 0;
    final BufferStrategy bufferStrategy = getBufferStrategy();
    public Application()
    {
        initUI();
    }

    private void initUI()
    {
        Board board = new Board(i, this);
        add(board);

        setSize(500, 500);

        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    public void Draw()
    {
        BufferStrategy bs = getBufferStrategy();
        super.paint(getGraphics());;
        if(bs == null)
            System.out.println("xd");
        bs.show(); // swap buffers
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