using TtrpgTrackerApi.Models;

namespace TtrpgTrackerApi.Services;
public interface IService<E> where E:IModel{
    Task<List<E>> GetAsync();
    Task<E?> GetAsync(string id);
    Task<List<E>> GetAllForParentAsync(string parentId);
    Task CreateAsync(E newEntity);
    Task UpdateAsync(string id, E updatedEntity);
    Task RemoveAsync(string id);
}