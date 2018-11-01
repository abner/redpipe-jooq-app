package io.abner.vertx;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
@Path("/todos")
public class RestController {

    @Inject
    MeuBean data;

    @Inject
    DeclaracaoService declaracaoService;

    @GET
    public JsonObject getTodos() {
        System.out.println("DECLARACAO SERVICE IS NULL??? " +  this.declaracaoService == null);
        return new JsonObject()
            .put(
                "data",
                new JsonArray()
                    .add(
                        new JsonObject()
                            .put("id",1)
                            .put("description", "Todo 1")
                            .put("date", data == null ? "null" : data.getData())
                    )
            );
    }
}