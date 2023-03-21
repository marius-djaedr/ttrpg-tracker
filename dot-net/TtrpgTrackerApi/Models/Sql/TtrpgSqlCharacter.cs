using System.ComponentModel.DataAnnotations.Schema;

namespace TtrpgTrackerApi.Models.Sql;

[Table("CHARACTER")]
public class TtrpgSqlCharacter :ISqlEntity{
    [Column("character_id")]
    public int Id { get; set; }
    
    [Column("name")]
    public string Name { get; set; } = null!;

    [Column("race")]
    public string Race { get; set; } = null!;

    [Column("class_role")]
    public string ClassRole { get; set; } = null!;

    [Column("gender")]
    public string Gender { get; set; } = null!;

    [Column("tragic_story")]
    public bool? TragicStory { get; set; }

    [Column("died_in_game")]
    public bool? DiedInGame { get; set; }


    [Column("campaign_id")]
    public int CampaignId { get; set; }
//    public TtrpgSqlCampaign Campaign { get; set; } = null!;
//    public List<TtrpgSqlSession> Sessions { get; set; }
}