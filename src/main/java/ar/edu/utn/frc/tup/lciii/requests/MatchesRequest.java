package ar.edu.utn.frc.tup.lciii.requests;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class MatchesRequest {
    private int id;
    private Date date;
    private List<TeamMatchRequest> teams;
    private int stadium;
    private String pool;
}
