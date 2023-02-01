package ru.sljuvi.shopmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sljuvi.shopmanager.entity.Shop;

/**
 * Репозиторий для магазинов.
 */
public interface ShopRepository extends JpaRepository<Shop, Integer> {

  Shop getShopById(Integer id);
}
