package io.abner.vertx;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

// import io.abner.vertx.jooq.tables.pojos.Declaracao;
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
    DocumentService documentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("documents")
    public Single<String> getDocuments() {
        return documentService.getAll()
        .map(list -> {
            System.out.println(list.toString());
            return Json.encode(list);
        })
        .doOnError(e -> e.printStackTrace());
    }

    @GET
    @Path("todos")
    public JsonObject getTodos() {
        System.out.println("DECLARACAO SERVICE IS NULL??? " +  this.documentService == null);
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