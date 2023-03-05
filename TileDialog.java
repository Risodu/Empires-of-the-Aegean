import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.BoxLayout;
import java.awt.Window;
import java.awt.Dialog;

public class TileDialog extends JDialog implements ChangeListener
{
    private City city;
    private Window owner;

    public TileDialog(Window owner, String text, Game game, int cityIndex)
    {
        super(owner, Dialog.ModalityType.DOCUMENT_MODAL);
        
        this.owner = owner;
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        add(new JLabel(toHTML(text)));

        if(cityIndex != -1)
        {
            city = game.cities.get(cityIndex);
            JSlider r = new JSlider(0, 255, city.r);
            JSlider g = new JSlider(0, 255, city.g);
            JSlider b = new JSlider(0, 255, city.b);
            add(r);
            add(g);
            add(b);
            r.addChangeListener(this);
            g.addChangeListener(this);
            b.addChangeListener(this);
            r.setName("r");
            g.setName("g");
            b.setName("b");
        }
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String toHTML(String text)
    {
        return "<html>" + text.replace("\n", "<br>") + "</html>";
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        JSlider source = (JSlider)e.getSource();
        switch(source.getName())
        {
            case "r": city.r = source.getValue(); break;
            case "g": city.g = source.getValue(); break;
            case "b": city.b = source.getValue(); break;
        }
        
        owner.repaint();
    }
}
