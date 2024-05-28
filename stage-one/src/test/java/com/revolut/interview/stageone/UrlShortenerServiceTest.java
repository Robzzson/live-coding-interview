package com.revolut.interview.stageone;

import static org.assertj.core.api.Assertions.assertThat;

import com.google.inject.Guice;
import com.google.inject.Injector;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UrlShortenerServiceTest {

  @BeforeEach
  void setUp() {
  }

  @Test
  void testShortenUrl() throws MalformedURLException {
    Injector injector = Guice.createInjector(new BasicModule());
    UrlShortenerService urlShortenerService = injector.getInstance(UrlShortenerService.class);

    assertThat(urlShortenerService.shortenUrl(new URL("https://www.google.com")))
        .isEqualTo("localhost:8000/a");
    assertThat(urlShortenerService.shortenUrl(new URL("https://www.amazon.com")))
        .isEqualTo("localhost:8000/b");
    assertThat(urlShortenerService.retrieve("localhost:8000/a"))
        .isEqualTo("https://www.google.com");
    assertThat(urlShortenerService.retrieve("localhost:8000/b"))
        .isEqualTo("https://www.amazon.com");
    assertThat(urlShortenerService.retrieve("localhost:8000/aaab"))
        .isEqualTo("https://www.amazon.com");
  }
}
