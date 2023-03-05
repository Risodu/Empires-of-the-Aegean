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
        // return new Vector2((int)pos.x + x / scale, (int)pos.y + y / scale);
        return new Vector2((int)Math.floor(pos.x + x / scale), (int)Math.floor(pos.y + y / scale));
    }

    public Vector2 worldToScreen(float x, float y)
    {
        return new Vector2((x - pos.x) * scale, (y - pos.y) * scale);
    }
}
