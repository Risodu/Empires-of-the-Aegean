import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;


public class Board extends JPanel implements MouseMotionListener, MouseWheelListener, MouseListener
{
    private Camera camera;
    private int lastMouseX, lastMouseY;
    private Application app;
    private Game game;

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
        // long time = System.currentTimeMillis();
        Graphics2D g2d = (Graphics2D)g;
        RenderingHints rh = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

        rh.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setRenderingHints(rh);

        Rectangle2D rect = new Rectangle2D.Float();
        g2d.setStroke(new BasicStroke(10));

        Vector2 from = camera.screenToWorld(0, 0);
        Vector2 to = camera.screenToWorld(getWidth(), getHeight());

        for(int i = (int)from.x; i <= to.x; i++)
        {
            for(int j = (int)from.y; j <= to.y; j++)
            {
                Vector2 screenPoint = camera.worldToScreen(i, j);
                rect.setFrame(screenPoint.x, screenPoint.y, camera.scale, camera.scale);
                g2d.setColor(game.GetTerrainAt(i, j).color);
                g2d.fill(rect);
            }
        }

        Ellipse2D circle = new Ellipse2D.Float();
        for(int i = 0; i < game.cities.size(); i++)
        {
            City city = game.cities.get(i);
            Vector2 pos = city.position;
            Vector2 screenPoint = camera.worldToScreen(pos.x + 0.2f, pos.y + 0.2f);
            g2d.setColor(new Color(city.r, city.g, city.b));
            circle.setFrame(screenPoint.x, screenPoint.y, camera.scale * 0.6f, camera.scale * 0.6f);
            g2d.fill(circle);
        }
        // System.out.println(System.currentTimeMillis() - time);
    }

    public void endTurn()
    {
        game.endTurn();
        repaint();
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
        camera.scale *= e.getWheelRotation() == 1 ? 0.8 : 1.25;
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
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