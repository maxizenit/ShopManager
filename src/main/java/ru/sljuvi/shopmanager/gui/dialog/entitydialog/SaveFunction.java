package ru.sljuvi.shopmanager.gui.dialog.entitydialog;

/**
 * Функциональный интерфейс, предоставляющий метод сохранения сущности.
 *
 * @param <T> класс сущности
 */
@FunctionalInterface
public interface SaveFunction<T> {

  /**
   * Сохраняет сущность.
   *
   * @param entity сущность
   */
  void save(T entity);
}
