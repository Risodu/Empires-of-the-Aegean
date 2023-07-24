public enum Jobs
{
    herder, baker, sawmill_worker, quarry_worker, librarian;

    public String getName()
    {
        return name().substring(0, 1).toUpperCase().concat(name().substring(1).replace("_", " ")).concat("s"); // name.replace('_', ' ').capitalize() + 's'
    }
}
