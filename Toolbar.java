import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Toolbar extends JPanel implements ActionListener
{
    JLabel turnLabel;
    JButton endTurnButton;
    Application app;
    
    public Toolbar(Application app)
    {
        this.app = app;
        turnLabel = new JLabel("Turn: 1");
        add(turnLabel);

        endTurnButton = new JButton("End turn");
        endTurnButton.addActionListener(this);
        endTurnButton.setActionCommand("endTurn");
        add(endTurnButton);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        if("endTurn".equals(e.getActionCommand()))
        {
            app.endTurn();
            turnLabel.setText("Turn: " + app.turn);
        }
    }
}
