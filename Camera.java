public class Camera
{
    public Vector2 pos;
    public float scale; // pixels per unit

    public Camera()
    {
        pos = new Vector2(0, 0);
        scale = 20;
    }

    public Vector2 screenToWorld(float x, float y)
    {
        return new Vector2((int)Math.floor(pos.x + x / (int)scale), (int)Math.floor(pos.y + y / (int)scale));
    }

    public Vector2 worldToScreen(float x, float y)
    {
        return new Vector2((x - pos.x) * (int)scale, (y - pos.y) * (int)scale);
    }

    public void changeScale(float multiplier, float x, float y)
    {
        float newScale = scale * multiplier;
        pos.x += x / (int)scale - x / (int)newScale;
        pos.y += y / (int)scale - y / (int)newScale;
        scale = newScale;
    }
}
