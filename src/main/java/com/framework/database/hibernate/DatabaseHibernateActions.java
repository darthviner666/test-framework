package com.framework.database.hibernate;

import com.framework.api.pojo.users.create.rq.CreateUserPojoRq;
import com.framework.utils.dataGenerators.createUser.CreateUserGenerator;
import com.framework.utils.logger.TestLogger;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

@Slf4j
public class DatabaseHibernateActions {
    private static final TestLogger logger = new TestLogger(DatabaseHibernateActions.class);

    @Step("Убедиться что таблица users содержит {usersCount} пользователей")
    public static void ensureUsersExist(int usersCount) {
        try (Session session = DatabaseHibernateHikariConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                Long userCount = session.createQuery("select count(*) from CreateUserPojoRq", Long.class)
                        .getSingleResult();

                logger.info("Current users in database: {}", userCount);

                List<CreateUserPojoRq> existingUsers = getUsersList();

                if (userCount < usersCount) {
                    int usersToGenerate = usersCount - userCount.intValue();
                    logger.info("Generating {} new users", usersToGenerate);

                    List<CreateUserPojoRq> users = CreateUserGenerator.generateUniqueUsers(usersToGenerate, existingUsers);
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

    @Step("Получить список пользователей из таблицы users")
    public static List<CreateUserPojoRq> getUsersList() {
        try (Session session = DatabaseHibernateHikariConfig.getSessionFactory().openSession()) {
            List<CreateUserPojoRq> users = session.createQuery("from CreateUserPojoRq", CreateUserPojoRq.class).list();
            logger.info("Users in database: {}", users);
            return users;
        }
    }

    @Step("Создать пользователя в таблице users")
    public static void createUser(CreateUserPojoRq  user) {
        try (Session session = DatabaseHibernateHikariConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(user);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        }
    }

    @Step("Очистить таблицу users")
    public static void clearUsersTable() {
        try (Session session = DatabaseHibernateHikariConfig.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.createQuery("delete from CreateUserPojoRq").executeUpdate();
                transaction.commit();
                logger.info("Таблица create_users очищена");
            } catch (Exception e) {
                transaction.rollback();
                logger.error("Не удалось очистить таблицу create_users: {}", e.getMessage());
                throw e;
            }
        }
    }

    @Step("Получить количество пользователей в таблице users")
    public static int getUsersCount() {
        try (Session session = DatabaseHibernateHikariConfig.getSessionFactory().openSession()) {
            return session.createQuery("select count(*) from CreateUserPojoRq", Long.class)
                    .getSingleResult()
                    .intValue();
        }
    }
}
