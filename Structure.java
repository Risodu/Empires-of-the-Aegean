public class Structure
{
    public Vector2 position;
    public StructureType type;

    public Structure(Vector2 position, StructureType type)
    {
        this.position = position;
        this.type = type;
    }

    public static Structure Create(Vector2 position, StructureType type, Game game)
    {
        switch(type)
        {
            case city: return new City(position, game);
            default: return new Structure(position, type);
        }
    }
}
