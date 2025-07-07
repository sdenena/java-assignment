package org.sd.configurations;

import org.hibernate.SessionFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.sd.entity.Account;
import org.sd.entity.AccountHistory;
import org.sd.entity.User;

import java.util.Properties;

public class HibernateUtil {
    private static final SessionFactory sessionFactory;
    private static final HikariDataSource dataSource;

    static {
        try {
            // 1. Create HikariCP DataSource
            dataSource = createHikariDataSource();

            // 2. Configure Hibernate
            Configuration configuration = new Configuration();
            configuration.setProperties(getHibernateProperties());

            // 3. Add entity classes
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(Account.class);
            configuration.addAnnotatedClass(AccountHistory.class);

            // 4. Register HikariCP DataSource manually
            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties())
                    .applySetting("hibernate.connection.datasource", dataSource)
                    .build();

            // 5. Build SessionFactory
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            System.out.println("✅ Hibernate SessionFactory created successfully with HikariCP");
        } catch (Exception e) {
            System.err.println("❌ Failed to create SessionFactory: " + e.getMessage());
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    private static HikariDataSource createHikariDataSource() {
        HikariConfig config = new HikariConfig();

        // PostgreSQL DB connection info
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/java_assignment_db");
        config.setUsername("postgres");
        config.setPassword("Sden$123456");
        config.setDriverClassName("org.postgresql.Driver");

        // HikariCP pool config
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.setLeakDetectionThreshold(60000);
        config.setPoolName("HikariCP-PostgreSQL");

        // Optional PostgreSQL optimizations
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");
        config.addDataSourceProperty("rewriteBatchedStatements", "true");
        config.addDataSourceProperty("ApplicationName", "HibernateHikariCPDemo");

        System.out.println("✅ HikariCP DataSource created successfully");
        return new HikariDataSource(config);
    }

    private static Properties getHibernateProperties() {
        Properties properties = new Properties();

        // No need to set connection.driver or URL, handled by injected DataSource

        // Hibernate Dialect
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        // Schema management (change to "update", "create", etc. if needed)
        properties.setProperty("hibernate.hbm2ddl.auto", "validate");

        // Logging
        properties.setProperty("hibernate.show_sql", "true");
        properties.setProperty("hibernate.format_sql", "true");
        properties.setProperty("hibernate.use_sql_comments", "true");

        // Performance
        properties.setProperty("hibernate.jdbc.batch_size", "25");
        properties.setProperty("hibernate.order_inserts", "true");
        properties.setProperty("hibernate.order_updates", "true");
        properties.setProperty("hibernate.jdbc.batch_versioned_data", "true");

        // Cache (optional)
        properties.setProperty("hibernate.cache.use_second_level_cache", "false");
        properties.setProperty("hibernate.cache.use_query_cache", "false");

        // Stats
        properties.setProperty("hibernate.generate_statistics", "true");

        // Session context
        properties.setProperty("hibernate.current_session_context_class", "thread");

        // Optional: explicitly disable autocommit
        properties.setProperty("hibernate.connection.autocommit", "false");

        return properties;
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }

    public static void printConnectionPoolStats() {
        if (dataSource != null) {
            System.out.println("=== HikariCP Pool Stats ===");
            System.out.println("Active connections: " + dataSource.getHikariPoolMXBean().getActiveConnections());
            System.out.println("Idle connections: " + dataSource.getHikariPoolMXBean().getIdleConnections());
            System.out.println("Total connections: " + dataSource.getHikariPoolMXBean().getTotalConnections());
            System.out.println("Threads awaiting connection: " + dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
    }

    public static void shutdown() {
        if (sessionFactory != null) sessionFactory.close();
        if (dataSource != null) dataSource.close();
        System.out.println("✅ SessionFactory and HikariCP DataSource closed");
    }
}
