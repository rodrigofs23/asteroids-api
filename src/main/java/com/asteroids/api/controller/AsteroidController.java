package com.asteroids.api.controller;

import com.asteroids.api.asteroid.Asteroid;
import com.asteroids.api.service.AsteroidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  public List<Asteroid> getAsteroids(@RequestParam String startDate, @RequestParam String endDate) {
    return asteroidService.getClosestAsteroids(startDate, endDate);
  }

  @GetMapping("/largest-asteroid")
    public Asteroid getLargestAsteroid(@RequestParam String year) {
        return asteroidService.getLargestAsteroid(year);
    }
}
