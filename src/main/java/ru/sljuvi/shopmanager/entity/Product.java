package ru.sljuvi.shopmanager.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Товар.
 */
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Product {

  /**
   * Идентификатор.
   */
  @EqualsAndHashCode.Include
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  /**
   * Название.
   */
  @NotNull
  private String name;

  /**
   * Цена.
   */
  @NotNull
  private Double price;

  /**
   * Количество в магазине.
   */
  @NotNull
  @Min(0)
  private Integer count;

  /**
   * Магазин.
   */
  @NotNull
  @ManyToOne
  @JoinColumn(name = "shop_id")
  private Shop shop;
}
