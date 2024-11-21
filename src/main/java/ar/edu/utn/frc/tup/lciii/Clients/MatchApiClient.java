package ar.edu.utn.frc.tup.lciii.Clients;

import ar.edu.utn.frc.tup.lciii.HttpClient.HttpClient;
import ar.edu.utn.frc.tup.lciii.requests.MatchesRequest;
import ar.edu.utn.frc.tup.lciii.requests.StadiumRequest;
import ar.edu.utn.frc.tup.lciii.requests.TeamRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MatchApiClient {
    private final HttpClient httpClient;

    public List<MatchesRequest> fetchMatches(){
        String url = "https://my-json-server.typicode.com/LCIV-2023/fake-api-rwc2023/matches";
        ResponseEntity<List<MatchesRequest>> response =
                httpClient.get(url, new ParameterizedTypeReference<List<MatchesRequest>>() {});
        return response.getBody();
    }

    public List<TeamRequest> fetchTeam(){
        String url = "https://my-json-server.typicode.com/LCIV-2023/fake-api-rwc2023/teams";
        ResponseEntity<List<TeamRequest>> response =
                httpClient.get(url, new ParameterizedTypeReference<List<TeamRequest>>() {
                });
        return  response.getBody();
    }

    public List<StadiumRequest> fetchStadiums(){
        String url = "https://my-json-server.typicode.com/LCIV-2023/fake-api-rwc2023/stadiums";
        ResponseEntity<List<StadiumRequest>> response =
                httpClient.get(url, new ParameterizedTypeReference<List<StadiumRequest>>() {});
        return  response.getBody();
    }
}
