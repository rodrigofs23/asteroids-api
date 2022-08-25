package com.asteroids.api.asteroid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Asteroid {

    private Long id;
    private String name;
    @JsonProperty("close_approach_data")
    private List<CloseApproachData> closeApproachData;
    @JsonProperty("estimated_diameter")
    private EstimatedDiameter estimatedDiameter;
}
