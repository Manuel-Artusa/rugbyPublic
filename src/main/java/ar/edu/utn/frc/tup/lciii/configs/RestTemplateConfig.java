package ar.edu.utn.frc.tup.lciii.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate(){return new RestTemplate();}
}
