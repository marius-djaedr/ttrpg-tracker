namespace TtrpgTrackerLoader.Models;

public class TtrpgSessionJson{
    public string Id {get; set;} = null!;
    public DateJson PlayDate {get; set;} = null!;
    public bool? ShortFlg {get; set;}
}