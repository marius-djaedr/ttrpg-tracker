using Microsoft.EntityFrameworkCore;
using TtrpgTrackerApi.Models;
using TtrpgTrackerApi.Models.Sql;

namespace TtrpgTrackerApi.Services.Sql;

public class TtrpgSessionsSqlService :  AbstractSqlService<TtrpgSqlSession,TtrpgSession>,ITtrpgSessionsService
{
    public TtrpgSessionsSqlService() : base(c => c.Sessions, (c,pid) => c.CampaignId==pid || c.CharacterId==pid) {}
    
    protected override TtrpgSession Convert(TtrpgSqlSession entity){
        TtrpgSession view = new TtrpgSession();
        view.Id = entity.Id.ToString();
        if(entity.CampaignId is null){
            view.ParentId = entity.CharacterId.ToString();
        }else{
            view.ParentId = entity.CampaignId.ToString();
        }
        entity.Date = view.Date;
        entity.ShortSession = view.ShortSession;
        entity.PlayedWithoutCharacter = view.PlayedWithoutCharacter;

        return view;
    }

    protected override void SetOntoEntity(TtrpgSession view,TtrpgSqlSession entity){
        view.Date = entity.Date;
        view.ShortSession = entity.ShortSession;
        view.PlayedWithoutCharacter = entity.PlayedWithoutCharacter;
    }
}