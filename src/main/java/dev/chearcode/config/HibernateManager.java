package dev.chearcode.config;

import dev.chearcode.entity.Match;
import dev.chearcode.entity.Player;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.InputStream;
import java.util.Properties;


public class HibernateManager {
    private static SessionFactory sessionFactory = buildSessionFactory();

    private HibernateManager() {
    }

    public static void init(){}

    public static Session getSession() {
        return sessionFactory.getCurrentSession();
    }

    public static void closeSessionFactory() {
        sessionFactory.close();
    }

    private static SessionFactory buildSessionFactory() {

        DatabaseConsoleManager.runConsoleServer(8082);

        try {
            Properties properties = new Properties();
            try (InputStream is = Thread.currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream("hibernate.properties")) {
                if (is == null) {
                    throw new IllegalStateException("hibernate.properties not found in classpath");
                }
                properties.load(is);
            }

            StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .applySettings(properties)
                    .build();

            MetadataSources sources = new MetadataSources(registry)
                    .addAnnotatedClass(Player.class)
                    .addAnnotatedClass(Match.class);

            Metadata metadata = sources.getMetadataBuilder().build();
            return metadata.getSessionFactoryBuilder().build();

        } catch (Throwable e) {
            System.err.println("Initial SessionFactory creation failed: " + e);
            throw new ExceptionInInitializerError(e);
        }
    }
}
