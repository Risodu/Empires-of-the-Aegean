import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JDialog;

public class ResearchDialog extends JDialog implements ActionListener
{
    private Game game;
    private JButton buttons[];
    private TechTree techTree;
    private static final int padding = 10;
    public ResearchDialog(Window owner, Game game)
    {
        super(owner, Dialog.ModalityType.DOCUMENT_MODAL);
        this.game = game;
        techTree = game.techTree;

        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);

        buttons = new JButton[techTree.technologies.length];
        for(int i = 0; i < buttons.length; i++)
        {
            Technology tech = techTree.technologies[i];
            buttons[i] = new JButton(tech.displayName);
            buttons[i].setToolTipText("<html>" + tech.description + "<br>Cost: " + Integer.toString(tech.cost) + "</html>");

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = tech.x;
            c.gridy = tech.y;
            c.insets = new Insets(padding, padding, padding, padding);

            add(buttons[i], c);
        }
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
    }
}