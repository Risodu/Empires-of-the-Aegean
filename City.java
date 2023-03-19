public class City
{
    public Vector2 position;
    public int population, materials, houses, farmers, builders;

    public City(Vector2 pos)
    {
        position = pos;
        population = 10;
        houses = 10;
    }

    public void endTurn()
    {
        materials += getMaterialProduction();
        int populationInc = getPopulationIncrease();
        
        if(population + populationInc > houses)
        {
            int builded = Math.min(population + populationInc - houses, materials / 2);
            houses += builded;
            populationInc = Math.min(population + populationInc, houses) - population;
            materials -= builded * 2;
        }

        float multiplier = 1 + ((float)populationInc / population);
        farmers *= multiplier;
        builders *= multiplier;
        population += populationInc;
    }

    public int getPopulationIncrease()
    {
        float food = farmers - population * 0.2f;
        return Math.round(food > 0 ? food * 0.5f : food * 2);
    }

    public int getMaterialProduction()
    {
        return builders;
    }

    public void fixTasks()
    {
        int total = farmers + builders;
        if(total <= population) return;
        float ratio = (float)population / total;
        farmers = (int)(farmers * ratio);
        builders = (int)(builders * ratio);
    }
}
