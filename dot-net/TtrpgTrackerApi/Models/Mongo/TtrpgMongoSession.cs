using MongoDB.Bson.Serialization.Attributes;
namespace TtrpgTrackerApi.Models.Mongo;

[BsonIgnoreExtraElements]
public class TtrpgMongoSession : MongoEntity
{
    [BsonDateTimeOptions(Kind = DateTimeKind.Local)]
    public DateTime Date { get; set; }

    public bool ShortSession { get; set; }

//if TRUE, then played and parent is campaign
//if FALSE, then ran and parent is campaign
//if NULL, then played and parent is character
    public bool? PlayedWithoutCharacter { get; set; }

}