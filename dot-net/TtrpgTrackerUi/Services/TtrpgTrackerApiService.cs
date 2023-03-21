using System.Net.Http.Headers;
using TtrpgTrackerUi.Models.Api;

namespace TtrpgTrackerUi.Services;

public class TtrpgTrackerApiService{
    
    private readonly HttpClient _client= new HttpClient();
    public TtrpgTrackerApiService(){
        _client.BaseAddress = new Uri("http://localhost:5138/api/");
        _client.DefaultRequestHeaders.Accept.Clear();
        _client.DefaultRequestHeaders.Accept.Add(new MediaTypeWithQualityHeaderValue("application/json"));
    }

    public async Task<List<E>> GetAll<E>(string type) where E:AbstractEntity{
       return await _client.GetFromJsonAsync<List<E>>(type) ?? new();
    }

    public void Create<E>(E entity, string type)where E:AbstractEntity{

    }

    public void Delete(string id, string type){
        
    }
}