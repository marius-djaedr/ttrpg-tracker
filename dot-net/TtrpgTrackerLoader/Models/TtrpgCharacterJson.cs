namespace TtrpgTrackerLoader.Models;

public class TtrpgCharacterJson
{
    public string Id {get; set;} = null!;
    public string Name { get; set; } = null!;
    public string Race { get; set; } = null!;
    public string ClassRole { get; set; } = null!;
    public string Gender { get; set; } = null!;
    public bool? TragicStoryFlg { get; set; }
    public bool? DiedInGameFlg { get; set; }
    public Dictionary<string,TtrpgSessionJson> SessionsIPlayed {get; set;}= new Dictionary<string, TtrpgSessionJson>();
}