import java.awt.EventQueue;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class Application extends JFrame
{   
    float i = 0;
    JSplitPane splitPane;
    Toolbar toolbar;
    int turn = 1;
    Board board;
    public Application()
    {
        initUI();
    }

    private void initUI()
    {
        board = new Board(i, this);
        toolbar = new Toolbar(this);
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, board, toolbar);
        splitPane.setDividerSize(0);
        add(splitPane);

        setSize(500, 500);

        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(getGraphics());
        splitPane.setDividerLocation(getHeight() - 80);
    }
    
    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            Application ex = new Application();
            ex.setVisible(true);
        });
    }
}