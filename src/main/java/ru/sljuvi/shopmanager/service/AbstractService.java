package ru.sljuvi.shopmanager.service;

import java.util.List;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Абстрактный класс с базовыми операциями для сущностей.
 *
 * @param <T> класс сущности
 */
public abstract class AbstractService<T, R extends JpaRepository<T, Integer>> {

  protected final R repository;

  public AbstractService(R repository) {
    this.repository = repository;
  }

  /**
   * Сохраняет сущность.
   *
   * @param entity сущность
   */
  public void save(@NonNull T entity) {
    repository.save(entity);
  }

  /**
   * Удаляет сущность.
   *
   * @param entity сущность
   */
  public void remove(@NonNull T entity) {
    repository.delete(entity);
  }

  /**
   * Сохраняет список сущностей.
   *
   * @param entities список сущностей
   */
  public void saveAll(List<T> entities) {
    repository.saveAll(entities);
  }

  /**
   * Возвращает список всех сущностей.
   *
   * @return список всех сущностей
   */
  public List<T> getAll() {
    return repository.findAll();
  }

  /**
   * Удаляет все сущности.
   */
  public void removeAll() {
    repository.deleteAll();
  }
}
