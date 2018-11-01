package io.abner.vertx;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;

import org.jooq.DSLContext;
import org.jooq.Record3;

import io.abner.vertx.jooq.tables.Declaracao;
import io.abner.vertx.jooq.tables.daos.DeclaracaoDao;
import io.reactivex.Single;
import net.redpipe.engine.core.AppGlobals;


@ApplicationScoped
public class DeclaracaoService {
    
    private DeclaracaoDao dao;

    private DSLContext context;

    public DeclaracaoService() {
        context = AppGlobals.get().getGlobal(DSLContext.class);
        dao = AppGlobals.get().getGlobal(DeclaracaoDao.class);
    }

    public Single<Optional<io.abner.vertx.jooq.tables.pojos.Declaracao>> getDeclaracaoPor(String cpf, int exercicio, boolean transmitida) {
        return dao.findOneById(getIdFor(cpf, exercicio, transmitida));
    }
    
    public Single<List<io.abner.vertx.jooq.tables.pojos.Declaracao>> getDeclaracoes() {
        return dao.findAll();
    }

    private Record3<String, Short, Boolean> getIdFor(String cpf, int exercicio, boolean retificadora) {
        return context.newRecord(io.abner.vertx.jooq.tables.Declaracao.DECLARACAO.CPF, io.abner.vertx.jooq.tables.Declaracao.DECLARACAO.EXERCICIO, Declaracao.DECLARACAO.TRANSMITIDA )
            .value1(cpf)
            .value2(new Integer(exercicio).shortValue())
            .value3(retificadora);
    }
}