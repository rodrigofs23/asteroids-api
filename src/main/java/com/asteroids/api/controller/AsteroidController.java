package com.asteroids.api.controller;

import com.asteroids.api.asteroid.Asteroid;
import com.asteroids.api.asteroid.AsteroidResponse;
import com.asteroids.api.service.AsteroidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/asteroid")
public class AsteroidController {

  private final AsteroidService asteroidService;

  @Autowired
  public AsteroidController(AsteroidService asteroidService) {
    this.asteroidService = asteroidService;
  }

  @GetMapping("/closest-asteroids")
  public ResponseEntity<List<AsteroidResponse>> getClosestAsteroids(
      @RequestParam String startDate, @RequestParam String endDate) {
    List<Asteroid> closestAsteroids = asteroidService.getClosestAsteroids(startDate, endDate);

    List<AsteroidResponse> response = new ArrayList<>();
    closestAsteroids.forEach(
        asteroid -> {
          AsteroidResponse build =
              AsteroidResponse.builder()
                  .id(asteroid.getId())
                  .name(asteroid.getName())
                  .closeApproachDate(
                      asteroid.getCloseApproachData().get(0).getCloseApproachDate().toString())
                  .distanceFromEarthInKilometers(
                      asteroid.getCloseApproachData().get(0).getMissDistance().getKilometers())
                  .build();
          response.add(build);
        });

    return ResponseEntity.ok(response);
  }

  @GetMapping("/largest-asteroid")
  public ResponseEntity<Asteroid> getLargestAsteroid(@RequestParam String year) {
    Asteroid largestAsteroid = asteroidService.getLargestAsteroid(year);
    return ResponseEntity.ok(
        Asteroid.builder()
            .id(largestAsteroid.getId())
            .name(largestAsteroid.getName())
            .estimatedDiameter(largestAsteroid.getEstimatedDiameter())
            .build());
  }
}
