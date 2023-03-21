using System.ComponentModel.DataAnnotations.Schema;

namespace TtrpgTrackerApi.Models.Sql;

[Table("SESSION")]
public class TtrpgSqlSession :ISqlEntity{
    [Column("character_id")]
    public int Id { get; set; }
    
    [Column("date")]
    public DateTime Date { get; set; }

    [Column("short_session")]
    public bool ShortSession { get; set; }

//if TRUE, then played and parent is campaign
//if FALSE, then ran and parent is campaign
//if NULL, then played and parent is character
    [Column("played_without_character")]
    public bool? PlayedWithoutCharacter { get; set; }
    

    [Column("campaign_id")]
    public int? CampaignId { get; set; }
//    public TtrpgSqlCampaign? Campaign { get; set; }

    [Column("character_id")]
    public int? CharacterId { get; set; }
//    public TtrpgSqlCharacter? Character { get; set; }
}