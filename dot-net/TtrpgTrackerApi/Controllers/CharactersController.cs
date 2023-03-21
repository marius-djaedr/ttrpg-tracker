using TtrpgTrackerApi.Models;
using TtrpgTrackerApi.Services;
using Microsoft.AspNetCore.Mvc;

namespace TtrpgTrackerApi.Controllers;

[ApiController]
[Route("api/[controller]")]
public class CharactersController : AbstractController<TtrpgCharacter>
{
    private readonly ITtrpgSessionsService _sessionsService;
    public CharactersController(ITtrpgCharactersService charactersService, ITtrpgSessionsService sessionsService) : base(charactersService){
        _sessionsService = sessionsService;
    }
    
    [HttpGet("{id:length(24)}/sessions")]
    public async Task<List<TtrpgSession>> ChildSessions(string id) =>
        await _sessionsService.GetAllForParentAsync(id);
}