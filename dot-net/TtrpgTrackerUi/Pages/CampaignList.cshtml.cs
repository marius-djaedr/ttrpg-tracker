using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.RazorPages;
using TtrpgTrackerUi.Models;
using TtrpgTrackerUi.Services;

namespace TtrpgTrackerUi.Pages
{
    public class CampaignListModel : PageModel
    {
        private readonly TtrpgCampaignService _service;
        public List<TtrpgCampaignView> campaigns = new();

        [BindProperty]
        public TtrpgCampaignView NewCampaign { get; set; } = new();
        public CampaignListModel(TtrpgCampaignService service)
        {
            _service = service;
        }
        public async Task<IActionResult> OnGetAsync()
        {
             campaigns = await _service.GetAll();
             return Page();
        }

        public IActionResult OnPost()
        {
            if (!ModelState.IsValid)
            {
                return Page();
            }
            _service.Create(NewCampaign);
            return RedirectToAction("Get");
        }

        //  public IActionResult OnPostUpdate(string id)
        // {
        //     _service.Update(id, "campaigns");
        //     return RedirectToAction("Get");
        // }

        public IActionResult OnPostDelete(string id)
        {
            _service.Delete(id);
            return RedirectToAction("Get");
        }
    }
}
