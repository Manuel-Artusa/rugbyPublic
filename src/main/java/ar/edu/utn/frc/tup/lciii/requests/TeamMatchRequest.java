package ar.edu.utn.frc.tup.lciii.requests;

import lombok.Data;

@Data
public class TeamMatchRequest {
    private int id;
    private int points;
    private int tries;
    private int yellow_cards;
    private int red_cards;
}
