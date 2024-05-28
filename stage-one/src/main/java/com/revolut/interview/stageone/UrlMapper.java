package com.revolut.interview.stageone;

public class UrlMapper {

  public String map(String record) {
    return "localhost:8000/%s".formatted(record);
  }
}
