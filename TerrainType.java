import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class TerrainType
{
    public String displayName;
    public Color color;
    public BufferedImage image;
    public static TerrainType 
    sea = new TerrainType("sea", "sea.png"), 
    shore = new TerrainType("shore", "shore.png"), 
    plain = new TerrainType("plain", "plain.png"), 
    forest = new TerrainType("forest", "forest2.png"), 
    hill = new TerrainType("hill", "hill.png"), 
    mountain = new TerrainType("mountain", "mountain.png");

    public TerrainType(String name, Color color)
    {
        displayName = name;
        this.color = color;
    }

    public TerrainType(String name, String imagePath)
    {
        displayName = name;
        try
        {
            this.image = ImageIO.read(new File(imagePath));
        }
        catch(IOException err)
        {
            System.out.println("Could not load " + imagePath + " for terrain " + name);
            System.exit(1);
        }
    }

    // Arguments between 0 and 1
    public static TerrainType GetTerrainFrom(double height, double moist)
    {
        if(height < 0.4)
            return sea;
        if(height < 0.5)
            return shore;
        if(height < 0.9)
        {
            switch((int)Math.floor(moist * 5))
            {
                case 0: return hill;
                case 1: case 2: return plain;
                case 3: case 4: return forest;
            }
        }
        if(height < 0.95)
            return hill;
        if(height < 1)
            return mountain;
        
        return sea;
    }
}
