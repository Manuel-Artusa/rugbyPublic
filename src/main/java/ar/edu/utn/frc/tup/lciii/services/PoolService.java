package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.Clients.MatchApiClient;
import ar.edu.utn.frc.tup.lciii.models.Match;
import ar.edu.utn.frc.tup.lciii.models.Pool;
import ar.edu.utn.frc.tup.lciii.models.Team;
import ar.edu.utn.frc.tup.lciii.models.TeamMatch;
import ar.edu.utn.frc.tup.lciii.requests.MatchesRequest;
import ar.edu.utn.frc.tup.lciii.requests.StadiumRequest;
import ar.edu.utn.frc.tup.lciii.requests.TeamMatchRequest;
import ar.edu.utn.frc.tup.lciii.requests.TeamRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PoolService {
    private final MatchApiClient matchApiClient;

    public List<Pool> getPools(String poolFilter) {
        List<MatchesRequest> matches = matchApiClient.fetchMatches();
        List<TeamRequest> teams = matchApiClient.fetchTeam();

        if (poolFilter != null && !poolFilter.isEmpty()) {
            matches.removeIf(match -> !match.getPool().equalsIgnoreCase(poolFilter));
        }

        return calculateStandings(matches, teams);
    }

    private List<Pool> calculateStandings(List<MatchesRequest> matches, List<TeamRequest> teams) {
        Map<String, List<Team>> poolMap = new HashMap<>();

        for (MatchesRequest match : matches) {
            String poolName = match.getPool();
            poolMap.putIfAbsent(poolName, new ArrayList<>());

            TeamMatchRequest team1Data = match.getTeams().get(0);
            TeamMatchRequest team2Data = match.getTeams().get(1);

            Team team1 = findOrCreate(poolMap.get(poolName), team1Data.getId(), teams);
            Team team2 = findOrCreate(poolMap.get(poolName), team2Data.getId(), teams);

            updateTeamStats(match, team1, team2, team1Data, team2Data);
        }

        // Convertir el mapa en una lista de objetos Pool
        return poolMap.entrySet().stream().map(entry -> {
            Pool pool = new Pool();
            pool.setPool_Id(entry.getKey()); // Nombre del pool
            pool.setTeams(entry.getValue()); // Lista de equipos en el pool
            return pool;
        }).toList();
    }

    private Team findOrCreate(List<Team> poolTeams, int teamId, List<TeamRequest> allTeams) {
        return poolTeams.stream()
                .filter(team -> team.getTeamId() == teamId)
                .findFirst()
                .orElseGet(() -> {
                    // Buscar información del equipo en la lista global
                    TeamRequest teamRequest = allTeams.stream()
                            .filter(t -> t.getId() == teamId)
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("Team not found: " + teamId));

                    // Crear y agregar el equipo al pool
                    Team team = new Team();
                    team.setTeamId(teamRequest.getId());
                    team.setTeamName(teamRequest.getName());
                    team.setCountry(teamRequest.getCountry());
                    poolTeams.add(team);
                    return team;
                });
    }

    private void updateTeamStats(MatchesRequest match, Team team1, Team team2, TeamMatchRequest data1, TeamMatchRequest data2) {
        int points1 = data1.getPoints();
        int points2 = data2.getPoints();

        team1.setMatchesPlayed(team1.getMatchesPlayed() + 1);
        team2.setMatchesPlayed(team2.getMatchesPlayed() + 1);

        // Actualizar estadísticas de puntos
        team1.setPointsFor(team1.getPointsFor() + points1);
        team1.setPointsAgainst(team1.getPointsAgainst() + points2);
        team2.setPointsFor(team2.getPointsFor() + points2);
        team2.setPointsAgainst(team2.getPointsAgainst() + points1);

        // Actualizar resultados (win, draw, loss)
        if (points1 > points2) {
            team1.setWins(team1.getWins() + 1);
            team2.setLosses(team2.getLosses() + 1);
            team1.setPoints(team1.getPoints() + 4); // 4 puntos por victoria
        } else if (points1 < points2) {
            team2.setWins(team2.getWins() + 1);
            team1.setLosses(team1.getLosses() + 1);
            team2.setPoints(team2.getPoints() + 4); // 4 puntos por victoria
        } else {
            team1.setDraws(team1.getDraws() + 1);
            team2.setDraws(team2.getDraws() + 1);
            team1.setPoints(team1.getPoints() + 2); // 2 puntos por empate
            team2.setPoints(team2.getPoints() + 2); // 2 puntos por empate
        }

        // Puntos de bonus
        if (data1.getTries() >= 4) {
            team1.setBonusPoints(team1.getBonusPoints() + 1);
            team1.setPoints(team1.getPoints() + 1);
        }
        if (data2.getTries() >= 4) {
            team2.setBonusPoints(team2.getBonusPoints() + 1);
            team2.setPoints(team2.getPoints() + 1);
        }

        if (Math.abs(points1 - points2) <= 7) {
            if (points1 < points2) {
                team1.setBonusPoints(team1.getBonusPoints() + 1);
                team1.setPoints(team1.getPoints() + 1);
            }
            if (points2 < points1) {
                team2.setBonusPoints(team2.getBonusPoints() + 1);
                team2.setPoints(team2.getPoints() + 1);
            }
        }

        // Actualizar estadísticas adicionales
        team1.setTriesMade(team1.getTriesMade() + data1.getTries());
        team2.setTriesMade(team2.getTriesMade() + data2.getTries());

        team1.setTotalYellowCards(team1.getTotalYellowCards() + data1.getYellow_cards());
        team2.setTotalYellowCards(team2.getTotalYellowCards() + data2.getYellow_cards());

        team1.setTotalRedCards(team1.getTotalRedCards() + data1.getRed_cards());
        team2.setTotalRedCards(team2.getTotalRedCards() + data2.getRed_cards());
    }
    public List<StadiumRequest> getStadiums(){
        List<StadiumRequest> stadiumRequestList = matchApiClient.fetchStadiums();
        return stadiumRequestList;
    }
    public StadiumRequest getStadiumsById(int id){
        List<StadiumRequest> stadiumRequestList = matchApiClient.fetchStadiums();
        return stadiumRequestList.stream()
                .filter(stadiumRequest ->  id == stadiumRequest.getId())
                .findFirst()
                .orElse(null);
    }
    public List<Match> getMatches(){
        List<MatchesRequest> matchesRequests = matchApiClient.fetchMatches();
        List<StadiumRequest> stadiumRequestList = matchApiClient.fetchStadiums();
        List<TeamRequest> teamMatchRequests = matchApiClient.fetchTeam();

        return matchesRequests.stream()
                .map(matchesRequest -> transformToMatch(matchesRequest,stadiumRequestList,teamMatchRequests))
                .toList();
    }

    public Match transformToMatch(MatchesRequest matchesRequest, List<StadiumRequest> stadiumRequest, List<TeamRequest> teamRequest){
        Match match = new Match();
        match.setId(matchesRequest.getId());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(matchesRequest.getDate());
        match.setDate(formattedDate);

        match.setPool(matchesRequest.getPool());

        StadiumRequest stadium = getStadiums().stream()
                .filter(s -> s.getId() == matchesRequest.getStadium())
                .findFirst()
                .orElse(null);
        match.setStadium(stadium.getName());

        List<TeamMatch> teamMatches = matchesRequest.getTeams().stream()
                .map(teamMatchRequest -> mapTeamMatch(teamMatchRequest,teamRequest))
                .toList();
        match.setTeams(teamMatches);

        return match;

    }

    private TeamMatch mapTeamMatch(TeamMatchRequest teamMatchRequest, List<TeamRequest> teamRequest) {
        TeamRequest teamRequest1 = teamRequest.stream()
                .filter(t -> t.getId() == teamMatchRequest.getId())
                .findFirst()
                .orElse(null);
        TeamMatch teamMatch = new TeamMatch();

        if (teamRequest != null){
            teamMatch.setTeamName(teamRequest1.getName());
        }else{
            teamMatch.setTeamName("unknow Team");
        }

        teamMatch.setTries(teamMatchRequest.getTries());
        teamMatch.setRed_Cards(teamMatch.getRed_Cards());
        teamMatch.setYellow_Cards(teamMatch.getYellow_Cards());
        teamMatch.setPoints(teamMatch.getPoints());
        return teamMatch;
    }

}
