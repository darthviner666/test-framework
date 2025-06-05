package com.framework.database;

import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.utils.dataGenerators.CreateUserGenerator;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DatabaseActions {
    public static final int REQUIRED_USERS_COUNT = 10;

    static public void ensureUsersExist() {
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            Long userCount = session.createQuery("select count(*) from User", Long.class)
                    .getSingleResult();

            if (userCount < REQUIRED_USERS_COUNT) {
                int usersToGenerate = REQUIRED_USERS_COUNT - userCount.intValue();
                List<CreateUserPojoRq> newUsers = CreateUserGenerator.generateUsers(usersToGenerate);

                for (CreateUserPojoRq user : newUsers) {
                    session.persist(user);
                }
            }

            tx.commit();
        }
    }
}
