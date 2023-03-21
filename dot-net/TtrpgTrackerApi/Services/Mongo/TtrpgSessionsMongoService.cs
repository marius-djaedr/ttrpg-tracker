using Microsoft.Extensions.Options;
using TtrpgTrackerApi.Models;
using TtrpgTrackerApi.Models.Mongo;

namespace TtrpgTrackerApi.Services.Mongo;

public class TtrpgSessionsMongoService: AbstractMongoService<TtrpgMongoSession,TtrpgSession>,ITtrpgSessionsService
{
    public TtrpgSessionsMongoService(MongoDbConnectionProvider connectionProvider) : base("SESSION", connectionProvider)
    {
    }

    protected override TtrpgMongoSession Convert(TtrpgSession view)
    {
        TtrpgMongoSession entity = new TtrpgMongoSession();
        entity.Id = view.Id;
        entity.Type = "SESSION";
        entity.ParentId = view.ParentId;
        entity.Date = view.Date;
        entity.ShortSession = view.ShortSession;
        entity.PlayedWithoutCharacter = view.PlayedWithoutCharacter;
        return entity;
    }

    protected override TtrpgSession Convert(TtrpgMongoSession entity)
    {
        TtrpgSession view = new TtrpgSession();
        view.Id = entity.Id;
        view.ParentId = entity.ParentId;
        view.Date = entity.Date;
        view.ShortSession = entity.ShortSession;
        view.PlayedWithoutCharacter = entity.PlayedWithoutCharacter;
        return view;
    }
}