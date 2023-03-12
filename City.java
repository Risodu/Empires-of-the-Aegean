public class City
{
    public Vector2 position;
    public int r, g, b, population, production, farmers, builders;

    public City(Vector2 pos)
    {
        position = pos;
        r = 255;
        g = 170;
        b = 0;
        population = 10;
    }

    public void endTurn()
    {
        production += builders;
        float food = farmers - population * 0.2f;
        int populationInc = Math.round(food > 0 ? food * 0.5f : food * 2);
        float multiplier = 1 + ((float)populationInc / population);
        System.out.println(populationInc);
        farmers *= multiplier;
        builders *= multiplier;
        population += populationInc;
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
