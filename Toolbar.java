import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Toolbar extends JPanel implements ActionListener
{
    JLabel turnLabel, messageLabel;
    JButton endTurnButton, buildButton;
    Application app;
    
    public Toolbar(Application app)
    {
        this.app = app;
        
        buildButton = new JButton("New city");
        buildButton.addActionListener(this);
        buildButton.setActionCommand("newCity");
        add(buildButton);

        turnLabel = new JLabel("Turn: 1");
        add(turnLabel);

        endTurnButton = new JButton("End turn");
        endTurnButton.addActionListener(this);
        endTurnButton.setActionCommand("endTurn");
        add(endTurnButton);
        
        messageLabel = new JLabel("Turn: 1");
        messageLabel.setVisible(false);
        add(messageLabel);
    }
    
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "endTurn":
                app.turn += 1;
                app.board.endTurn();
                turnLabel.setText("Turn: " + app.turn);
                break;
            case "newCity":
                app.board.buildCity();
                ShowMessage("Select city to build from");
                break;
        }
    }

    public void ShowMessage(String text)
    {
        messageLabel.setText(text);
        setDisplayingMessage(true);
    }

    public void HideMessage()
    {
        setDisplayingMessage(false);
    }
    
    private void setDisplayingMessage(boolean value)
    {
        buildButton.setVisible(!value);
        turnLabel.setVisible(!value);
        endTurnButton.setVisible(!value);
        messageLabel.setVisible(value);
    }
}
