package ru.sljuvi.shopmanager.service;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sljuvi.shopmanager.entity.Salesman;
import ru.sljuvi.shopmanager.entity.Shop;
import ru.sljuvi.shopmanager.repository.SalesmanRepository;

/**
 * Сервис для продавцов.
 */
@Service
@Transactional
public class SalesmanService extends AbstractService<Salesman, SalesmanRepository> {

  @Autowired
  public SalesmanService(SalesmanRepository repository) {
    super(repository);
  }

  /**
   * Возвращает список продавцов магазина.
   *
   * @param shop магазин
   * @return список продавцов магазина
   */
  public List<Salesman> getByShop(Shop shop) {
    return shop != null ? repository.getAllByShopId(shop.getId()) : Collections.emptyList();
  }

  /**
   * Удаляет всех продавцов магазина.
   *
   * @param shop магазин
   */
  public void removeByShop(Shop shop) {
    if (shop != null) {
      repository.deleteAllByShopId(shop.getId());
    }
  }
}
