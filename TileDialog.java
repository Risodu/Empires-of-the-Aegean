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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class TileDialog extends JDialog implements ChangeListener
{
    private City city;
    private Vector2 tile;
    private Game game;
    private Window owner;
    private Slider farmersSlider, buildersSlider;
    private GridBagLayout layout;
    private JLabel mainText;

    public TileDialog(Window owner, Vector2 tile, Game game)
    {
        super(owner, Dialog.ModalityType.DOCUMENT_MODAL);
        this.owner = owner;
        this.tile = tile;
        this.game = game;

        layout = new GridBagLayout();
        setLayout(layout);

        mainText = new JLabel();
        mainText.setAlignmentX(Component.RIGHT_ALIGNMENT);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 20, 5, 20);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;
        layout.setConstraints(mainText, c);
        add(mainText);

        int cityIndex = game.GetCity(tile);
        city = cityIndex != -1 ? game.cities.get(cityIndex) : null;

        if(city != null)
        {
            farmersSlider = addSlider(0, Math.min(city.population, city.foodSource), city.farmers, "Farmers");
            buildersSlider = addSlider(0, Math.min(city.population, city.materialSource), city.builders, "Builders");
        }

        updateText();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void updateText()
    {
        String text = "Selected tile: " + game.GetTerrainAt(tile).displayName;
        
        if(city != null)
        {
            text += "\nThere is a city on this tile";
            text += String.format("\nPopulation: %d (%+d)", city.population, city.getPopulationIncrease());
            text += String.format("\nMaterials: %d (%+d)", city.materials, city.getMaterialProduction());
            text += "\nHouses: " + city.houses;
        }
        
        mainText.setText(toHTML(text));

    }

    private Slider addSlider(int min, int max, int value, String name)
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(5, 10, 5, 0);
        
        JLabel nameLabel = new JLabel(name);
        layout.setConstraints(nameLabel, c);
        add(nameLabel);
        
        JSlider s = new JSlider(min, max, value);
        c.weightx = 100;
        layout.setConstraints(s, c);
        add(s);
        
        JLabel valueLabel = new JLabel(String.valueOf(value));
        c.weightx = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 0, 5, 10);
        layout.setConstraints(valueLabel, c);
        add(valueLabel);

        s.addChangeListener(this);
        s.setName(name);
        s.setMinorTickSpacing((int)Math.pow(10, Math.floor(Math.log10(max) - 0.5)));
        s.setPaintTicks(true);
        // s.setSnapToTicks(true);
        return new Slider(nameLabel, valueLabel, s);
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
            case "Farmers": city.farmers = source.getValue(); break;
            case "Builders": city.builders = source.getValue(); break;
        }
        city.fixTasks();
        farmersSlider.setValue(city.farmers);
        buildersSlider.setValue(city.builders);
        updateText();
        owner.repaint();
    }

    private class Slider
    {
        public Slider(JLabel name, JLabel value, JSlider slider)
        {
            this.name = name;
            this.value = value;
            this.slider = slider;
        }

        public void setValue(int x)
        {
            slider.setValue(x);
            this.value.setText(String.valueOf(x));
        }

        JLabel name, value;
        JSlider slider;
    }
}
