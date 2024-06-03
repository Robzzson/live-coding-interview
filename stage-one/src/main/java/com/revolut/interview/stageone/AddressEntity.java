package com.revolut.interview.stageone;

import java.util.UUID;

public class AddressEntity {

  public AddressEntity(UUID id, String address) {
    this.id = id;
    this.address = address;
  }

  private final UUID id;
  private final String address;

  public UUID getId() {
    return id;
  }

  public String getAddress() {
    return address;
  }
}
