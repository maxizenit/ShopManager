package ru.sljuvi.shopmanager.gui.dialog.entitydialog;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.springframework.util.StringUtils;
import ru.sljuvi.shopmanager.entity.Shop;
import ru.sljuvi.shopmanager.exception.InvalidFieldException;

public class ShopDialog extends EntityDialog<Shop> {

  private JPanel contentPane;

  private JButton buttonOK;

  private JButton buttonCancel;

  private JTextField addressField;

  private JTextField specializationField;

  private JTextField directorFullNameField;

  public ShopDialog(JFrame parent, SaveFunction<Shop> saveFunction,
      RefreshGUIFunction refreshGUIFunction, Shop entity) {
    super(parent, saveFunction, refreshGUIFunction, entity);
    init(contentPane, buttonOK, buttonCancel);
    pack();
    setVisible(true);
  }

  @Override
  protected void fillFields(Shop entity) {
    addressField.setText(entity.getAddress());
    specializationField.setText(entity.getSpecialization());
    directorFullNameField.setText(entity.getDirectorFullName());
  }

  @Override
  protected void validateFields() throws InvalidFieldException {
    List<String> invalidFields = new ArrayList<>();

    if (!StringUtils.hasText(addressField.getText())) {
      invalidFields.add("адрес");
    }

    if (!StringUtils.hasText(specializationField.getText())) {
      invalidFields.add("специализация");
    }

    if (!StringUtils.hasText(directorFullNameField.getText())) {
      invalidFields.add("ФИО директора");
    }

    if (!invalidFields.isEmpty()) {
      throw new InvalidFieldException(invalidFields);
    }
  }

  @Override
  protected Shop createNewEntity() {
    return new Shop();
  }

  @Override
  protected void fillEntityFromFields(Shop entity) {
    entity.setAddress(addressField.getText());
    entity.setSpecialization(specializationField.getText());
    entity.setDirectorFullName(directorFullNameField.getText());
  }

  {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    contentPane = new JPanel();
    contentPane.setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
    contentPane.add(panel1,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null,
            null, null, 0, false));
    final Spacer spacer1 = new Spacer();
    panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null,
        0, false));
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1, true, false));
    panel1.add(panel2,
        new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    buttonOK = new JButton();
    buttonOK.setText("ОК");
    panel2.add(buttonOK, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    buttonCancel = new JButton();
    buttonCancel.setText("Отмена");
    panel2.add(buttonCancel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
    contentPane.add(panel3,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    final JLabel label1 = new JLabel();
    label1.setText("Адрес");
    panel3.add(label1,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    final JLabel label2 = new JLabel();
    label2.setText("Специализация");
    panel3.add(label2,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    final JLabel label3 = new JLabel();
    label3.setText("ФИО директора");
    panel3.add(label3,
        new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    addressField = new JTextField();
    panel3.add(addressField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    specializationField = new JTextField();
    panel3.add(specializationField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    directorFullNameField = new JTextField();
    panel3.add(directorFullNameField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return contentPane;
  }

}
