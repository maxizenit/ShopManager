package ru.sljuvi.shopmanager.service;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sljuvi.shopmanager.entity.Product;
import ru.sljuvi.shopmanager.entity.Shop;
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

  /**
   * Возвращает список товаров в магазине.
   *
   * @param shop магазин
   * @return список товаров в магазине
   */
  public List<Product> getByShop(Shop shop) {
    return shop != null ? repository.getAllByShopId(shop.getId()) : Collections.emptyList();
  }
}
