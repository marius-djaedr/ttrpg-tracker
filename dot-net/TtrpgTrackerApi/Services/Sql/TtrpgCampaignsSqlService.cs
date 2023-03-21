using Microsoft.EntityFrameworkCore;
using TtrpgTrackerApi.Models;
using TtrpgTrackerApi.Models.Sql;

namespace TtrpgTrackerApi.Services.Sql;

public class TtrpgCampaignsSqlService :  AbstractSqlService<TtrpgSqlCampaign,TtrpgCampaign>,ITtrpgCampaignsService
{
    public TtrpgCampaignsSqlService() : base(c => c.Campaigns, (c,pid) => throw new NotImplementedException()) {}
    
    protected override TtrpgCampaign Convert(TtrpgSqlCampaign entity){
        TtrpgCampaign view = new TtrpgCampaign();
        view.Id = entity.Id.ToString();
        view.Name = entity.Name;
        view.System = entity.System;
        view.Gm = entity.Gm;
        view.Completed = entity.Completed;
        return view;
    }

    protected override void SetOntoEntity(TtrpgCampaign view,TtrpgSqlCampaign entity){
        entity.Name = view.Name;
        entity.System = view.System;
        entity.Gm = view.Gm;
        entity.Completed = view.Completed;
    }
}