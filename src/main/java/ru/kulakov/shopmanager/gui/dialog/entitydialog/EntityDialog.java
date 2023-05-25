package ru.kulakov.shopmanager.gui.dialog.entitydialog;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.hibernate.exception.ConstraintViolationException;
import ru.kulakov.shopmanager.exception.InvalidFieldException;
import ru.kulakov.shopmanager.gui.dialog.CommonDialog;

/**
 * Класс, представляющий диалог для добавления новой или изменения существующей сущности.
 *
 * @param <T> класс сущности
 */
public abstract class EntityDialog<T> extends CommonDialog {

  /**
   * Указатель на метод сохранения сущности.
   */
  private final SaveFunction<T> saveFunction;
  /**
   * Указатель на метод обновления таблицы сущностей.
   */
  private final RefreshGUIFunction refreshGUIFunction;
  /**
   * Сущность.
   */
  private T entity;

  public EntityDialog(JFrame parent, SaveFunction<T> saveFunction,
      RefreshGUIFunction refreshGUIFunction, T entity) {
    super(parent);
    this.saveFunction = saveFunction;
    this.refreshGUIFunction = refreshGUIFunction;
    this.entity = entity;
  }

  /**
   * Инициализирует окно диалога и добавляет обработчики событий для кнопок.
   *
   * @param contentPanel главная панель диалога
   * @param buttonOK     кнопка "ОК"
   * @param buttonCancel кнопка "Отмена"
   */
  protected void init(JPanel contentPanel, JButton buttonOK, JButton buttonCancel) {
    super.init(contentPanel, buttonOK);
    buttonCancel.addActionListener(e -> onCancel());
    if (entity != null) {
      fillFields(entity);
    } else {
      entity = createNewEntity();
    }
  }

  @Override
  protected void onOK() {
    try {
      saveEntity();
      onCancel();
    } catch (ConstraintViolationException | InvalidFieldException e) {
      showMessageDialog(e.getMessage());
      e.printStackTrace();
    }
  }

  /**
   * Добавляет в результат новую или изменяет существующую сущность.
   */
  private void saveEntity() throws InvalidFieldException {
    validateFields();
    fillEntityFromFields(entity);
    saveFunction.save(entity);
    refreshGUIFunction.refreshGUI();
  }

  /**
   * Отображает диалог с заданным сообщением.
   *
   * @param message сообщение
   */
  private void showMessageDialog(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  /**
   * Заполняет поля диалога значениями из сущности.
   *
   * @param entity сущность
   */
  protected abstract void fillFields(T entity);

  /**
   * Проверяет корректность заполнения полей.
   *
   * @throws InvalidFieldException если есть некорректные поля
   */
  protected abstract void validateFields() throws InvalidFieldException;

  /**
   * Возвращает новый экземпляр класса сущности.
   *
   * @return новый экземпляр класса сущности
   */
  protected abstract T createNewEntity();

  /**
   * Заполняет поля сущности значениями из полей диалога.
   *
   * @param entity сущность
   */
  protected abstract void fillEntityFromFields(T entity);
}
