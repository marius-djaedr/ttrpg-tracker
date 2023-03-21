namespace TtrpgTrackerLoader.Models;

public class TtrpgCampaignJson
{
    public string Id {get; set;} = null!;
    public string Name { get; set; }= null!;
    public string System { get; set; }= null!;
    public string Gm { get; set; }= null!;
    public bool? CompletedFlg { get; set; }
    public Dictionary<string,TtrpgCharacterJson> CharactersIPlayed {get; set;}= new Dictionary<string, TtrpgCharacterJson>();
    public Dictionary<string,TtrpgSessionJson> SessionsIPlayedNoCharacter {get; set;}= new Dictionary<string, TtrpgSessionJson>();
    public Dictionary<string,TtrpgSessionJson> SessionsIRan {get; set;}= new Dictionary<string, TtrpgSessionJson>();
}