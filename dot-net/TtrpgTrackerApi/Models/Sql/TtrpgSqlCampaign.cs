using System.ComponentModel.DataAnnotations.Schema;

namespace TtrpgTrackerApi.Models.Sql;

[Table("CAMPAIGN")]
public class TtrpgSqlCampaign :ISqlEntity{
    [Column("campaign_id")]
    public int Id { get; set; }
    
    [Column("name")]
    public string Name { get; set; } = null!;

    [Column("system")]
    public string System { get; set; } = null!;

    [Column("gm")]
    public string Gm { get; set; } = null!;

    [Column("completed")]
    public bool? Completed { get; set; }


//    public List<TtrpgSqlCharacter> Characters { get; set; }
//    public List<TtrpgSqlSession> Sessions { get; set; }
}