
namespace TtrpgTrackerUi.Models.Api;

public abstract class AbstractEntity
{
    public string? Id { get; set; }

    public string Type { get; set; } = null!;

    public string? ParentId { get; set; }
}