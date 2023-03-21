using Microsoft.EntityFrameworkCore;
using TtrpgTrackerApi.Models;
using TtrpgTrackerApi.Models.Sql;

namespace TtrpgTrackerApi.Services.Sql;

public class TtrpgCharactersSqlService :  AbstractSqlService<TtrpgSqlCharacter,TtrpgCharacter>,ITtrpgCharactersService
{
    public TtrpgCharactersSqlService() : base(c => c.Characters, (c,pid) => c.CampaignId == pid) {}
    
    protected override TtrpgCharacter Convert(TtrpgSqlCharacter entity){
        TtrpgCharacter view = new TtrpgCharacter();
        view.Id = entity.Id.ToString();
        view.ParentId = entity.CampaignId.ToString();
        view.Name = entity.Name;
        view.Race = entity.Race;
        view.ClassRole = entity.ClassRole;
        view.Gender = entity.Gender;
        view.TragicStory = entity.TragicStory;
        view.DiedInGame = entity.DiedInGame;

        return view;
    }

    protected override void SetOntoEntity(TtrpgCharacter view,TtrpgSqlCharacter entity){
        entity.Name = view.Name;
        entity.Race = view.Race;
        entity.ClassRole = view.ClassRole;
        entity.Gender = view.Gender;
        entity.TragicStory = view.TragicStory;
        entity.DiedInGame = view.DiedInGame;
    }
}