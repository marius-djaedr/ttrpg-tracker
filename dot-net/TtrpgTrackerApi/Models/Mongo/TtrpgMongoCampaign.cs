using MongoDB.Bson.Serialization.Attributes;
namespace TtrpgTrackerApi.Models.Mongo;

[BsonIgnoreExtraElements]
public class TtrpgMongoCampaign : MongoEntity
{
    public string Name { get; set; } = null!;

    public string System { get; set; } = null!;

    public string Gm { get; set; } = null!;

    public bool? Completed { get; set; }
}