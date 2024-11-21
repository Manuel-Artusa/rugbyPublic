package ar.edu.utn.frc.tup.lciii.models;

import lombok.Data;

@Data
public class Team {
    private int teamId;
    private String teamName;
    private String country;
    private int matchesPlayed;
    private int wins;
    private int draws;
    private int losses;
    private int pointsFor;
    private int pointsAgainst;
    private int pointsDifferential;
    private int triesMade;
    private int bonusPoints;
    private int points;
    private int totalYellowCards;
    private int totalRedCards;
}
