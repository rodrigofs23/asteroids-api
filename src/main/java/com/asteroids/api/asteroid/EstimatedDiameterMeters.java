package com.asteroids.api.asteroid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EstimatedDiameterMeters {
  @JsonProperty("estimated_diameter_min")
  private Double estimatedDiameterMin;

  @JsonProperty("estimated_diameter_max")
  private Double estimatedDiameterMax;

  public Double getAverage() {
    return (estimatedDiameterMin + estimatedDiameterMax) / 2;
  }
}
