import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.BoxLayout;
import java.awt.Window;
import java.awt.Component;
import java.awt.Dialog;

public class TileDialog extends JDialog implements ChangeListener
{
    private City city;
    private Window owner;
    private JSlider farmersSlider, buildersSlider;

    public TileDialog(Window owner, Vector2 tile, Game game)
    {
        super(owner, Dialog.ModalityType.DOCUMENT_MODAL);
        this.owner = owner;
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        String text = "Selected tile: " + game.GetTerrainAt(tile).displayName;
        int cityIndex = game.GetCity(tile);

        JLabel mainText = new JLabel();
        mainText.setAlignmentX(Component.RIGHT_ALIGNMENT);
        add(mainText);

        if(cityIndex != -1)
        {
            city = game.cities.get(cityIndex);
            text += "\nThere is a city on this tile";
            text += "\nPopulation: " + city.population;
            text += "\nProduction: " + city.production;
            farmersSlider = addSlider(0, city.population, city.farmers, "Food");
            buildersSlider = addSlider(0, city.population, city.builders, "Production");
        }
        
        mainText.setText(toHTML(text));

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JSlider addSlider(int min, int max, int value, String name)
    {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(new JLabel(name));
        JSlider s = new JSlider(min, max, value);
        panel.add(s);
        s.addChangeListener(this);
        s.setName(name);
        s.setMinorTickSpacing(1);
        s.setPaintTicks(true);
        s.setSnapToTicks(true);
        add(panel);
        return s;
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
            case "Food": city.farmers = source.getValue(); break;
            case "Production": city.builders = source.getValue(); break;
        }
        city.fixTasks();
        farmersSlider.setValue(city.farmers);
        buildersSlider.setValue(city.builders);
        owner.repaint();
    }
}
