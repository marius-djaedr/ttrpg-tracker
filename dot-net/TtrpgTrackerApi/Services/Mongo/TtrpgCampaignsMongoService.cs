using TtrpgTrackerApi.Models;
using TtrpgTrackerApi.Models.Mongo;
using Microsoft.Extensions.Options;

namespace TtrpgTrackerApi.Services.Mongo;

public class TtrpgCampaignsMongoService : AbstractMongoService<TtrpgMongoCampaign,TtrpgCampaign>,ITtrpgCampaignsService
{
    public TtrpgCampaignsMongoService(MongoDbConnectionProvider connectionProvider) : base("CAMPAIGN", connectionProvider)
    {
    }

    protected override TtrpgMongoCampaign Convert(TtrpgCampaign view)
    {
        TtrpgMongoCampaign entity = new TtrpgMongoCampaign();
        entity.Id = view.Id;
        entity.Type = "CAMPAIGN";
        entity.Name = view.Name;
        entity.System = view.System;
        entity.Gm = view.Gm;
        entity.Completed = view.Completed;
        return entity;
    }

    protected override TtrpgCampaign Convert(TtrpgMongoCampaign entity)
    {
        TtrpgCampaign view = new TtrpgCampaign();
        view.Id = entity.Id;
        view.Name = entity.Name;
        view.System = entity.System;
        view.Gm = entity.Gm;
        view.Completed = entity.Completed;
        return view;
    }
}