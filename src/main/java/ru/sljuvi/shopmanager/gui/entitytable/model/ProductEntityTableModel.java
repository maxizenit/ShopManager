package ru.sljuvi.shopmanager.gui.entitytable.model;

import java.util.Vector;
import ru.sljuvi.shopmanager.entity.Product;

public class ProductEntityTableModel extends EntityTableModel<Product> {

  private static final String[] COLUMN_NAMES = {"Название", "Цена", "Количество"};

  public ProductEntityTableModel() {
    super(COLUMN_NAMES);
  }

  @Override
  protected void fillVectorFromEntity(Vector<Object> vector, Product entity) {
    vector.add(entity.getName());
    vector.add(entity.getPrice());
    vector.add(entity.getCount());
  }
}
