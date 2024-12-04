package com.authorization_server.util.seq_gen;

import jakarta.persistence.Query;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

public class CustomSequenceGenerator implements IdentifierGenerator {

    public static final String INCREMENT_SIZE = "increment_size";
    public static final String ENTITY = "entity";
    private int incrementSize;
    private Class<?> entityClass;


    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        try {
            Query query = session.createQuery(String.format("select count(u) from %s u", entityClass.getSimpleName()),
                    entityClass);

            Long l = (Long) query.getSingleResult();
            return l.intValue() + incrementSize;
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
    }

    @Override
    public void configure(Type type, Properties parameters, ServiceRegistry serviceRegistry) {
        IdentifierGenerator.super.configure(type, parameters, serviceRegistry);
        incrementSize = Integer.parseInt(parameters.getProperty(INCREMENT_SIZE, "1"));
        String className = parameters.getProperty(ENTITY);
        try {
            entityClass = Class.forName(className);
        } catch (ClassNotFoundException ignored) {
        }

    }
}
