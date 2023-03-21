namespace TtrpgTrackerLoader.Models.Rest;

public class TtrpgCharacter : MongoEntity
{
    public string Name { get; set; } = null!;

    public string Race { get; set; } = null!;

    public string ClassRole { get; set; } = null!;

    public string Gender { get; set; } = null!;

    public bool? TragicStory { get; set; }

    public bool? DiedInGame { get; set; }

}