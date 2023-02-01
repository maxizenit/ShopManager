package ru.sljuvi.shopmanager.gui.entitytable.filter;

/**
 * Интерфейс фильтра сущностей для {@code EntityTable}.
 *
 * @param <T> класс сущности
 */
public interface EntityTableFilter<T> {

  /**
   * Возвращает признак фильтрации сущности.
   *
   * @param t сущность
   * @return {@code true}, если сущность удовлетворяет фильтру
   */
  boolean isFiltered(T t);

  /**
   * Возвращает признак пустоты фильтра.
   *
   * @return {@code true}, если все поля фильтра пустые
   */
  boolean isEmpty();
}
