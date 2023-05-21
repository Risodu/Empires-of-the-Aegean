import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JButton;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

public class TileDialog extends JDialog implements ChangeListener, ActionListener
{
    private City city;
    private Vector2 tile;
    private Game game;
    private Window owner;
    private Slider[] jobSliders = new Slider[Jobs.values().length];
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
            for(int i = 0; i < city.jobs.length; i++)
            {
                jobSliders[i] = addSlider(0, Math.min(city.population, city.maxJobs[i]), city.jobs[i], i, Jobs.values()[i].getName());
            }
            
            addButton("Build bakery", "bakery");
            addButton("Build sawmill", "sawmill");
            addButton("Build quarry", "quarry");
        }

        updateText();

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void update()
    {
        updateText();
        updateSliders();
    }

    private void updateText()
    {
        String text = "Selected tile: " + game.GetTerrainAt(tile).displayName;
        
        if(city != null)
        {
            text += "\nThere is a city on this tile";
            text += String.format("\nPopulation: %d (%+d)", city.population, city.getPopulationIncrease());
            text += String.format("\nMaterials: %d (%+d)", city.materials, city.getMaterialProduction());
            text += String.format("\nCulture (shared): %d (%+d)", game.culture, game.getCultureIncrease());
            text += "\nHouses: " + city.houses;
        }
        
        mainText.setText(toHTML(text));
    }

    private void updateSliders()
    {
        for(int i = 0; i < jobSliders.length; i++)
        {
            jobSliders[i].setValue(city.jobs[i]);
            int max = Math.min(city.population, city.maxJobs[i]);
            jobSliders[i].slider.setMaximum(max);
            jobSliders[i].slider.setMinorTickSpacing((int)Math.pow(10, Math.floor(Math.log10(max) - 0.5)));
        }
    }

    private Slider addSlider(int min, int max, int value, int id, String name)
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
        s.setName(Integer.toString(id));
        s.setMinorTickSpacing((int)Math.pow(10, Math.floor(Math.log10(max) - 0.5)));
        s.setPaintTicks(true);
        return new Slider(nameLabel, valueLabel, s);
    }

    private JButton addButton(String name, String command)
    {
        JButton button = new JButton(name);
        button.addActionListener(this);
        button.setActionCommand(command);
        add(button);
        return button;
    }

    private String toHTML(String text)
    {
        return "<html>" + text.replace("\n", "<br>") + "</html>";
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        JSlider source = (JSlider)e.getSource();
        city.jobs[Integer.parseInt(source.getName())] = source.getValue();
        city.fixJobs();
        update();
        owner.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        try
        {
            switch(e.getActionCommand())
            {
                case "bakery": city.build(0); break;
                case "sawmill": city.build(1); break;
                case "quarry": city.build(2); break;
            }
        }
        catch(GameError err)
        {
            JOptionPane.showMessageDialog(this, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        update();
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
