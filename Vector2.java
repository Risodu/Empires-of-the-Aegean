import java.util.Objects;

public class Vector2
{
    public float x, y;
    
    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object object)
    {
        Vector2 other = (Vector2)object;
        return Float.compare(x, other.x) == 0 && Float.compare(y, other.y) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
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

    public static Vector2[] near = new Vector2[]{right, left, up, down};
}
