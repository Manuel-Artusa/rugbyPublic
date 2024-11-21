package ar.edu.utn.frc.tup.lciii.models;

import lombok.Data;

import java.util.List;

@Data
public class Pool {
    private String pool_Id;
    private List<Team> teams;
}
