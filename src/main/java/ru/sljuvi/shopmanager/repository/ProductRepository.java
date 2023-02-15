package ru.sljuvi.shopmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sljuvi.shopmanager.entity.Product;

/**
 * Репозиторий для товаров.
 */
public interface ProductRepository extends JpaRepository<Product, Integer> {

}
