package ru.kulakov.shopmanager.gui.entitytable.model;

import java.util.Vector;

import ru.kulakov.shopmanager.entity.ProductInShop;

public class ProductInShopEntityTableModel extends EntityTableModel<ProductInShop> {

  private static final String[] COLUMN_NAMES = {"Название", "Цена", "Количество"};

  public ProductInShopEntityTableModel() {
    super(COLUMN_NAMES);
  }

  @Override
  protected void fillVectorFromEntity(Vector<Object> vector, ProductInShop entity) {
    vector.add(entity.getProduct().getName());
    vector.add(entity.getProduct().getPrice());
    vector.add(entity.getCount());
  }
}
