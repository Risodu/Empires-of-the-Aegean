import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class BuildDialog extends JDialog implements ActionListener, MouseListener
{
    private JButton changeSource, buildCity, buildRoad, buildPort;
    private Application app;
    private Board board;
    private Game game;
    private Camera camera;
    private int buildsFrom = -1;
    private StructureType newStructureType;
    private List<Vector2> portPlaces = new ArrayList<Vector2>();

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
        buildPort = addButton("Port", "port");

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
        UpdatePortPlaces();
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
            case "port": newStructureType = StructureType.port; break;
        }
        board.repaint();
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
        buildPort.setEnabled(false);
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
            if(!(city.materials >= 40 && city.population > 10) && newStructureType == StructureType.city)
                newStructureType = null;

            buildRoad.setEnabled(city.materials >= 5);
            if(!(city.materials >= 5) && newStructureType == StructureType.road)
                newStructureType = null;

            buildPort.setEnabled(city.materials >= 20 && game.techTree.portUnlocked());
            if(!(city.materials >= 20) && newStructureType == StructureType.port)
                newStructureType = null;
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
            else if(newStructureType == StructureType.port)
            {
                game.ports.add(building);
            }
            game.structures.add(building);
            SelectSourceCity(buildsFrom);
            UpdatePortPlaces();
            board.repaint();
        }
        catch(GameError err)
        {
            JOptionPane.showMessageDialog(app, err.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void ValidateNewStructure(Vector2 tile) throws GameError
    {
        if(game.GetCity(tile) != -1) throw new GameError("Structure already present");
        if(game.GetRoad(tile) != -1) throw new GameError("Structure already present");
        if(game.GetPort(tile) != -1) throw new GameError("Structure already present");
        if(newStructureType != StructureType.port)
        {
            if(game.GetTerrainAt(tile) == TerrainType.sea) throw new GameError("Can't build structure at water");
        }
        else
        {
            if(game.GetTerrainAt(tile) != TerrainType.sea) throw new GameError("Port must be built at water");
            if(game.PortNearby(tile)) throw new GameError("Port can't be built right next to another port");
            if(portPlaces.contains(tile)) return;
        }
        if(!game.RoadNearby(tile)) throw new GameError("There is no road nearby");
    }

    public void UpdatePortPlaces()
    {
        class SearchElement
        {
            Vector2 pos;
            int dist;

            public SearchElement(Vector2 pos, int dist)
            {
                this.pos = pos;
                this.dist = dist;
            }
        }

        portPlaces.clear();
        Queue<SearchElement> queue = new ArrayDeque<SearchElement>();
        Set<Vector2> reached = new HashSet<Vector2>();
        
        for(int i = 0; i < game.ports.size(); i++)
        {
            queue.add(new SearchElement(game.ports.get(i).position, 0));
        }

        reached.add(new Vector2(-1, 0));
        System.out.println(reached.contains(new Vector2(-1, 0)));

        while(!queue.isEmpty())
        {
            SearchElement current = queue.remove();
            System.out.println(current.pos.x);
            System.out.println(current.pos.y);
            System.out.println();
            boolean shore = false;
            for(int i = 0; i < 4; i++)
            {
                SearchElement next = new SearchElement(current.pos.add(Vector2.near[i]), current.dist + 1);
                if(game.GetTerrainAt(next.pos) != TerrainType.sea)
                {
                    shore = true;
                    continue;
                }
                if(next.dist > 10) continue;
                if(reached.contains(next.pos)) continue;
                reached.add(next.pos);
                queue.add(next);
            }
            if(shore) portPlaces.add(current.pos);
        }
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
