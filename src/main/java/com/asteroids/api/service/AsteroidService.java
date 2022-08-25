package com.asteroids.api.service;

import com.asteroids.api.asteroid.Asteroid;
import com.asteroids.api.dataclient.AsteroidsNeowsCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class AsteroidService {

    private final AsteroidsNeowsCache asteroidsNeowsCache;

    @Autowired
    public AsteroidService(AsteroidsNeowsCache asteroidsNeowsCache) {
        this.asteroidsNeowsCache = asteroidsNeowsCache;
    }

    public List<Asteroid> getClosestAsteroids(String startDate, String endDate) {
        List<Asteroid> asteroidsList = getAsteroidList(startDate, endDate);
        return getTop10ClosestAsteroids(asteroidsList);
    }

    public Asteroid getLargestAsteroid(String year) {
        Asteroid largestAsteroid = null;
        LocalDate startDate = LocalDate.parse(getStartDate(year));
        LocalDate endDate = LocalDate.parse(getEndDate(year));
        LocalDate lastDate = startDate.plusYears(1);
        int cont = 1;

        while (startDate.isBefore(lastDate)) {
            System.out.println("startDate: " + startDate + ", endDate: " + endDate + ". Calling Nasa API: " + cont);
            List<Asteroid> asteroidsList = getAsteroidList(startDate.toString(), endDate.toString());
            if (!asteroidsList.isEmpty()) {
                largestAsteroid = checkLargestAsteroid(largestAsteroid, asteroidsList);
            }
            startDate = startDate.plusWeeks(1);
            endDate = endDate.plusWeeks(1);
            cont++;
        }
        return largestAsteroid;
    }

    private Asteroid checkLargestAsteroid(Asteroid largestAsteroid, List<Asteroid> asteroidsList) {
        if (largestAsteroid == null) {
            largestAsteroid = getLargestAsteroidFromList(asteroidsList);
        }
        Asteroid tempAsteroid = getLargestAsteroidFromList(asteroidsList);
        if (tempAsteroid.getEstimatedDiameter().getMeters().getAverage() > largestAsteroid.getEstimatedDiameter().getMeters().getAverage()) {
            largestAsteroid = tempAsteroid;
        }
        return largestAsteroid;
    }

    private List<Asteroid> getAsteroidList(String startDate, String endDate) {
        JsonNode asteroids = asteroidsNeowsCache.getAsteroids(startDate, endDate);
        return asteroidListMapper(asteroids);
    }

    private List<Asteroid> asteroidListMapper(JsonNode asteroids) {
        List<Asteroid> asteroidList = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();

        asteroids.get("near_earth_objects").forEach(date -> date.forEach(asteroid -> {
            try {
                asteroidList.add(mapper.treeToValue(asteroid, Asteroid.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }));
        return asteroidList;
    }

    private List<Asteroid> getTop10ClosestAsteroids(List<Asteroid> asteroidList) {
        int size = Math.min(asteroidList.size(), 10);
        return sortAsteroidsByDistance(asteroidList).subList(0, size);
    }

    private List<Asteroid> sortAsteroidsByDistance(List<Asteroid> asteroids) {
        List<Asteroid> sortedAsteroids = new ArrayList<>(asteroids);
        sortedAsteroids.sort(Comparator.comparing((Asteroid asteroid) ->
                Double.parseDouble(asteroid.getCloseApproachData().get(0).getMissDistance().getKilometers())));
        return sortedAsteroids;
    }

    private Asteroid getLargestAsteroidFromList(List<Asteroid> asteroids) {
        return asteroids.stream().max(Comparator.comparing(asteroid ->
                        (asteroid.getEstimatedDiameter().getMeters().getAverage())))
                .orElseThrow(() -> new RuntimeException("No asteroids found"));
    }

    private String getStartDate(String year) {
        return year + "-01-01";
    }

    private String getEndDate(String year) {
        return year + "-01-07";
    }
}
