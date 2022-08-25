package com.asteroids.api.asteroid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AsteroidResponse {
    private long id;
    private String name;
    @JsonProperty("close_approach_data")
    private String closeApproachDate;
    @JsonProperty("distance_from_earth_in_km")
    private String distanceFromEarthInKilometers;
    @JsonProperty("estimated_diameter")
    private EstimatedDiameterMeters estimatedDiameter;
}
