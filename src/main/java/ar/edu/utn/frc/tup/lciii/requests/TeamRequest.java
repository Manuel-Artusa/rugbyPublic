package ar.edu.utn.frc.tup.lciii.requests;

import lombok.Data;

@Data
public class TeamRequest {
    private int id;
    private String name;
    private String country;
    private String world_ranking;
    private String pool;
}
