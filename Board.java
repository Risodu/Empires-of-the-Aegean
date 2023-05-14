import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Window;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class Board extends JPanel implements MouseMotionListener, MouseWheelListener, MouseListener
{
    private Camera camera;
    private int lastMouseX, lastMouseY;
    private Application app;
    private Game game;
    private boolean buildMode = false;

    public Board(float seed, Application app)
    {
        camera = new Camera();
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        addMouseListener(this);

        setPreferredSize(new Dimension(500, 500));
        this.app = app;
        game = new Game(seed);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        drawMap(g);
    }

    void drawMap(Graphics g)
    {
        Graphics2D g2d = (Graphics2D)g;

        Rectangle2D rect = new Rectangle2D.Float();
        g2d.setStroke(new BasicStroke(10));

        Vector2 from = camera.screenToWorld(0, 0);
        Vector2 to = camera.screenToWorld(getWidth(), getHeight());

        for(int i = (int)from.x; i <= to.x; i++)
        {
            for(int j = (int)from.y; j <= to.y; j++)
            {
                Vector2 screenPoint = camera.worldToScreen(i, j);
                TerrainType terrain = game.GetTerrainAt(i, j);
                if(terrain.image == null)
                {
                    rect.setFrame(screenPoint.x, screenPoint.y, camera.scale, camera.scale);
                    g2d.setColor(terrain.color);
                    g2d.fill(rect);
                }
                else
                {
                    g2d.drawImage(terrain.image, (int)screenPoint.x, (int)screenPoint.y, (int)camera.scale, (int)camera.scale, null);
                }
            }
        }

        g2d.setColor(new Color(214, 156, 47));
        for(int i = 0; i < game.roads.size(); i++)
        {
            Vector2 pos = game.roads.get(i).position;
            drawRoad(pos, g2d);
        }

        Ellipse2D circle = new Ellipse2D.Float();
        g2d.setColor(new Color(255, 170, 0));
        for(int i = 0; i < game.cities.size(); i++)
        {
            City city = game.cities.get(i);
            Vector2 pos = city.position;
            Vector2 screenPoint = camera.worldToScreen(pos.x + 0.2f, pos.y + 0.2f);
            circle.setFrame(screenPoint.x, screenPoint.y, camera.scale * 0.6f, camera.scale * 0.6f);
            g2d.fill(circle);
        }
    }

    private void drawRoad(Vector2 pos, Graphics2D g2d)
    {
        float rw = 0.3f; // Road width
        float ro = (1 - rw) * 0.5f; // Road offset

        drawRoadSegment(g2d, pos, ro, ro, rw, rw);
        if(game.RoadPresent(pos.add(Vector2.down)))
            drawRoadSegment(g2d, pos, ro, ro, rw, rw + ro);
        if(game.RoadPresent(pos.add(Vector2.up)))
            drawRoadSegment(g2d, pos, ro, 0, rw, rw + ro);
        if(game.RoadPresent(pos.add(Vector2.right)))
            drawRoadSegment(g2d, pos, ro, ro, rw + ro, rw);
        if(game.RoadPresent(pos.add(Vector2.left)))
            drawRoadSegment(g2d, pos, 0, ro, rw + ro, rw);
    }

    private void drawRoadSegment(Graphics2D g2d, Vector2 road, float xoffset, float yoffset, float width, float height)
    {
        Rectangle2D rect = new Rectangle2D.Float();
        Vector2 screenPoint = camera.worldToScreen(road.x + xoffset, road.y + yoffset);
        rect.setFrame(screenPoint.x, screenPoint.y, camera.scale * width, camera.scale * height);
        g2d.fill(rect);
    }

    public void endTurn()
    {
        game.endTurn();
        repaint();
    }

    public void enterBuildMode()
    {
        buildMode = true;
        new BuildDialog(app, game, camera);
    }

    public void exitBuildMode()
    {
        buildMode = false;
        app.toolbar.HideMessage();
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        lastMouseX = e.getX();
        lastMouseY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        camera.pos.x -= (e.getX() - lastMouseX) / camera.scale;
        camera.pos.y -= (e.getY() - lastMouseY) / camera.scale;
        lastMouseX = e.getX();
        lastMouseY = e.getY();
        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e)
    {
        if(e.getWheelRotation() == 1 ? camera.scale < 10 : camera.scale > 50) return;
        camera.changeScale(e.getWheelRotation() == 1 ? 0.8f : 1.25f, e.getX(), e.getY());
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if(buildMode) return; // Handled by BuildDialog
        Vector2 tile = camera.screenToWorld(e.getX(), e.getY());
        new TileDialog((Window)app, tile, game);
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