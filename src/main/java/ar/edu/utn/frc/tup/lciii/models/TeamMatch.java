package ar.edu.utn.frc.tup.lciii.models;

import lombok.Data;

@Data
public class TeamMatch {
    private String teamName;
    private int points;
    private int tries;
    private int yellow_Cards;
    private int red_Cards;
}
