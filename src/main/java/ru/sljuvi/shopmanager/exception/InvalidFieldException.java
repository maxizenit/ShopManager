package ru.sljuvi.shopmanager.exception;

import java.util.List;

/**
 * Сигнализирует о том, что поле сущности заполнено некорректно.
 */
public class InvalidFieldException extends Exception {

  /**
   * Первая строка сообщения об ошибке.
   */
  private static final String MESSAGE = "Данные в следующие поля введены некорректно:\n";

  public InvalidFieldException(List<String> invalidFields) {
    super(createMessage(invalidFields));
  }

  /**
   * Создаёт сообщение об ошибке, вставляя переданные названия полей.
   *
   * @param invalidFields названия полей, в которые были ошибочно введены данные
   * @return сообщение об ошибке
   */
  private static String createMessage(List<String> invalidFields) {
    StringBuilder sb = new StringBuilder();
    sb.append(MESSAGE);
    invalidFields.forEach(f -> sb.append(String.format("%s%n", f)));
    return sb.toString();
  }
}
