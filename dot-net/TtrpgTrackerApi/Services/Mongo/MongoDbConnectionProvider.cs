using TtrpgTrackerApi.Models.Mongo;
using Microsoft.Extensions.Options;
using MongoDB.Driver;

namespace TtrpgTrackerApi.Services.Mongo;

public class MongoDbConnectionProvider{

    private readonly IMongoDatabase _database;
    private readonly string _collectionName;

    public MongoDbConnectionProvider(IOptions<TtrpgTrackerMongoDatabaseSettings> databaseSettings)
    {
        
        var settings = MongoClientSettings.FromConnectionString(databaseSettings.Value.ConnectionString);
        settings.WaitQueueSize = 5000;
        settings.ServerApi = new ServerApi(ServerApiVersion.V1);
        var mongoClient = new MongoClient(settings);

         _database = mongoClient.GetDatabase(databaseSettings.Value.DatabaseName);
         _collectionName = databaseSettings.Value.CollectionName;
    }
    
    public IMongoCollection<E> GetCollection<E>(){
        return _database.GetCollection<E>(_collectionName);
    }
}