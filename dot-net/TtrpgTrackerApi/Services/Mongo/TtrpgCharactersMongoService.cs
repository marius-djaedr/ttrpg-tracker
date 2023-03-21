using Microsoft.Extensions.Options;
using TtrpgTrackerApi.Models;
using TtrpgTrackerApi.Models.Mongo;

namespace TtrpgTrackerApi.Services.Mongo;

public class TtrpgCharactersMongoService: AbstractMongoService<TtrpgMongoCharacter,TtrpgCharacter>,ITtrpgCharactersService
{
    public TtrpgCharactersMongoService(MongoDbConnectionProvider connectionProvider) : base("CHARACTER", connectionProvider)
    {
    }

    protected override TtrpgMongoCharacter Convert(TtrpgCharacter view)
    {
        TtrpgMongoCharacter entity = new TtrpgMongoCharacter();
        entity.Id = view.Id;
        entity.Type = "CHARACTER";
        entity.ParentId = view.ParentId;
        entity.Name = view.Name;
        entity.Race = view.Race;
        entity.ClassRole = view.ClassRole;
        entity.Gender = view.Gender;
        entity.TragicStory = view.TragicStory;
        entity.DiedInGame = view.DiedInGame;
        return entity;
    }

    protected override TtrpgCharacter Convert(TtrpgMongoCharacter entity)
    {
        TtrpgCharacter view = new TtrpgCharacter();
        view.Id = entity.Id;
        view.ParentId = entity.ParentId;
        view.Name = entity.Name;
        view.Race = entity.Race;
        view.ClassRole = entity.ClassRole;
        view.Gender = entity.Gender;
        view.TragicStory = entity.TragicStory;
        view.DiedInGame = entity.DiedInGame;
        return view;
    }
}