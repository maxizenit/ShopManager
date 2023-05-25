package ru.kulakov.shopmanager.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.kulakov.shopmanager.entity.Salesman;

/**
 * Репозиторий для продавцов.
 */
public interface SalesmanRepository extends JpaRepository<Salesman, Integer> {

  List<Salesman> getAllByShopId(Integer shopId);

  void deleteAllByShopId(Integer shopId);
}
