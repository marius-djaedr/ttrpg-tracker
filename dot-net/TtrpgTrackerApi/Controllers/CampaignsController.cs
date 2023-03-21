using TtrpgTrackerApi.Models;
using TtrpgTrackerApi.Services;
using Microsoft.AspNetCore.Mvc;

namespace TtrpgTrackerApi.Controllers;

[ApiController]
[Route("api/[controller]")]
public class CampaignsController : AbstractController<TtrpgCampaign>
{
    private readonly ITtrpgCharactersService _charactersService;
    private readonly ITtrpgSessionsService _sessionsService;
    public CampaignsController(ITtrpgCampaignsService campaignsService, ITtrpgCharactersService charactersService, ITtrpgSessionsService sessionsService) : base(campaignsService){
        _charactersService = charactersService;
        _sessionsService = sessionsService;
    }

    [HttpGet("{id:length(24)}/characters")]
    public async Task<List<TtrpgCharacter>> ChildCharacters(string id) =>
        await _charactersService.GetAllForParentAsync(id);

    [HttpGet("{id:length(24)}/sessions")]
    public async Task<List<TtrpgSession>> ChildSessions(string id) =>
        await _sessionsService.GetAllForParentAsync(id);
}