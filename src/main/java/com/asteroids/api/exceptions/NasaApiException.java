package com.asteroids.api.exceptions;

public class NasaApiException extends RuntimeException {

  public NasaApiException(String message) {
    super(message);
  }

  public NasaApiException(String message, Exception cause) {
    super(message, cause);
  }
}
