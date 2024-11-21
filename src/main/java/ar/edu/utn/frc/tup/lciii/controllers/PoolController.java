package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.models.Match;
import ar.edu.utn.frc.tup.lciii.models.Pool;
import ar.edu.utn.frc.tup.lciii.requests.StadiumRequest;
import ar.edu.utn.frc.tup.lciii.services.PoolService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PoolController {

    private final PoolService poolService;
    @GetMapping("/getAll")
    public ResponseEntity<List<Pool>> getPools() {
        return ResponseEntity.ok(poolService.getPools(null));
    }
    @GetMapping("/{poolId}")
    public ResponseEntity<List<Pool>> getPools(@PathVariable("poolId")String poolId) {
        return ResponseEntity.ok(poolService.getPools(poolId));
    }
    @GetMapping("/stadiums")
    public  ResponseEntity<List<StadiumRequest>> getStadiums(){
        return ResponseEntity.ok(poolService.getStadiums());
    }
    @GetMapping("/stadiumById")
    public  ResponseEntity<StadiumRequest> getStadiums(int id){
        return ResponseEntity.ok(poolService.getStadiumsById(id));
    }
    @GetMapping("/matches")
    public  ResponseEntity<List<Match>> getMatches(){
        return ResponseEntity.ok(poolService.getMatches());
    }


}
