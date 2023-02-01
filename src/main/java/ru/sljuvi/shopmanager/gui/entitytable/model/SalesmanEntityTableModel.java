package ru.sljuvi.shopmanager.gui.entitytable.model;

import java.util.Vector;
import ru.sljuvi.shopmanager.entity.Salesman;

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
