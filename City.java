public class City extends Structure
{
    public int population, materials, houses, woodSource, stoneSource;
    public int[] jobs = new int[Jobs.values().length], maxJobs = new int[Jobs.values().length];

    public City(Vector2 pos, Game game)
    {
        super(pos, StructureType.city);
        population = 10;
        houses = 10;
        for(int x = -1; x <= 1; x++)
        {
            for(int y = -1; y <= 1; y++)
            {
                TerrainType type = game.GetTerrainAt(pos.x + x, pos.y + y);
                maxJobs[Jobs.farmer.ordinal()] += type.food * 5;
                maxJobs[Jobs.builder.ordinal()] += type.materials * 5;
                if(type == TerrainType.forest || type == TerrainType.hill) woodSource += type.materials * 5;
                if(type == TerrainType.mountain) stoneSource += type.materials * 5;
            }
        }
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
        jobs[Jobs.farmer.ordinal()] *= multiplier;
        jobs[Jobs.builder.ordinal()] *= multiplier;
        population += populationInc;
        fixJobs();
    }

    public int getPopulationIncrease()
    {
        int boosted = Math.min(jobs[Jobs.farmer.ordinal()], jobs[Jobs.baker.ordinal()] * 5);
        float food = jobs[Jobs.farmer.ordinal()] + boosted * 0.5f - population * 0.2f;
        return Math.round(food > 0 ? food * 0.5f : food * 2);
    }

    public int getMaterialProduction()
    {
        int boosted = Math.min(jobs[Jobs.sawmill_worker.ordinal()] * 5, woodSource) + Math.min(jobs[Jobs.quarry_worker.ordinal()] * 5, stoneSource);
        boosted = Math.min(boosted, jobs[Jobs.builder.ordinal()]);
        return jobs[Jobs.builder.ordinal()] + Math.round(boosted * 0.5f);
    }

    public void fixJobs()
    {
        int total = 0;
        for(int i = 0; i < jobs.length; i++)
            total += jobs[i];
        
        float ratio = total <= population ? 1 : (float)population / total;

        for(int i = 0; i < jobs.length; i++)
            jobs[i] = Math.min((int)(jobs[i] * ratio), maxJobs[i]);
    }

    public void build(int type) throws GameError
    {
        if(materials < 10)
        {
            throw new GameError("You don't have enough materials (required: 10, current: " + materials + ")");
        }
        maxJobs[Jobs.baker.ordinal() + type] += 5;
        materials -= 10;
    }
}
