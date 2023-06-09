using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace TtrpgTrackerApi.Models;

public class MongoEntity
{
    [BsonId]
    [BsonRepresentation(BsonType.ObjectId)]
    public string? Id { get; set; }

    public string Type { get; set; } = null!;


    [BsonRepresentation(BsonType.ObjectId)]
    public string? ParentId { get; set; }
}