using TtrpgTrackerApi.Models;
using TtrpgTrackerApi.Services;
using Microsoft.Extensions.Options;
using MongoDB.Driver;

namespace TtrpgTrackerApi.Services.Mongo;

public abstract class AbstractMongoService<E,V> : IService<V> where E : MongoEntity where V:IModel
{
    private readonly string _entityType;
    private readonly IMongoCollection<E> _entityCollection;

    public AbstractMongoService(string entityType, MongoDbConnectionProvider connectionProvider)
    {
        _entityType=entityType;

         _entityCollection = connectionProvider.GetCollection<E>();
    }

    public async Task<List<V>> GetAsync() =>
        Convert(await _entityCollection.Find(x => x.Type == _entityType).ToListAsync());

    public async Task<V?> GetAsync(string id) =>
        Convert(await _entityCollection.Find(x => x.Id == id).FirstOrDefaultAsync());

    public async Task<List<V>> GetAllForParentAsync(string parentId) =>
        Convert(await _entityCollection.Find(x => x.Type == _entityType && x.ParentId == parentId).ToListAsync());

    public async Task CreateAsync(V newEntity) {
        await _entityCollection.InsertOneAsync(Convert(newEntity));
    }

    public async Task UpdateAsync(string id, V updatedEntity) {
        await _entityCollection.ReplaceOneAsync(x => x.Id == id, Convert(updatedEntity));
    }

    public async Task RemoveAsync(string id) {
        await _entityCollection.DeleteOneAsync(x => x.Id == id);
    }

    private List<V> Convert(List<E> entityList) =>
        entityList.ConvertAll<V>(this.Convert);

    protected abstract E Convert(V view);
    protected abstract V Convert(E entity);
}