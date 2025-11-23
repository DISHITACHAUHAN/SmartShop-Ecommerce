package com.ecommerce.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * Data initializer to run SQL scripts on application startup
 * Executes schema.sql and data.sql for database setup
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private DataSource dataSource;

    @Override
    public void run(String... args) {
        try {
            logger.info("Initializing database schema and data...");
            
            // Create ResourceDatabasePopulator
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
            populator.setContinueOnError(false);
            populator.setIgnoreFailedDrops(true);
            
            // Add schema script
            populator.addScript(new ClassPathResource("schema.sql"));
            logger.info("Schema script loaded");
            
            // Add data script
            populator.addScript(new ClassPathResource("data.sql"));
            logger.info("Data script loaded");
            
            // Execute scripts
            populator.execute(dataSource);
            logger.info("Database initialized successfully");
            
        } catch (Exception e) {
            logger.error("Error initializing database", e);
            // Don't throw exception to allow application to start
            // This allows manual database setup if needed
        }
    }
}
