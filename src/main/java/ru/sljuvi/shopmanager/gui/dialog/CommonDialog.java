package ru.sljuvi.shopmanager.gui.dialog;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

/**
 * Класс, представляющий диалог.
 */
public abstract class CommonDialog extends JDialog {

  public CommonDialog(JFrame parent) {
    super(parent);
  }

  /**
   * Инициализирует окно диалога и добавляет обработчик события для кнопки "ОК".
   *
   * @param contentPane главная панель диалога
   * @param buttonOK    кнопка "ОК"
   */
  protected void init(JPanel contentPane, JButton buttonOK) {
    setContentPane(contentPane);
    setModal(true);

    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    contentPane.registerKeyboardAction(e -> onCancel(),
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    getRootPane().setDefaultButton(buttonOK);
    buttonOK.addActionListener(e -> onOK());
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    setMinimumSize(contentPane.getMinimumSize());
    setSize(contentPane.getPreferredSize());

    Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation((int) (dimension.getWidth() / 2), (int) (dimension.getHeight() / 2));
    setResizable(false);
  }

  /**
   * Выходит из диалога по нажатию кнопки ОК.
   */
  protected void onOK() {
    onCancel();
  }

  /**
   * Выходит из диалога по нажатию кнопки Отмена.
   */
  protected void onCancel() {
    dispose();
  }
}
