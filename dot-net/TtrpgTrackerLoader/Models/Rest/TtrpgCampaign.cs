namespace TtrpgTrackerLoader.Models.Rest;

public class TtrpgCampaign : MongoEntity
{
    public string Name { get; set; } = null!;

    public string System { get; set; } = null!;

    public string Gm { get; set; } = null!;

    public bool? Completed { get; set; }
}