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
    public int food, materials, culture; 
    public static TerrainType 
    sea = new TerrainType("sea", "sea.png", 2, 0, 0), 
    shore = new TerrainType("shore", "shore.png", 1, 0, 2), 
    plain = new TerrainType("plain", "plain.png", 4, 0, 0), 
    forest = new TerrainType("forest", "forest2.png", 2, 2, 1), 
    hill = new TerrainType("hill", "hill.png", 1, 2, 2), 
    mountain = new TerrainType("mountain", "mountain.png", 0, 4, 0);

    public TerrainType(String name, Color color)
    {
        displayName = name;
        this.color = color;
    }

    public TerrainType(String name, String imagePath, int food, int materials, int culture)
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
        this.food = food;
        this.materials = materials;
        this.culture = culture;
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
