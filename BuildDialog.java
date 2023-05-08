import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class BuildDialog extends JDialog implements ActionListener, MouseListener
{
    private JButton changeSource, buildCity, buildRoad;
    private Application app;
    private Board board;
    private Game game;
    private Camera camera;
    private int buildsFrom = -1;
    private StructureType newStructureType;

    public BuildDialog(Application app, Game game, Camera camera)
    {
        super(app, "Build dialog", Dialog.ModalityType.MODELESS);

        this.app = app;
        board = app.board;
        this.game = game;
        this.camera = camera;
        board.addMouseListener(this);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        changeSource = addButton("Select source city", "source");
        buildCity = addButton("City", "city");
        buildRoad = addButton("Road", "road");

        addWindowListener(new WindowListener()
        {
            public void windowClosing(WindowEvent e) {Clean();}
            public void windowOpened(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowClosed(WindowEvent e) {}
            public void windowDeactivated(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
        });

        ResetSourceCity();
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void Clean()
    {
        board.removeMouseListener(this);
        board.exitBuildMode();
    }

    private JButton addButton(String name, String command)
    {
        JButton button = new JButton(name);
        button.setActionCommand(command);
        button.addActionListener(this);
        add(button);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        switch(e.getActionCommand())
        {
            case "source": ResetSourceCity(); break;
            case "city": newStructureType = StructureType.city; break;
            case "road": newStructureType = StructureType.road; break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        Vector2 tile = camera.screenToWorld(e.getX(), e.getY());
        if(buildsFrom == -1)
        {
            SelectSourceCity(game.GetCity(tile));
        }
        else if(newStructureType != null)
        {
            SelectNewStructure(tile);
        }
    }

    private void ResetSourceCity()
    {
        buildsFrom = -1;
        app.toolbar.ShowMessage("Select build source city");
        buildCity.setEnabled(false);
        buildRoad.setEnabled(false);
    }

    private void SelectSourceCity(int selected)
    {
        try
        {
            if(selected == -1) throw new GameError("No city selected");
            app.toolbar.ShowMessage("Use build dialog to build structures");
            buildsFrom = selected; // Selects city
            City city = game.cities.get(selected);
            buildCity.setEnabled(city.materials >= 40 && city.population > 10);
            buildRoad.setEnabled(city.materials >= 5);
        }
        catch(GameError err)
        {
            JOptionPane.showMessageDialog(app, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void SelectNewStructure(Vector2 tile)
    {
        try
        {
            ValidateNewStructure(tile);
            Structure building = Structure.Create(tile, newStructureType, game);
            City source = game.cities.get(buildsFrom);
            source.materials -= newStructureType.materialCost();
            source.population -= newStructureType.populationCost();
            source.fixJobs();
            if(newStructureType == StructureType.city)
            {
                game.cities.add((City)building);
            }
            SelectSourceCity(buildsFrom);
            board.repaint();
        }
        catch(GameError err)
        {
            JOptionPane.showMessageDialog(app, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ValidateNewStructure(Vector2 tile) throws GameError
    {
        if(game.GetCity(tile) != -1) throw new GameError("Structure already present");
        if(game.GetTerrainAt(tile) == TerrainType.sea) throw new GameError("Can't build structure at water");
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
