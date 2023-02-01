package ru.sljuvi.shopmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.sljuvi.shopmanager.entity.Shop;
import ru.sljuvi.shopmanager.repository.ShopRepository;

/**
 * Сервис для магазинов.
 */
@Service
public class ShopService extends AbstractService<Shop, ShopRepository> {

  @Autowired
  public ShopService(ShopRepository repository) {
    super(repository);
  }

  /**
   * Возвращает магазин с заданным идентификатором.
   *
   * @param id идентификатор
   * @return магазин с заданным идентификатором
   */
  public Shop getById(Integer id) {
    return repository.getShopById(id);
  }
}