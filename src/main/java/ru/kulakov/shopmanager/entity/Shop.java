package ru.kulakov.shopmanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Магазин.
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Shop {

  /**
   * Идентификатор.
   */
  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * Адрес.
   */
  @NotNull
  private String address;

  /**
   * Специализация.
   */
  @NotNull
  private String specialization;

  /**
   * Полное имя директора.
   */
  @NotNull
  private String directorFullName;

  @Override
  public String toString() {
    return String.format("%d (%s)", id, specialization);
  }
}
