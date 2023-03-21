using MongoDB.Bson.Serialization.Attributes;
namespace TtrpgTrackerApi.Models;

public class TtrpgSession : IModel
{
    public string? Id { get; set; }
    public string? ParentId { get; set; }

    public DateTime Date { get; set; }

    public bool ShortSession { get; set; }

//if TRUE, then played and parent is campaign
//if FALSE, then ran and parent is campaign
//if NULL, then played and parent is character
    public bool? PlayedWithoutCharacter { get; set; }

}