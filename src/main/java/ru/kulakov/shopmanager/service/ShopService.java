package ru.kulakov.shopmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kulakov.shopmanager.repository.ShopRepository;
import ru.kulakov.shopmanager.entity.Shop;

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
