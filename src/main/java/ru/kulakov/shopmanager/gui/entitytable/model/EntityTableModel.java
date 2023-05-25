package ru.kulakov.shopmanager.gui.entitytable.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.table.DefaultTableModel;
import ru.kulakov.shopmanager.gui.entitytable.filter.EntityTableFilter;

public abstract class EntityTableModel<T> extends DefaultTableModel {

  protected List<T> entities;

  private List<T> oldEntities;

  public EntityTableModel(String[] columnNames) {
    super(null, columnNames);
  }

  @Override
  public boolean isCellEditable(int row, int column) {
    return false;
  }

  private void updateDataVector() {
    dataVector.clear();
    entities.forEach(e -> {
      Vector<Object> vector = new Vector<>();
      fillVectorFromEntity(vector, e);
      dataVector.add(vector);
    });
  }

  protected abstract void fillVectorFromEntity(Vector<Object> vector, T entity);

  public T getEntityByRow(int index) {
    try {
      return entities.get(index);
    } catch (NullPointerException | IndexOutOfBoundsException e) {
      return null;
    }
  }

  public void updateEntities(List<T> entities) {
    this.entities = entities;
    oldEntities = null;
    updateDataVector();
  }

  public void clear() {
    entities = null;
    oldEntities = null;
    dataVector.clear();
  }

  public void filter(EntityTableFilter<T> filter) {
    clearFilter();

    List<T> newEntities = new CopyOnWriteArrayList<>();

    entities.parallelStream().forEach(e -> {
      if (filter.isFiltered(e)) {
        newEntities.add(e);
      }
    });

    oldEntities = new ArrayList<>(entities);
    entities = newEntities;
    updateDataVector();
  }

  public void clearFilter() {
    if (oldEntities != null) {
      updateEntities(oldEntities);
    }
  }
}
