public class City
{
    public Vector2 position;
    public int population, materials, houses, farmers, builders, foodSource, materialSource;

    public City(Vector2 pos, Game game)
    {
        position = pos;
        population = 10;
        houses = 10;
        for(int x = -1; x <= 1; x++)
        {
            for(int y = -1; y <= 1; y++)
            {
                TerrainType type = game.GetTerrainAt(pos.x + x, pos.y + y);
                foodSource += type.food;
                materialSource += type.materials;
            }
        }
        foodSource *= 5;
        materialSource *= 5;
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
        fixTasks();
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
        float ratio = total <= population ? 1 : (float)population / total;
        farmers = Math.min((int)(farmers * ratio), foodSource);
        builders = Math.min((int)(builders * ratio), materialSource);
    }
}
