using Microsoft.EntityFrameworkCore;
using TtrpgTrackerApi.Models.Sql;
namespace TtrpgTrackerApi.Services.Sql;
public class TtrpgTrackerDbContext : DbContext{
    public DbSet<TtrpgSqlCampaign> Campaigns { get; set; }
    public DbSet<TtrpgSqlCharacter> Characters { get; set; }
    public DbSet<TtrpgSqlSession> Sessions { get; set; }

}