using MongoDB.Bson.Serialization.Attributes;
namespace TtrpgTrackerApi.Models;

public class TtrpgCampaign : IModel
{    
    public string? Id { get; set; }
    
    public string Name { get; set; } = null!;

    public string System { get; set; } = null!;

    public string Gm { get; set; } = null!;

    public bool? Completed { get; set; }
}