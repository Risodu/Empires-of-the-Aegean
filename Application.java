import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class Application extends JFrame implements ActionListener
{   
    float i = 0;
    JSplitPane splitPane;
    JLabel turnLabel;
    JButton endTurnButton;
    int turn = 1;
    Board board;
    public Application()
    {
        initUI();
    }

    private void initUI()
    {
        board = new Board(i, this);
        JPanel infoPanel = new JPanel();
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, board, infoPanel);
        splitPane.setDividerSize(0);
        add(splitPane);

        turnLabel = new JLabel("Turn: 1");
        infoPanel.add(turnLabel);

        endTurnButton = new JButton("End turn");
        endTurnButton.addActionListener(this);
        endTurnButton.setActionCommand("endTurn");
        infoPanel.add(endTurnButton);

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

    public void actionPerformed(ActionEvent e)
    {
        if("endTurn".equals(e.getActionCommand()))
        {
            turn += 1;
            turnLabel.setText("Turn: " + turn);
            board.endTurn();
        }
    }
}