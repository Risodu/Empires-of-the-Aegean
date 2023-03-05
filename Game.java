import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Game
{
    private float seed;
    private NoiseGenerator noise;
    public List<Vector2> cities = new ArrayList<Vector2>();

    public Game(float seed)
    {
        this.seed = seed;
        noise = new NoiseGenerator(seed);
        Random random = new Random();
        for(int i = 0; i < 10; i++)
        {
            cities.add(new Vector2(random.nextInt(10), random.nextInt(10)));
        }
    }

    public TerrainType GetTerrainAt(float x, float y)
    {
        double height = (noise.smoothNoise(x / 5, y / 5, 0.5));
        double moist = (noise.smoothNoise(x / 5, y / 5, 1.5));
        return TerrainType.GetTerrainFrom(normalize(height), normalize(moist));
    }

    public TerrainType GetTerrainAt(Vector2 tile)
    {
        return GetTerrainAt(tile.x, tile.y);
    }

    static double normalize(double i)
    {
        return 1 / (1 + Math.exp(-5 * i));
        // return Math.copySign(Math.pow(Math.abs(i), 0.7), i);
    }

    int GetCity(Vector2 pos)
    {
        for(int i = 0; i < cities.size(); i++)
        {
            if(cities.get(i).equals(pos))
                return i;
        }
        return -1;
    }
}
