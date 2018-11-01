package io.abner.vertx;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import io.abner.vertx.jooq.tables.pojos.Declaracao;
import io.reactivex.Single;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@ApplicationScoped
@Path("/api")
public class RestController {

    @Inject
    MeuBean data;

    @Inject
    DeclaracaoService declaracaoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("declaracoes")
    public Single<String> obterDeclaracoes() {
        return declaracaoService.getDeclaracoes()
        .map(list -> Json.encode(list))
        .doOnError(e -> e.printStackTrace());
    }

    @GET
    @Path("todos")
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