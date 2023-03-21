using TtrpgTrackerApi.Models;
using TtrpgTrackerApi.Services;
using Microsoft.AspNetCore.Mvc;

namespace TtrpgTrackerApi.Controllers;

[ApiController]
[Route("api/[controller]")]
public class SessionsController : AbstractController<TtrpgSession>
{
    public SessionsController(ITtrpgSessionsService sessionsService) : base(sessionsService){
    }
}