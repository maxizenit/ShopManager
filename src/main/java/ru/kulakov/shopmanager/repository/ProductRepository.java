package ru.kulakov.shopmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kulakov.shopmanager.entity.Product;

/**
 * Репозиторий для товаров.
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
