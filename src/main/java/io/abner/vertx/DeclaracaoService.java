package io.abner.vertx;

import java.util.List;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.jooq.DSLContext;
import org.jooq.Record2;

import io.abner.vertx.jooq.tables.daos.DeclaracaoDao;
import io.abner.vertx.jooq.tables.pojos.Declaracao;
import io.abner.vertx.jooq.tables.records.DeclaracaoRecord;
import io.reactivex.Single;
import net.redpipe.engine.core.AppGlobals;


@ApplicationScoped
public class DeclaracaoService {
    
    // @Inject
    private DeclaracaoDao dao;

    // @Inject
    private DSLContext context;

    public DeclaracaoService() {
        context = AppGlobals.get().getGlobal(DSLContext.class);
        dao = AppGlobals.get().getGlobal(DeclaracaoDao.class);
    }

    public Single<Optional<Declaracao>> getDeclaracaoPor(String cpf, int exercicio) {
        return dao.findOneById(getIdFor(cpf, exercicio));
    }
    
    public Single<List<Declaracao>> getDeclaracoes() {
        return dao.findAll();
    }

    private Record2<String, Short> getIdFor(String cpf, int exercicio) {
        return context.newRecord(io.abner.vertx.jooq.tables.Declaracao.DECLARACAO.CPF, io.abner.vertx.jooq.tables.Declaracao.DECLARACAO.EXERCICIO)
            .value1(cpf)
            .value2(new Integer(exercicio).shortValue());
    }
}