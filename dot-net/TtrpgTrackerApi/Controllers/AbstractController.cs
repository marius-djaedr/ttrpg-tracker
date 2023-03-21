using TtrpgTrackerApi.Models;
using TtrpgTrackerApi.Services;
using Microsoft.AspNetCore.Mvc;

namespace TtrpgTrackerApi.Controllers;

public class AbstractController<E> : ControllerBase where E:IModel
{
    private readonly IService<E> _service;

    public AbstractController(IService<E> service) =>
        _service = service;

    [HttpGet]
    public async Task<List<E>> Get() =>
        await _service.GetAsync();

    [HttpGet("{id:length(24)}")]
    public async Task<ActionResult<E>> Get(string id)
    {
        var entity = await _service.GetAsync(id);

        if (entity is null)
        {
            return NotFound();
        }

        return entity;
    }

    [HttpPost]
    public async Task<IActionResult> Post(E newEntity)
    {
        await _service.CreateAsync(newEntity);

        return CreatedAtAction(nameof(Get), new { id = newEntity.Id }, newEntity);
    }

    [HttpPut("{id:length(24)}")]
    public async Task<IActionResult> Update(string id, E updatedEntity)
    {
        var entity = await _service.GetAsync(id);

        if (entity is null)
        {
            return NotFound();
        }

        updatedEntity.Id = entity.Id;

        await _service.UpdateAsync(id, updatedEntity);

        return NoContent();
    }

    [HttpDelete("{id:length(24)}")]
    public async Task<IActionResult> Delete(string id)
    {
        var entity = await _service.GetAsync(id);

        if (entity is null)
        {
            return NotFound();
        }

        await _service.RemoveAsync(id);

        return NoContent();
    }
}