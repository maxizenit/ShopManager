package ru.sljuvi.shopmanager.gui.entitytable.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;
import ru.sljuvi.shopmanager.entity.Product;

/**
 * Фильтр для таблицы товаров.
 */
@Getter
@Setter
public class ProductEntityTableFilter implements EntityTableFilter<Product> {

  /**
   * Название.
   */
  private String name;

  /**
   * Минимальная цена.
   */
  private Double priceFrom;

  /**
   * Максимальная цена.
   */
  private Double priceTo;

  @Override
  public boolean isFiltered(Product product) {
    if (StringUtils.hasText(name) && !name.equals(product.getName())) {
      return false;
    }

    if (priceFrom != null && priceFrom.compareTo(product.getPrice()) > 0) {
      return false;
    }

    return priceTo == null || priceTo.compareTo(product.getPrice()) >= 0;
  }

  @Override
  public boolean isEmpty() {
    return !StringUtils.hasText(name) && priceFrom == null && priceTo == null;
  }
}
