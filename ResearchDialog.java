import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class ResearchDialog extends JDialog implements ActionListener
{
    private Game game;
    private JPanel panel;
    private JButton buttons[];
    private TechTree techTree;
    private static final int padding = 10;
    public ResearchDialog(Window owner, Game game)
    {
        super(owner, Dialog.ModalityType.DOCUMENT_MODAL);
        this.game = game;
        techTree = game.techTree;
        
        panel = new JPanel(new GridBagLayout())
        {
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                for(int i = 0; i < techTree.technologies.length; i++)
                {
                    Technology tech = techTree.technologies[i];
                    Component a = this.getComponent(i);
                    for(int j = 0; j < tech.prerequisites.length; j++)
                    {
                        Component b = this.getComponent(tech.prerequisiteIndices[j]);
                        g.drawLine(a.getX() + a.getWidth() / 2, a.getY() + a.getHeight() / 2, b.getX() + b.getWidth() / 2, b.getY() + b.getHeight() / 2);
                    }
                }
            }
        };
        add(panel);

        buttons = new JButton[techTree.technologies.length];
        for(int i = 0; i < buttons.length; i++)
        {
            Technology tech = techTree.technologies[i];
            buttons[i] = new JButton(tech.displayName);
            buttons[i].setToolTipText("<html>" + tech.description + "<br>Cost: " + Integer.toString(tech.cost) + "</html>");
            buttons[i].setActionCommand(Integer.toString(i));
            buttons[i].addActionListener(this);

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = tech.x;
            c.gridy = tech.y;
            c.insets = new Insets(padding, padding, padding, padding);

            panel.add(buttons[i], c);
        }
        
        pack();
        update();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void update()
    {
        for(int i = 0; i < buttons.length; i++)
        {
            Technology tech = techTree.technologies[i];
            if(tech.researched)
                buttons[i].setBackground(new Color(0, 255, 0, 255));
            else
                buttons[i].setEnabled(techTree.hasPrerequisites(i) && game.culture >= tech.cost);
        }
    }
    
    @Override
    public void actionPerformed(ActionEvent e)
    {
        int index = Integer.parseInt(e.getActionCommand());
        Technology tech = techTree.technologies[index];
        if(game.culture < tech.cost) return;
        techTree.research(index);
        game.culture -= tech.cost;
        update();
    }
}