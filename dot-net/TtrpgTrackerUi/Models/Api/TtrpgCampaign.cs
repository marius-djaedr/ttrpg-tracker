namespace TtrpgTrackerUi.Models.Api;

public class TtrpgCampaign : AbstractEntity
{
    public string Name { get; set; } = null!;

    public string System { get; set; } = null!;

    public string Gm { get; set; } = null!;

    public bool? Completed { get; set; }
}