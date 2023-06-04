public class Technology
{
    public String displayName, description;
    public int cost, x, y, parentIndex;
    public Technology parent;
    public boolean researched;

    public Technology(String displayName, String description, int cost, int x, int y, int parentIndex, Technology parent)
    {
        this.displayName = displayName;
        this.description = description;
        this.cost = cost;
        this.x = x;
        this.y = y;
        this.parentIndex = parentIndex;
        this.parent = parent;
        this.researched = false;
    }
}
