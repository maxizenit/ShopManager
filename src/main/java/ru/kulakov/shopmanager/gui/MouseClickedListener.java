package ru.kulakov.shopmanager.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Обработчик события для нажатия кнопки мыши. Наследуется от {@code MouseListener} и переопределяет
 * как пустые все его методы, кроме {@code mouseClicked}, сокращая создание класса на основе
 * интерфейса. Все наследники данного интерфейса должны переопределить только один метод.
 */
public abstract class MouseClickedListener implements MouseListener {

  @Override
  public final void mousePressed(MouseEvent e) {
  }

  @Override
  public final void mouseReleased(MouseEvent e) {
  }

  @Override
  public final void mouseEntered(MouseEvent e) {
  }

  @Override
  public final void mouseExited(MouseEvent e) {
  }
}
