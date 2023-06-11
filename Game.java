import java.util.Random;
import java.util.List;
import java.util.ArrayList;

public class Game
{
    private NoiseGenerator noise;
    public List<City> cities = new ArrayList<City>();
    public List<Structure> roads = new ArrayList<Structure>();
    public int culture;
    public TechTree techTree;

    public Game(float seed)
    {
        techTree = new TechTree("technologies.json", this);
        noise = new NoiseGenerator(seed);
        Random random = new Random();
        for(int i = 0; i < 100; i++)
        {
            Vector2 position = new Vector2(random.nextInt(10) + i, random.nextInt(10) + i);
            if(GetTerrainAt(position) == TerrainType.sea) continue;
            cities.add(new City(position, this));
            break;
        }
    }

    public void endTurn()
    {
        culture += getCultureIncrease();
        for(int i = 0; i < cities.size(); i++)
        {
            City city = cities.get(i);
            city.endTurn();
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

    public static double normalize(double i)
    {
        return 1 / (1 + Math.exp(-5 * i));
        // return Math.copySign(Math.pow(Math.abs(i), 0.7), i);
    }

    public int GetCity(Vector2 pos)
    {
        for(int i = 0; i < cities.size(); i++)
        {
            if(cities.get(i).position.equals(pos))
                return i;
        }
        return -1;
    }

    public int GetRoad(Vector2 pos)
    {
        for(int i = 0; i < roads.size(); i++)
        {
            if(roads.get(i).position.equals(pos))
                return i;
        }
        return -1;
    }

    public boolean RoadPresent(Vector2 pos)
    {
        for(int i = 0; i < roads.size(); i++)
        {
            if(roads.get(i).position.equals(pos))
                return true;
        }
        return false;
    }
    
    public boolean RoadNearby(Vector2 tile)
    {
        for(int i = -1; i < 2; i++)
        {
            for(int j = -1; j < 2; j++)
            {
                if(i == 0 && j == 0) continue;
                if(RoadPresent(tile.add(new Vector2(i, j)))) return true;
            }
        }
        return false;
    }

    public int getCultureIncrease()
    {
        int total = 0;
        for(int i = 0; i < cities.size(); i++)
        {
            total += cities.get(i).getCultureIncrease();
        }
        return total;
    }
}
