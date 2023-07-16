public class City extends Structure
{
    public int population, materials, houses, woodSource, stoneSource;
    public int[] jobs = new int[Jobs.values().length], 
        maxJobs = new int[Jobs.values().length], 
        jobWeights = new int[Jobs.values().length],
        tileJobs = new int[9],
        tileJobWeights = new int[9];
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
        int boosted = Math.min(rawFood, jobs[Jobs.baker.ordinal()] * 10);
        System.out.println(rawFood);
        System.out.println(game.techTree.foodBonus());
        float food = (rawFood + boosted * 0.5f) * game.techTree.foodBonus() * 0.5f - population * 0.2f;
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
        int boosted = Math.min(jobs[Jobs.sawmill_worker.ordinal()] * 10, wood) + Math.min(jobs[Jobs.quarry_worker.ordinal()] * 10, stone);
        return Math.round((materials + boosted * 0.5f) * game.techTree.materialBonus() * 0.5f);
    }

    public int getCultureIncrease()
    {
        int culture = 0;
        for(int i = 0; i < 9; i++)
        {
            culture += surroundings[i].culture * tileJobs[i];
        }
        return Math.round(culture * game.techTree.cultureBonus() * 0.5f);
    }

    public void fixJobs()
    {
        int total = 0;
        for(int i = 0; i < jobs.length; i++)
            total += jobWeights[i];

        for(int i = 0; i < tileJobs.length; i++)
            total += tileJobWeights[i];
        
        float ratio = total == 0 ? 0 : (float)population / total;

        int allocated = 0;
        for(int i = 0; i < jobs.length; i++)
        {
            int current = Math.min((int)(jobWeights[i] * ratio), maxJobs[i]);
            jobs[i] = current;
            allocated += current;
        }

        for(int i = 0; i < tileJobs.length; i++)
        {
            int current = Math.min((int)(tileJobWeights[i] * ratio), surroundings[i].capacity);
            tileJobs[i] = current;
            allocated += current;
        }

        if(total == 0) return;
        for(int i = 0; i < population - allocated; i++)
        {
            int lowestIndex = -1;
            float lowestRatio = 100000;

            for(int j = 0; j < jobs.length; j++)
            {
                if(jobs[j] == maxJobs[j]) continue;
                if((float)jobWeights[j] / total == 0) continue;
                float currentRatio = ((float)jobs[j] / population) / ((float)jobWeights[j] / total); // actual fraction / requested fraction
                if(currentRatio >= lowestRatio) continue;
                lowestRatio = currentRatio;
                lowestIndex = j;
            }
            
            for(int j = 0; j < tileJobs.length; j++)
            {
                if(tileJobs[j] == surroundings[j].capacity) continue;
                if((float)tileJobWeights[j] / total == 0) continue;
                float currentRatio = ((float)tileJobs[j] / population) / ((float)tileJobWeights[j] / total); // actual fraction / requested fraction
                if(currentRatio >= lowestRatio) continue;
                lowestRatio = currentRatio;
                lowestIndex = j | 0x0100;
            }

            if(lowestIndex == -1) break;
            if((lowestIndex & 0x0100) == 0)
                jobs[lowestIndex]++;
            else
                tileJobs[lowestIndex & 0xff]++;
        }
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
