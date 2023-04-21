namespace TtrpgTrackerLoader.Models.Rest;

public class MongoEntity
{
    public string? _id { get; set; }

    public string Type { get; set; } = null!;


    public string? ParentId { get; set; }
}