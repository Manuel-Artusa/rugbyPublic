package ar.edu.utn.frc.tup.lciii.models;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Match {
    private int id;
    private String date;
    private List<TeamMatch> teams;
    private String stadium;
    private String pool;
}
