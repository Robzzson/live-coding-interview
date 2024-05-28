package com.revolut.interview.stageone;

import jakarta.inject.Inject;
import java.math.BigInteger;
import java.net.URL;
import org.apache.commons.lang3.StringUtils;

public class UrlShortenerService {

  final UrlRepository repository;
  final EncoderService service;
  final SequenceProvider sequenceProvider;
  final UrlMapper mapper;

  @Inject
  public UrlShortenerService(UrlRepository repository, EncoderService service, SequenceProvider sequenceProvider, UrlMapper mapper) {
    this.repository = repository;
    this.service = service;
    this.sequenceProvider = sequenceProvider;
    this.mapper = mapper;
  }

  public String shortenUrl(URL url) {
    BigInteger next = sequenceProvider.getNext();
    String encodedBigInteger = service.encode(next);
    repository.save(new StringRecord(next, url.toString()));
    return mapper.map(encodedBigInteger);
  }

  public String retrieve(String s) {
    String symbol = StringUtils.substring(s, s.lastIndexOf("/") + 1, s.length());
    BigInteger decoded = service.decode(symbol);
    return repository.getById(decoded);
  }
}
