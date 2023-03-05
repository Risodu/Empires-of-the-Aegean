import java.awt.Color;

public class TerrainType
{
    public String displayName;
    public Color color;
    public static TerrainType 
    sea = new TerrainType("sea", new Color(33, 63, 255)), 
    shore = new TerrainType("shore", new Color(245, 245, 56)), 
    plain = new TerrainType("plain", new Color(87, 222, 47)), 
    forest = new TerrainType("forest", new Color(35, 112, 12)), 
    hill = new TerrainType("hill", new Color(143, 91, 9)), 
    mountain = new TerrainType("mountain", new Color(115, 115, 115));

    public TerrainType(String name, Color color)
    {
        displayName = name;
        this.color = color;
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
