package ru.kulakov.shopmanager.gui.entitytable.model;

import java.util.Vector;

import ru.kulakov.shopmanager.entity.Salesman;

public class SalesmanEntityTableModel extends EntityTableModel<Salesman> {

  private static final String[] COLUMN_NAMES = {"ФИО"};

  public SalesmanEntityTableModel() {
    super(COLUMN_NAMES);
  }

  @Override
  protected void fillVectorFromEntity(Vector<Object> vector, Salesman entity) {
    vector.add(entity.getFullName());
  }
}
