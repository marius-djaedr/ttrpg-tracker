using TtrpgTrackerApi.Models.Mongo;
using TtrpgTrackerApi.Services;
using TtrpgTrackerApi.Services.Mongo;
using TtrpgTrackerApi.Services.Sql;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

//AddMongoSingletons(builder);
AddSqlSingletons(builder);
builder.Services.AddControllers();

// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();

void AddMongoSingletons(WebApplicationBuilder builder){
    builder.Services.Configure<TtrpgTrackerMongoDatabaseSettings>(builder.Configuration.GetSection("TtrpgTrackerMongoDatabase"));
    builder.Services.AddSingleton<MongoDbConnectionProvider>();
    builder.Services.AddSingleton<ITtrpgCampaignsService,TtrpgCampaignsMongoService>();
    builder.Services.AddSingleton<ITtrpgCharactersService,TtrpgCharactersMongoService>();
    builder.Services.AddSingleton<ITtrpgSessionsService,TtrpgSessionsMongoService>();
}

void AddSqlSingletons(WebApplicationBuilder builder){
    builder.Services.AddSingleton<ITtrpgCampaignsService,TtrpgCampaignsSqlService>();
    builder.Services.AddSingleton<ITtrpgCharactersService,TtrpgCharactersSqlService>();
    builder.Services.AddSingleton<ITtrpgSessionsService,TtrpgSessionsSqlService>();
}