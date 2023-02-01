package ru.sljuvi.shopmanager.util.report;

import lombok.Getter;
import lombok.Setter;

/**
 * Датабин для отчёта по товарам.
 */
@Getter
@Setter
public class ProductDataBean {

  /**
   * Название.
   */
  private String name;

  /**
   * Цена.
   */
  private String price;

  /**
   * Количество.
   */
  private String count;
}
