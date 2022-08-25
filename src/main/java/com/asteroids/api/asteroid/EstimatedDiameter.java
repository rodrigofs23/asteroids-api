package com.asteroids.api.asteroid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EstimatedDiameter {
    private EstimatedDiameterMeters meters;
}
