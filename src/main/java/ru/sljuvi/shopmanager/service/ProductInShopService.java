package ru.sljuvi.shopmanager.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sljuvi.shopmanager.entity.Product;
import ru.sljuvi.shopmanager.entity.ProductInShop;
import ru.sljuvi.shopmanager.entity.Shop;
import ru.sljuvi.shopmanager.repository.ProductInShopRepository;

/**
 * Сервис для товаров в магазине.
 */
@Service
public class ProductInShopService extends AbstractService<ProductInShop, ProductInShopRepository> {

  @Autowired
  public ProductInShopService(ProductInShopRepository repository) {
    super(repository);
  }

  /**
   * Возвращает товары в магазине по магазину.
   *
   * @param shop магазин
   * @return список товаров в магазине
   */
  public List<ProductInShop> getByShop(Shop shop) {
    return repository.getAllByShopId(shop.getId());
  }

  /**
   * Увеличивает количество товара в магазине на 1.
   *
   * @param productInShop товар в магазине
   */
  public void increment(ProductInShop productInShop) {
    productInShop.setCount(productInShop.getCount() + 1);
    save(productInShop);
  }

  /**
   * Уменьшает количество товара в магазине на 1. Если Количество становится равным 0, товар
   * удаляется.
   *
   * @param productInShop товар в магазине
   */
  public void decrement(ProductInShop productInShop) {
    if (productInShop.getCount() <= 1) {
      remove(productInShop);
    } else {
      productInShop.setCount(productInShop.getCount() - 1);
      save(productInShop);
    }
  }

  /**
   * Добавляет товар в магазин.
   *
   * @param shop    магазин
   * @param product товар
   */
  public void addProductInShop(Shop shop, Product product) {
    ProductInShop productInShop = repository.getByShopIdAndProductId(shop.getId(), product.getId());

    if (productInShop == null) {
      productInShop = new ProductInShop();

      productInShop.setShop(shop);
      productInShop.setProduct(product);
      productInShop.setCount(1);

      save(productInShop);
    } else {
      increment(productInShop);
    }
  }
}
