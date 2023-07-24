import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;

public class TechTree
{
    public Technology[] technologies;
    private Game game;

    public TechTree(String path, Game game)
    {
        this.game = game;
        String data=null;
        try
        {
            File file = new File(path);
            Scanner sc = new Scanner(file);
            sc.useDelimiter("\\Z");
            data = sc.next();
            sc.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("File can't be loaded:");
            System.out.println(e.getMessage());
            System.exit(0);
        }

        JSONArray array = new JSONArray(data);
        technologies = new Technology[array.length()];
        for(int i = 0; i < array.length(); i++)
        {
            JSONObject current = array.getJSONObject(i);

            JSONArray prerequsiteArray = current.getJSONArray("prerequisites");
            int[] prerequisiteIndices = new int[prerequsiteArray.length()];
            Technology[] prerequisites = new Technology[prerequsiteArray.length()];
            for(int j = 0; j < prerequsiteArray.length(); j++)
            {
                int index = prerequsiteArray.getInt(j);
                prerequisiteIndices[j] = index;
                prerequisites[j] = technologies[index];
            }

            technologies[i] = new Technology(
                current.getString("displayName"),
                current.getString("description"),
                current.getInt("cost"),
                current.getInt("x"),
                current.getInt("y"),
                prerequisiteIndices,
                prerequisites
            );
        }
    }

    public boolean hasPrerequisites(int index)
    {
        Technology[] req = technologies[index].prerequisites;
        for(int i = 0; i < req.length; i++)
        {
            if(!req[i].researched) return false;
        }
        return true;
    }

    public void research(int index)
    {
        technologies[index].researched = true;
        switch(index)
        {
            case 0:
                TerrainType.plain.capacity += 5;
                break;
            case 3:
                TerrainType.sea.food += 2;
                break;
            case 15:
                TerrainType.plain.food += 2;
                break;
        }
    }

    public int getHouseCost()
    {
        return technologies[1].researched ? 2 : 3;
    }

    public float foodBonus()
    {
        float temp = 1;
        if(technologies[11].researched) temp *= 1.2; // Bronze working
        if(technologies[13].researched) temp *= 1.25; // Engineering
        if(technologies[14].researched) temp *= 1.3; // Advanced cooking
        return temp;
    }
    
    public float materialBonus()
    {
        float temp = 1;
        if(technologies[9].researched) temp *= 1.2; // Mathematics
        if(technologies[11].researched) temp *= 1.3; // Bronze working
        if(technologies[12].researched) temp *= 1.3; // Advanced Construction
        if(technologies[13].researched) temp *= 1.25; // Engineering
        return temp;
    }

    public float cultureBonus()
    {
        float temp = 1;
        if(technologies[2].researched) temp *= 1.25; // Writing
        if(technologies[9].researched) temp *= 1.2; // Mathematics
        if(technologies[13].researched) temp *= 1.25; // Engineering
        return temp;
    }

    public boolean barnUnlocked(){return technologies[4].researched;}
    public boolean bakeryUnlocked(){return technologies[5].researched;}
    public boolean sawmillUnlocked(){return technologies[6].researched;}
    public boolean quarryUnlocked(){return technologies[7].researched;}
    public boolean libraryUnlocked(){return technologies[8].researched;}
}
