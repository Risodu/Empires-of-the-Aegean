public enum Jobs
{
    farmer, builder, baker;

    public String getName()
    {
        return name().substring(0, 1).toUpperCase().concat(name().substring(1)).concat("s"); // name.capitalize() + 's'
    }
}
