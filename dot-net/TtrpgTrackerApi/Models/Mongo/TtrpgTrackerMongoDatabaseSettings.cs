namespace TtrpgTrackerApi.Models.Mongo;

public class TtrpgTrackerMongoDatabaseSettings
{
    public string ConnectionString { get; set; } = null!;

    public string DatabaseName { get; set; } = null!;

    public string CollectionName { get; set; } = null!;
}