public enum StructureType
{
    city, road;

    public int materialCost()
    {
        switch(this)
        {
            case city: return 40;
            case road: return 5;
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
}
