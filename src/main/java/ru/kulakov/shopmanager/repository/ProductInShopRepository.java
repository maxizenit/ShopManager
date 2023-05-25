package ru.kulakov.shopmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kulakov.shopmanager.entity.ProductInShop;

/**
 * Репозиторий для товаров магазина.
 */
@Repository
public interface ProductInShopRepository extends JpaRepository<ProductInShop, Integer> {

  List<ProductInShop> getAllByShopId(Integer shopId);

  ProductInShop getByShopIdAndProductId(Integer shopId, Integer productId);
}
