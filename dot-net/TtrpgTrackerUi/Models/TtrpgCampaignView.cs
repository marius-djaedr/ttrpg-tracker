namespace TtrpgTrackerUi.Models;

public class TtrpgCampaignView
{    
    public string? Id { get; set; }

    public string Name { get; set; } = null!;

    public string System { get; set; } = null!;

    public string Gm { get; set; } = null!;

    public CampaignCompleted Completed { get; set; }
}

public enum CampaignCompleted {Ongoing, Completed, Abandoned}