package io.abner.vertx;

import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import io.abner.vertx.jooq.tables.daos.DeclaracaoDao;
// import io.abner.vertx.jooq.tables.daos.DeclaracaoDao;
import io.reactiverse.pgclient.PgPoolOptions;
import io.reactiverse.reactivex.pgclient.PgPool;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.sql.SQLClient;
import net.redpipe.engine.core.AppGlobals;
import net.redpipe.engine.core.Server;

public class Main extends AbstractVerticle {

    public static void main(String[] args) {
        Server redpipeServer = new Server() {
            @Override
            protected SQLClient createDbClient(JsonObject config) {
                System.out.println("CONFIG ---- " + config.encode());
                JsonObject myConfig = new JsonObject();
                if(config.containsKey("db_host"))
                    myConfig.put("host", config.getString("db_host"));
                if(config.containsKey("db_port"))
                    myConfig.put("port", config.getInteger("db_port"));
                if(config.containsKey("db_user"))
                    myConfig.put("user", config.getString("db_user"));
                if(config.containsKey("db_pass"))
                    myConfig.put("password", config.getString("db_pass"));
                if(config.containsKey("db_name"))
                    myConfig.put("database", config.getString("db_name"));
                myConfig.put("maxSize", config.getInteger("db_max_pool_size", 30));

                System.out.println("MYCONFIG ----- " + myConfig.encode());
                
                Vertx vertx = AppGlobals.get().getVertx();
                // Pool options
                PgPoolOptions options = new PgPoolOptions(myConfig);

                PgPool pgPool = PgPool.pool(vertx, options);

                Configuration configuration = new DefaultConfiguration().set(SQLDialect.POSTGRES);
                
                DeclaracaoDao declaracaoDAO = new DeclaracaoDao(configuration, pgPool);

                DSLContext context  = DSL.using(configuration);

                AppGlobals.get().setGlobal(DSLContext.class, context);

                AppGlobals.get().setGlobal(DeclaracaoDao.class, declaracaoDAO);

                return null;
            }
        };
        
        redpipeServer.start(RestController.class).subscribe(() -> {
            System.out.println("Rest Server started!");
        }, e -> {
            e.printStackTrace();
            System.out.println("Failed to start the server");
        });
    }
    public void start(Future<Void> future) {
        HttpServer httpSrv = vertx.createHttpServer();

        httpSrv.requestHandler(req -> {
            req.response().putHeader("content-type", "application/json");
            req.response().end("{\"result\": \"OK\"}");
        }).listen(18080, f -> {
            if (f.result() != null) {
                System.out.println("Server just started");
                future.complete();
            } else {
                future.fail(f.cause());
            }
        });
    }
}