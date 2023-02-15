package ru.sljuvi.shopmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sljuvi.shopmanager.entity.Product;
import ru.sljuvi.shopmanager.repository.ProductRepository;

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
