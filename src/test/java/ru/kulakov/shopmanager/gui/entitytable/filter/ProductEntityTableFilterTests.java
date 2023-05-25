package ru.kulakov.shopmanager.gui.entitytable.filter;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import ru.kulakov.shopmanager.entity.Product;

public class ProductEntityTableFilterTests {

  @Test
  public void filterTest() {
    Product product = new Product();

    product.setName("RTX");
    product.setPrice(300.0);

    ProductEntityTableFilter filter = new ProductEntityTableFilter();

    filter.setName("RTX");
    assertTrue(filter.isFiltered(product));

    filter.setPriceTo(250.0);
    assertFalse(filter.isFiltered(product));

    filter.setName(null);
    filter.setPriceTo(null);
    filter.setPriceFrom(350.0);
    assertFalse(filter.isFiltered(product));

    filter.setPriceFrom(299.0);
    filter.setPriceTo(301.0);
    assertTrue(filter.isFiltered(product));

    filter.setName("RTX");
    filter.setPriceFrom(300.0);
    filter.setPriceTo(300.0);
    assertTrue(filter.isFiltered(product));
  }
}
