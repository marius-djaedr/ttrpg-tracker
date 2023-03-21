namespace TtrpgTrackerLoader.Models;

public class TtrpgDataContainer{
    public Dictionary<string,TtrpgCampaignJson> CampaignMap {get; set;}= new Dictionary<string, TtrpgCampaignJson>();
}