package com.framework.database;

import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.utils.dataGenerators.CreateUserGenerator;
import com.framework.utils.logger.TestLogger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class DatabaseActions {
    private static final TestLogger logger = new TestLogger(DatabaseActions.class);

    public static void ensureUsersExist(int usersCount) {
        try (Session session = DatabaseConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Long userCount = session.createQuery("select count(*) from CreateUserPojoRq", Long.class)
                        .getSingleResult();

                logger.info("Current users in database: {}", userCount);

                if (userCount < usersCount) {
                    int usersToGenerate = usersCount - userCount.intValue();
                    logger.info("Generating {} new users", usersToGenerate);

                    List<CreateUserPojoRq> users = CreateUserGenerator.generateUsers(usersToGenerate);
                    for (CreateUserPojoRq user : users) {
                        session.persist(user);
                    }
                }

                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }
}
