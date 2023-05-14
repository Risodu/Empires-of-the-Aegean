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

    public Vector2 add(Vector2 other)
    {
        return new Vector2(x + other.x, y + other.y);
    }

    public static Vector2
        left = new Vector2(-1, 0),
        right = new Vector2(1, 0),
        up = new Vector2(0, -1),
        down = new Vector2(0, 1);
}
