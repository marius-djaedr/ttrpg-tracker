using System.Net.Http.Headers;
using System.Net.Http.Json;
using Newtonsoft.Json;
using TtrpgTrackerLoader.Models;
using TtrpgTrackerLoader.Models.Rest;

namespace TtrpgTrackerLoader;
public class Program{
    private readonly static HttpClient _client= new HttpClient();
    public static async Task Main(string[] args){
        _client.BaseAddress = new Uri("http://localhost:5138/");
        _client.DefaultRequestHeaders.Accept.Clear();
        _client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));

        await ClearTable();

        string inputFile = Path.Combine("input","campaigns-20230126-072349.json");
        string inputJson = File.ReadAllText(inputFile);
        
        TtrpgDataContainer? data = JsonConvert.DeserializeObject<TtrpgDataContainer?>(inputJson);
        Console.WriteLine(data);
        if(data!=null){
            List<Task> campaignTasks = new List<Task>();

            foreach(TtrpgCampaignJson campaignJson in data.CampaignMap.Values){
                campaignTasks.Add(ConvertAndPostCampaign(campaignJson));
            }

            await Task.WhenAll(campaignTasks);
            Console.WriteLine("Job's done");
        }else{
            Console.WriteLine("Wasn't data to convert");
        }
    }

    private static async Task ClearTable(){
        List<Task> taskList = new List<Task>();
        taskList.Add(ClearAllForType("sessions"));
        taskList.Add(ClearAllForType("characters"));
        taskList.Add(ClearAllForType("campaigns"));

        await Task.WhenAll(taskList);
        Console.WriteLine("Delete's done");
    }

    private static async Task ClearAllForType(string type) {
        List<MongoEntity>? list = await _client.GetFromJsonAsync<List<MongoEntity>>($"api/{type}");
        if(list is null){
            Console.WriteLine($"Wasn't anything to delete for type {type}");
        }else{
            List<Task> taskList = new List<Task>();
            foreach(MongoEntity entity in list){
                string? id = entity.Id;
                if(id is null){
                    throw new NullReferenceException($"Somehow a null ID in type {type}");
                }
                taskList.Add(DeleteOne(type,id));
            }
            await Task.WhenAll(taskList);
            Console.WriteLine($"Delete's done for type {type}");
        }
    }

    private static async Task DeleteOne(string type, string id){
        await _client.DeleteAsync($"api/{type}/{id}");
    }

    private static async Task ConvertAndPostCampaign(TtrpgCampaignJson campaignJson){
        TtrpgCampaign campaignRest = new TtrpgCampaign();
        campaignRest.Type = "CAMPAIGN";
        campaignRest.Name = campaignJson.Name;
        campaignRest.System = campaignJson.System;
        campaignRest.Gm = campaignJson.Gm;
        campaignRest.Completed = campaignJson.CompletedFlg;

        string campaignId=await CreateAsync("campaigns",campaignRest);
        List<Task> childTasks = new List<Task>();

        foreach(TtrpgCharacterJson characterJson in campaignJson.CharactersIPlayed.Values){
            childTasks.Add(ConvertAndPostCharacter(campaignId,characterJson));
        }
        
        foreach(TtrpgSessionJson sessionJson in campaignJson.SessionsIPlayedNoCharacter.Values){
            childTasks.Add(ConvertAndPostSession(campaignId,sessionJson,true));
        }
        
        foreach(TtrpgSessionJson sessionJson in campaignJson.SessionsIRan.Values){
            childTasks.Add(ConvertAndPostSession(campaignId,sessionJson,false));
        }

        await Task.WhenAll(childTasks);
    }

    private static async Task ConvertAndPostCharacter(string campaignId,TtrpgCharacterJson characterJson){
        TtrpgCharacter characterRest = new TtrpgCharacter();
        characterRest.Type = "CHARACTER";
        characterRest.ParentId = campaignId;
        characterRest.Name = characterJson.Name;
        characterRest.Race = characterJson.Race;
        characterRest.ClassRole = characterJson.ClassRole;
        characterRest.Gender = characterJson.Gender;
        characterRest.TragicStory = characterJson.TragicStoryFlg;
        characterRest.DiedInGame = characterJson.DiedInGameFlg;

        string characterId=await CreateAsync("characters",characterRest);
        List<Task> childTasks = new List<Task>();

        foreach(TtrpgSessionJson sessionJson in characterJson.SessionsIPlayed.Values){
            childTasks.Add(ConvertAndPostSession(characterId,sessionJson,null));
        }

        await Task.WhenAll(childTasks);
    }

    private static async Task ConvertAndPostSession(string parentId,TtrpgSessionJson sessionJson,bool? playedWithoutCharacter){
        TtrpgSession sessionRest = new TtrpgSession();
        sessionRest.Type = "SESSION";
        sessionRest.ParentId = parentId;
        sessionRest.ShortSession = sessionJson.ShortFlg??false;
        sessionRest.PlayedWithoutCharacter = playedWithoutCharacter;

        DateJson playDate = sessionJson.PlayDate;
        sessionRest.Date = new DateTime(playDate.Year, playDate.Month, playDate.Day);

        await CreateAsync("sessions",sessionRest);
    }

    private static async Task<string> CreateAsync<E>(string urlParam,E entity)
    {
        HttpResponseMessage response = await _client.PostAsJsonAsync($"api/{urlParam}", entity);
        response.EnsureSuccessStatusCode();

        // return URI of the created resource.
        Uri? location= response.Headers.Location;

        if(location is null){
            throw new NullReferenceException($"Unable to get id for created {urlParam}");
        }
        return location.Segments[3];
    }
}

