public class Vector2
{
    public float x, y;
    
    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Vector2 other)
    {
        return Float.compare(x, other.x) == 0 && Float.compare(y, other.y) == 0;
    }
}
