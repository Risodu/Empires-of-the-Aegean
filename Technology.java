public class Technology
{
    public String displayName, description;
    public int cost, x, y;
    public int[] prerequisiteIndices;
    public Technology[] prerequisites;
    public boolean researched;

    public Technology(String displayName, String description, int cost, int x, int y, int[] prerequisiteIndices, Technology[] prerequisites)
    {
        this.displayName = displayName;
        this.description = description;
        this.cost = cost;
        this.x = x;
        this.y = y;
        this.prerequisiteIndices = prerequisiteIndices;
        this.prerequisites = prerequisites;
        this.researched = false;
    }
}
