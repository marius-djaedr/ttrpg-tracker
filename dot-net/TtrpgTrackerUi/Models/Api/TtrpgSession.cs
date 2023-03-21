namespace TtrpgTrackerUi.Models.Api;


public class TtrpgSession : AbstractEntity
{
    public DateTime Date { get; set; }

    public bool ShortSession { get; set; }

//if TRUE, then played and parent is campaign
//if FALSE, then ran and parent is campaign
//if NULL, then played and parent is character
    public bool? PlayedWithoutCharacter { get; set; }

}