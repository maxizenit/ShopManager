package ru.kulakov.shopmanager.gui.dialog.entitydialog;

/**
 * Функциональный интерфейс, предоставляющий метод обновления таблицы сущностей.
 */
@FunctionalInterface
public interface RefreshGUIFunction {

  /**
   * Обновляет таблицу сущностей.
   */
  void refreshGUI();
}
