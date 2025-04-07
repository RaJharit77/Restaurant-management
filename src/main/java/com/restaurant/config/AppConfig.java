package com.restaurant.config;

import com.restaurant.db.DataBaseSource;
import com.restaurant.dao.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public DataBaseSource dataSource() {
        return new DataBaseSource();
    }

    @Bean
    public DishDAO dishDAO() {
        return new DishDAOImpl(dataSource());
    }

    @Bean
    public IngredientDAO ingredientDAO() {
        return new IngredientDAOImpl(dataSource());
    }

    @Bean
    public OrderDAO orderDAO() {
        return new OrderDAOImpl(dataSource());
    }

    @Bean
    public DishOrderDAO dishOrderDAO() {
        return new DishOrderDAOImpl(dataSource());
    }

    @Bean
    public OrderStatusDAO orderStatusDAO() {
        return new OrderStatusDAOImpl(dataSource());
    }

    @Bean
    public DishOrderStatusDAO dishOrderStatusDAO() {
        return new DishOrderStatusDAOImpl(dataSource());
    }

    @Bean
    public StockMovementDAO stockMovementDAO() {
        return new StockMovementImpl(dataSource());
    }
}