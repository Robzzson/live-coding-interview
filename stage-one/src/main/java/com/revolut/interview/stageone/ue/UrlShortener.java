package com.revolut.interview.stageone.ue;

import jakarta.inject.Inject;
import java.math.BigInteger;
import java.util.Optional;

public class UrlShortener {

  final UrlRepository repository;
  final UrlEncoder encoder;
  final SequenceProvider provider;

  @Inject
  public UrlShortener(UrlRepository repository, UrlEncoder encoder, SequenceProvider provider) {
    this.repository = repository;
    this.encoder = encoder;
    this.provider = provider;
  }

  /*
   * https://revolut.com -> abc
   */
  public String shortenUrl(String url) {
    BigInteger next = provider.getNext();
    repository.save(new UrlEntity(next, url));
    return encoder.encode(next);
  }

  /*
   * abc -> https://revolut.com
   */
  public Optional<String> retrieveOriginalUrl(String identifier) {
    BigInteger decoded = encoder.decode(identifier);
    return repository.getById(decoded).map(UrlEntity::getValue);
  }
}
