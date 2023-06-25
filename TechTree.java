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
            int parentIndex = current.getInt("parent");
            Technology parent = parentIndex == -1 ? null : technologies[parentIndex];
            technologies[i] = new Technology(
                current.getString("displayName"),
                current.getString("description"),
                current.getInt("cost"),
                current.getInt("x"),
                current.getInt("y"),
                parentIndex,
                parent
            );
        }
    }

    public void research(int index)
    {
        technologies[index].researched = true;
        switch(index)
        {
            case 0:
                TerrainType.plain.capacity += 5;
        }
    }

    public int getHouseCost()
    {
        return technologies[1].researched ? 2 : 3;
    }

    public boolean bakeryUnlocked(){return technologies[2].researched;}
    public boolean sawmillUnlocked(){return technologies[3].researched;}
    public boolean quarryUnlocked(){return technologies[4].researched;}
}
