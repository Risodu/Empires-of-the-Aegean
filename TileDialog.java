import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.JButton;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

public class TileDialog extends JDialog implements ChangeListener, ActionListener
{
    private City city;
    private Vector2 tile;
    private Game game;
    private Window owner;
    private Slider[] jobSliders = new Slider[Jobs.values().length], tileSliders = new Slider[9];
    private GridBagLayout layout;
    private JLabel mainText;
    private JPanel surroundings;
    private static final int tileSize = 96;

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
                jobSliders[i] = addSlider(0, 100, city.jobWeights[i], i, Jobs.values()[i].getName());
            }
            
            addButton("Build bakery", "bakery").setEnabled(game.techTree.bakeryUnlocked());
            addButton("Build sawmill", "sawmill").setEnabled(game.techTree.sawmillUnlocked());
            addButton("Build quarry", "quarry").setEnabled(game.techTree.quarryUnlocked());

            surroundings = new JPanel(new GridLayout(3, 3, 0, 0))
            {
                protected void paintComponent(Graphics g)
                {
                    super.paintComponent(g);
                    for(int i = 0; i < 3; i++)
                    {
                        for(int j = 0; j < 3; j++)
                        {
                            TerrainType current = game.GetTerrainAt(i + tile.x - 1, j + tile.y - 1);
                            g.drawImage(current.image, tileSize * i, tileSize * j, tileSize, tileSize, null);
                        }
                    }
                }
            };
            surroundings.setPreferredSize(new Dimension(tileSize * 3, tileSize * 3));

            for(int i = 0; i < 9; i++)
            {
                addTileSlider(i);
            }
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = GridBagConstraints.RELATIVE;
            c.gridwidth = GridBagConstraints.REMAINDER;
            layout.setConstraints(surroundings, c);
            add(surroundings);
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
        }

        for(int i = 0; i < tileSliders.length; i++)
        {
            tileSliders[i].setValue(city.tileJobs[i]);
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
        return new Slider(nameLabel, valueLabel, s, false);
    }

    private JButton addButton(String name, String command)
    {
        JButton button = new JButton(name);
        button.addActionListener(this);
        button.setActionCommand(command);
        add(button);
        return button;
    }
    
    private void addTileSlider(int i)
    {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(tileSize, tileSize));

        JSlider s = new JSlider(0, 100, city.tileJobWeights[i]);
        s.setOpaque(false);
        s.setName(Integer.toString(i));
        s.setPreferredSize(new Dimension(tileSize, s.getPreferredSize().height));

        JLabel l = new JLabel(' ' + Integer.toString(city.tileJobs[i]) + ' ');
        l.setBackground(Color.white);
        l.setOpaque(true);
        tileSliders[i] = new Slider(null, l, s, true);

        s.addChangeListener(new ChangeListener()
        {
            @Override
            public void stateChanged(ChangeEvent e)
            {
                JSlider source = (JSlider)e.getSource();
                city.tileJobWeights[Integer.parseInt(source.getName())] = source.getValue();
                city.fixJobs();
                l.setText(' ' + Integer.toString(city.tileJobs[i]) + ' ');
                update();
                owner.repaint();
            }
        });

        p.add(s);
        p.add(l);
        surroundings.add(p);
    }

    private String toHTML(String text)
    {
        return "<html>" + text.replace("\n", "<br>") + "</html>";
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        JSlider source = (JSlider)e.getSource();
        city.jobWeights[Integer.parseInt(source.getName())] = source.getValue();
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
        public Slider(JLabel name, JLabel value, JSlider slider, boolean pad)
        {
            this.name = name;
            this.value = value;
            this.slider = slider;
            this.pad = pad;
        }

        public void setValue(int x)
        {
            String value = String.valueOf(x);
            if(pad) value = ' ' + value + ' ';
            this.value.setText(value);
        }

        JLabel name, value;
        JSlider slider;
        boolean pad;
    }
}
