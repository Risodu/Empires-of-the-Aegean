public class City extends Structure
{
    public int population, materials, houses, woodSource, stoneSource;
    public int[] jobs = new int[Jobs.values().length], maxJobs = new int[Jobs.values().length], tileJobs = new int[9];
    public TerrainType[] surroundings = new TerrainType[9];
    public Game game;

    public City(Vector2 pos, Game game)
    {
        super(pos, StructureType.city);
        population = 10;
        houses = 10;
        for(int x = 0; x < 3; x++)
        {
            for(int y = 0; y < 3; y++)
            {
                TerrainType current = game.GetTerrainAt(x + pos.x - 1, y + pos.y - 1);
                surroundings[y * 3 + x] = current;
            }
        }

        game.roads.add(new Structure(pos, StructureType.road));
        this.game = game;
    }

    public void endTurn()
    {
        materials += getMaterialProduction();
        int populationInc = getPopulationIncrease();
        
        if(population + populationInc > houses)
        {
            int cost = game.techTree.getHouseCost();
            int builded = Math.min(population + populationInc - houses, materials / cost);
            houses += builded;
            populationInc = Math.min(population + populationInc, houses) - population;
            materials -= builded * cost;
        }

        float multiplier = 1 + ((float)populationInc / population);
        for(int i = 0; i < jobs.length; i++)
        {
            jobs[i] *= multiplier;            
        }
        population += populationInc;
        fixJobs();
    }

    public int getPopulationIncrease()
    {
        int rawFood = 0;
        for(int i = 0; i < 9; i++)
        {
            rawFood += surroundings[i].food * tileJobs[i];
        }
        rawFood >>= 1;
        int boosted = Math.min(rawFood, jobs[Jobs.baker.ordinal()] * 5);
        float food = rawFood + boosted * 0.5f - population * 0.2f;
        return Math.round(food > 0 ? food * 0.5f : food * 2);
    }

    public int getMaterialProduction()
    {
        int wood = 0, stone = 0, materials = 0;
        for(int i = 0; i < 9; i++)
        {
            int current = surroundings[i].materials * tileJobs[i];
            switch(surroundings[i].displayName)
            {
                case "forest": 
                    wood += current; 
                    break;
                case "hill":
                case "mountain":
                    stone += current;
                    break;
            }
            materials += current;
        }
        int boosted = Math.min(jobs[Jobs.sawmill_worker.ordinal()] * 5, wood) + Math.min(jobs[Jobs.quarry_worker.ordinal()] * 5, stone);
        return (materials + Math.round(boosted * 0.5f)) >> 1;
    }

    public int getCultureIncrease()
    {
        int culture = 0;
        for(int i = 0; i < 9; i++)
        {
            culture += surroundings[i].culture * tileJobs[i];
        }
        return culture >> 1;
    }

    public void fixJobs()
    {
        int total = 0;
        for(int i = 0; i < jobs.length; i++)
            total += jobs[i];

        for(int i = 0; i < tileJobs.length; i++)
            total += tileJobs[i];
        
        float ratio = total <= population ? 1 : (float)population / total;

        for(int i = 0; i < jobs.length; i++)
            jobs[i] = Math.min((int)(jobs[i] * ratio), maxJobs[i]);

        for(int i = 0; i < tileJobs.length; i++)
            tileJobs[i] = (int)(tileJobs[i] * ratio);
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
