using TtrpgTrackerApi.Models;
using TtrpgTrackerApi.Models.Sql;
using Microsoft.EntityFrameworkCore;

namespace TtrpgTrackerApi.Services.Sql;

public abstract class AbstractSqlService<E,V> 
                where E : class, ISqlEntity, new() 
                where V : IModel
{
    private readonly Func<TtrpgTrackerDbContext,DbSet<E>> _tableFunction;
    private readonly Func<E,int,bool> _parentPredicate;
    
    public AbstractSqlService(Func<TtrpgTrackerDbContext,DbSet<E>> tableFunction, Func<E,int,bool> parentPredicate){
        _tableFunction=tableFunction;
        _parentPredicate=parentPredicate;
    }

    public async Task<List<V>> GetAsync(){
        using(var context = new TtrpgTrackerDbContext()){
            return Convert(await _tableFunction(context).ToListAsync());
        }
    }

    public async Task<V?> GetAsync(string id)
    {
        using(var context = new TtrpgTrackerDbContext()){
            return Convert(await CommonQuery(context,id));
        }
    }

    public async Task<List<V>> GetAllForParentAsync(string parentId)
    {
        using(var context = new TtrpgTrackerDbContext()){
            int id = int.Parse(parentId);
            return Convert(await _tableFunction(context).Where(e => _parentPredicate(e,id)).ToListAsync());
        }
    }

    public async Task CreateAsync(V newEntity)
    {
        using(var context = new TtrpgTrackerDbContext()){
            _tableFunction(context).Add(Convert(newEntity));
            await context.SaveChangesAsync();
        }
    }

    public async Task UpdateAsync(string id, V updatedEntity)
    {
        using(var context = new TtrpgTrackerDbContext()){
            E found =await CommonQuery(context,id);
            SetOntoEntity(updatedEntity,found);
            await context.SaveChangesAsync();
        }
    }

    public async Task RemoveAsync(string id)
    {
        using(var context = new TtrpgTrackerDbContext()){
            E found = await CommonQuery(context,id);
            _tableFunction(context).Remove(found);
            await context.SaveChangesAsync();
        }
    }

    private Task<E> CommonQuery(TtrpgTrackerDbContext context, string stringId){
        int id = int.Parse(stringId);
        return _tableFunction(context).SingleAsync(e => e.Id==id);
    }

    protected List<V> Convert(List<E> entityList) =>
        entityList.ConvertAll<V>(this.Convert);
    

    protected E Convert(V view){
        E entity = new E();
        SetOntoEntity(view,entity);
        return entity;
    }

    protected abstract V Convert(E entity);
    protected abstract void SetOntoEntity(V view,E entity);
}