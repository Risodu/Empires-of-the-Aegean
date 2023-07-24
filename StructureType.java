public enum StructureType
{
    city, road, port;

    public int materialCost()
    {
        switch(this)
        {
            case city: return 40;
            case road: return 5;
            case port: return 20;
            default: return 0;
        }
    }

    public int populationCost()
    {
        switch(this)
        {
            case city: return 10;
            default: return 0;
        }
    }

    public boolean canBuild(Game game)
    {
        switch(this)
        {
            case port: return game.techTree.portUnlocked();
            default: return true;
        }
    }
}
