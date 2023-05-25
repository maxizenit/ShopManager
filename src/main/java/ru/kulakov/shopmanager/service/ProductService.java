package ru.kulakov.shopmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kulakov.shopmanager.repository.ProductRepository;
import ru.kulakov.shopmanager.entity.Product;

/**
 * Сервис для товаров.
 */
@Service
public class ProductService extends AbstractService<Product, ProductRepository> {

  @Autowired
  public ProductService(ProductRepository repository) {
    super(repository);
  }
}
