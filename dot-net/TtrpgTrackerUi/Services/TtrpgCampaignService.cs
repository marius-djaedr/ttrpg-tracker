
using TtrpgTrackerUi.Models;
using TtrpgTrackerUi.Models.Api;

namespace TtrpgTrackerUi.Services;

public class TtrpgCampaignService{
        private readonly TtrpgTrackerApiService _service;

     public TtrpgCampaignService(TtrpgTrackerApiService service)
        {
            _service = service;
        }

    public async Task<List<TtrpgCampaignView>> GetAll(){
        List<TtrpgCampaign> entityList = await _service.GetAll<TtrpgCampaign>("campaigns");
        return entityList.ConvertAll(Convert);
    }

    public void Create(TtrpgCampaignView campaign){
        TtrpgCampaign entity = Convert(campaign);
        _service.Create<TtrpgCampaign>(entity,"campaigns");
    }

    public void Delete(string id){
        _service.Delete(id,"campaigns");
    }

    private TtrpgCampaignView Convert(TtrpgCampaign entity){
        TtrpgCampaignView view = new();
        view.Id=entity.Id;
        view.Name=entity.Name;
        view.System=entity.System;
        view.Gm=entity.Gm;
        view.Completed=entity.Completed is null? CampaignCompleted.Ongoing : entity.Completed.Value? CampaignCompleted.Completed: CampaignCompleted.Abandoned;
        return view;
    }

    private TtrpgCampaign Convert(TtrpgCampaignView view){
        TtrpgCampaign entity = new();
        entity.Id=view.Id;
        entity.Name=view.Name;
        entity.System=view.System;
        entity.Gm=view.Gm;
        switch(view.Completed){
            case CampaignCompleted.Ongoing: 
                entity.Completed=null;
                break;
            case CampaignCompleted.Completed: 
                entity.Completed=true;
                break;
            case CampaignCompleted.Abandoned: 
                entity.Completed=false;
                break;
        }

        return entity;
    }
}