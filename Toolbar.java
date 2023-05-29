import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Toolbar extends JPanel implements ActionListener
{
    JLabel turnLabel, messageLabel;
    JButton endTurnButton, buildButton, researchButton;
    Application app;
    
    public Toolbar(Application app)
    {
        this.app = app;
        
        buildButton = createButton("Build", "build");
        researchButton = createButton("Research", "research");

        turnLabel = new JLabel("Turn: 1");
        add(turnLabel);

        endTurnButton = createButton("End turn", "endTurn");
        
        messageLabel = new JLabel("Turn: 1");
        messageLabel.setVisible(false);
        add(messageLabel);
    }

    private JButton createButton(String label, String command)
    {
        JButton button = new JButton(label);
        button.addActionListener(this);
        button.setActionCommand(command);
        add(button);
        return button;
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
            case "build":
                app.board.enterBuildMode();
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
        researchButton.setVisible(!value);
        turnLabel.setVisible(!value);
        endTurnButton.setVisible(!value);
        messageLabel.setVisible(value);
    }
}
