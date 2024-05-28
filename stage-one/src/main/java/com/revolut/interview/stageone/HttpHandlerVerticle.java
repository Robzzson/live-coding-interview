package com.revolut.interview.stageone;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.MultiMap;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class HttpHandlerVerticle extends AbstractVerticle {

  private static final Logger log = LoggerFactory.getLogger(HttpHandlerVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) {
    Router router = Router.router(vertx);

    // Mount the handler for all incoming requests at every path and HTTP method
    router.route().handler(context -> {
      // Get the address of the request
      String address = context.request().connection().remoteAddress().toString();
      // Get the query parameter "name"
      MultiMap queryParams = context.queryParams();
      String name = queryParams.contains("name") ? queryParams.get("name") : "unknown";
      // Write a json response
      context.json(
          new JsonObject()
              .put("name", name)
              .put("address", address)
              .put("message", "Hello " + name + " connected from " + address)
      );
    });

    vertx.createHttpServer()
        .requestHandler(router)
        .listen(8888)
        .onSuccess(server -> {
          System.out.println("Started HTTP server at port %s".formatted(server.actualPort()));
          log.info("Started HTTP server at port %s".formatted(server.actualPort()));
          startPromise.complete();
        });
  }
}
