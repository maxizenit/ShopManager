package ru.sljuvi.shopmanager.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import ru.sljuvi.shopmanager.entity.Product;
import ru.sljuvi.shopmanager.entity.Salesman;
import ru.sljuvi.shopmanager.entity.Shop;
import ru.sljuvi.shopmanager.gui.dialog.entitydialog.ProductDialog;
import ru.sljuvi.shopmanager.gui.dialog.entitydialog.SalesmanDialog;
import ru.sljuvi.shopmanager.gui.dialog.entitydialog.ShopDialog;
import ru.sljuvi.shopmanager.gui.entitytable.EntityTable;
import ru.sljuvi.shopmanager.gui.entitytable.model.ProductEntityTableModel;
import ru.sljuvi.shopmanager.gui.entitytable.model.SalesmanEntityTableModel;
import ru.sljuvi.shopmanager.service.ProductService;
import ru.sljuvi.shopmanager.service.SalesmanService;
import ru.sljuvi.shopmanager.service.ShopService;
import ru.sljuvi.shopmanager.util.report.ProductReportGenerator;
import ru.sljuvi.shopmanager.util.xmlhandler.SalesmanXMLHandler;

@Component
public class MainForm extends JFrame {

  private final ProductService productService;

  private final SalesmanService salesmanService;

  private final ShopService shopService;

  private JPanel mainPanel;

  private JComboBox<Shop> shopCombo;

  private JButton addShopButton;

  private JButton editShopButton;

  private JButton removeShopButton;

  private EntityTable<Product> productTable;

  private JButton addProductButton;

  private JButton editProductButton;

  private JButton removeProductButton;

  private JButton saveProductsAsHTMLButton;

  private EntityTable<Salesman> salesmanTable;

  private JButton addSalesmanButton;

  private JButton editSalesmanButton;

  private JButton removeSalesmanButton;

  private JButton loadSalesmanFromXMLButton;

  private JButton saveSalesmanToXMLButton;

  private JButton replaceSalesmanFromXMLButton;

  private JLabel addressLabel;

  private JLabel specializationLabel;

  private JLabel directorFullNameLabel;

  @Autowired
  public MainForm(ProductService productService, SalesmanService salesmanService,
      ShopService shopService) {
    this.productService = productService;
    this.salesmanService = salesmanService;
    this.shopService = shopService;

    $$$setupUI$$$();
    initGUI();
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setContentPane(mainPanel);
    setMinimumSize(new Dimension(800, 600));
    pack();
    setVisible(true);
  }

  private void showMessageDialog(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  private void createUIComponents() {
    productTable = new EntityTable<>(new ProductEntityTableModel());
    salesmanTable = new EntityTable<>(new SalesmanEntityTableModel());
  }

  private void initGUI() {
    refreshShopCombo();
    addShopButtonsListeners();
    addProductButtonsListeners();
    addSalesmanButtonsListeners();

    shopCombo.addActionListener(a -> {
      Shop shop = (Shop) shopCombo.getSelectedItem();

      if (shop != null) {
        fillAll(shop);
      } else {
        clearAll();
      }
    });
  }

  private void refreshShopCombo() {
    DefaultComboBoxModel<Shop> model = new DefaultComboBoxModel<>();
    model.addAll(shopService.getAll());
    shopCombo.setModel(model);
  }

  private void addShopButtonsListeners() {
    addShopButton.addActionListener(
        a -> new ShopDialog(this, shopService::save, this::refreshShopCombo, null));

    editShopButton.addActionListener(a -> {
      Shop shop = (Shop) shopCombo.getSelectedItem();

      if (shop != null) {
        new ShopDialog(this, shopService::save, () -> {
          refreshShopCombo();
          fillAll(shop);
        }, shop);
      }
    });

    removeShopButton.addActionListener(a -> {
      Shop shop = (Shop) shopCombo.getSelectedItem();

      if (shop != null) {
        shopService.remove(shop);
        refreshShopCombo();
        clearAll();
      }
    });
  }

  private void addProductButtonsListeners() {
    addProductButton.addActionListener(a -> new ProductDialog(this, productService::save,
        () -> refreshProductTable((Shop) shopCombo.getSelectedItem()), null, shopService.getAll()));

    editProductButton.addActionListener(a -> {
      Product product = productTable.getSelectedEntity();

      if (product != null) {
        new ProductDialog(this, productService::save,
            () -> refreshProductTable((Shop) shopCombo.getSelectedItem()), product,
            shopService.getAll());
      }
    });

    removeProductButton.addActionListener(a -> {
      Product product = productTable.getSelectedEntity();

      if (product != null) {
        productService.remove(product);
        refreshProductTable((Shop) shopCombo.getSelectedItem());
      }
    });

    ProductReportGenerator reportGenerator = new ProductReportGenerator();
    JFileChooser fileChooser = new JFileChooser();

    saveProductsAsHTMLButton.addActionListener(a -> {
      Shop shop = (Shop) shopCombo.getSelectedItem();

      if (shop != null) {
        List<Product> products = productService.getByShop(shop);

        if (!products.isEmpty()) {
          int result = fileChooser.showSaveDialog(this);

          if (result == JFileChooser.APPROVE_OPTION) {
            try {
              reportGenerator.generate(fileChooser.getSelectedFile(), products);
            } catch (JRException | IOException e) {
              showMessageDialog(e.getMessage());
            }
          }
        }
      }
    });
  }

  private void addSalesmanButtonsListeners() {
    addSalesmanButton.addActionListener(a -> new SalesmanDialog(this, salesmanService::save,
        () -> refreshSalesmanTable((Shop) shopCombo.getSelectedItem()), null,
        shopService.getAll()));

    editSalesmanButton.addActionListener(a -> {
      Salesman salesman = salesmanTable.getSelectedEntity();

      if (salesman != null) {
        new SalesmanDialog(this, salesmanService::save,
            () -> refreshSalesmanTable((Shop) shopCombo.getSelectedItem()), salesman,
            shopService.getAll());
      }
    });

    removeSalesmanButton.addActionListener(a -> {
      Salesman salesman = salesmanTable.getSelectedEntity();

      if (salesman != null) {
        salesmanService.remove(salesman);
        salesmanTable.updateEntities(salesmanService.getByShop((Shop) shopCombo.getSelectedItem()));
      }
    });

    JFileChooser fileChooser = new JFileChooser();
    SalesmanXMLHandler xmlHandler = new SalesmanXMLHandler(shopService);

    loadSalesmanFromXMLButton.addActionListener(a -> {
      int result = fileChooser.showOpenDialog(this);

      if (result == JFileChooser.APPROVE_OPTION) {
        try {
          salesmanService.saveAll(xmlHandler.loadFromXML(fileChooser.getSelectedFile()));
          Shop shop = (Shop) shopCombo.getSelectedItem();
          if (shop != null) {
            refreshSalesmanTable(shop);
          }
        } catch (IOException | SAXException e) {
          showMessageDialog(e.getMessage());
        }
      }
    });

    saveSalesmanToXMLButton.addActionListener(a -> {
      Shop shop = (Shop) shopCombo.getSelectedItem();

      if (shop != null) {
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
          xmlHandler.saveToXML(fileChooser.getSelectedFile(), salesmanService.getByShop(shop));
        }
      }
    });

    replaceSalesmanFromXMLButton.addActionListener(a -> {
      Shop shop = (Shop) shopCombo.getSelectedItem();

      if (shop != null) {
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          final List<Salesman>[] salesmen = new List[]{new ArrayList<>()};

          Thread removeThread = new Thread(() -> salesmanService.removeByShop(shop));
          Thread loadThread = new Thread(() -> {
            try {
              salesmen[0] = xmlHandler.loadFromXML(file);
            } catch (IOException | SAXException e) {
              SwingUtilities.invokeLater(() -> showMessageDialog(e.getMessage()));
            }
          });

          Thread saveThread = new Thread(() -> salesmanService.saveAll(salesmen[0]));

          try {
            removeThread.start();
            loadThread.start();

            removeThread.join();
            loadThread.join();

            saveThread.start();
            saveThread.join();
          } catch (InterruptedException e) {
            removeThread.interrupt();
            loadThread.interrupt();
            saveThread.interrupt();

            showMessageDialog(e.getMessage());
          } finally {
            refreshSalesmanTable(shop);
          }
        }
      }
    });
  }

  private void refreshProductTable(Shop shop) {
    productTable.updateEntities(productService.getByShop(shop));
  }

  private void refreshSalesmanTable(Shop shop) {
    salesmanTable.updateEntities(salesmanService.getByShop(shop));
  }

  private void fillAll(Shop shop) {
    refreshProductTable(shop);
    refreshSalesmanTable(shop);

    addressLabel.setText(shop.getAddress());
    specializationLabel.setText(shop.getSpecialization());
    directorFullNameLabel.setText(shop.getDirectorFullName());
  }

  private void clearAll() {
    productTable.clear();
    salesmanTable.clear();

    addressLabel.setText(null);
    specializationLabel.setText(null);
    directorFullNameLabel.setText(null);
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    createUIComponents();
    mainPanel = new JPanel();
    mainPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
    mainPanel.add(panel1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_NORTH,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    shopCombo = new JComboBox();
    panel1.add(shopCombo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
    panel1.add(panel2,
        new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL,
            GridConstraints.SIZEPOLICY_FIXED,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    addShopButton = new JButton();
    addShopButton.setText("Добавить");
    panel2.add(addShopButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    editShopButton = new JButton();
    editShopButton.setText("Изменить");
    panel2.add(editShopButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    removeShopButton = new JButton();
    removeShopButton.setText("Удалить");
    panel2.add(removeShopButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label1 = new JLabel();
    label1.setText("Магазин");
    panel1.add(label1,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    mainPanel.add(panel3,
        new GridConstraints(1, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    panel3.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Товары",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    panel3.add(productTable,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    final JPanel panel4 = new JPanel();
    panel4.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
    panel3.add(panel4, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    addProductButton = new JButton();
    addProductButton.setText("Добавить");
    panel4.add(addProductButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    editProductButton = new JButton();
    editProductButton.setText("Изменить");
    panel4.add(editProductButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    removeProductButton = new JButton();
    removeProductButton.setText("Удалить");
    panel4.add(removeProductButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    saveProductsAsHTMLButton = new JButton();
    saveProductsAsHTMLButton.setText("Сохранить как отчёт в HTML");
    panel4.add(saveProductsAsHTMLButton,
        new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel5 = new JPanel();
    panel5.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    mainPanel.add(panel5,
        new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    panel5.setBorder(
        BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Продавцы",
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    panel5.add(salesmanTable,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    final JPanel panel6 = new JPanel();
    panel6.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
    panel5.add(panel6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    addSalesmanButton = new JButton();
    addSalesmanButton.setText("Добавить");
    panel6.add(addSalesmanButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    editSalesmanButton = new JButton();
    editSalesmanButton.setText("Изменить");
    panel6.add(editSalesmanButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    removeSalesmanButton = new JButton();
    removeSalesmanButton.setText("Удалить");
    panel6.add(removeSalesmanButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    loadSalesmanFromXMLButton = new JButton();
    loadSalesmanFromXMLButton.setText("Загрузить из XML");
    panel6.add(loadSalesmanFromXMLButton,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    saveSalesmanToXMLButton = new JButton();
    saveSalesmanToXMLButton.setText("Сохранить в XML");
    panel6.add(saveSalesmanToXMLButton,
        new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    replaceSalesmanFromXMLButton = new JButton();
    replaceSalesmanFromXMLButton.setText("Заменить из XML");
    panel6.add(replaceSalesmanFromXMLButton,
        new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel7 = new JPanel();
    panel7.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
    mainPanel.add(panel7, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_SOUTH,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    panel7.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black),
        "Справка о магазине", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
        null, null));
    final JLabel label2 = new JLabel();
    label2.setText("Адрес");
    panel7.add(label2,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null,
            new Dimension(13, 16), null, 0, false));
    final JLabel label3 = new JLabel();
    label3.setText("Специализация");
    panel7.add(label3,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null,
            new Dimension(13, 16), null, 0, false));
    final JLabel label4 = new JLabel();
    label4.setText("ФИО директора");
    panel7.add(label4,
        new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null,
            new Dimension(13, 16), null, 0, false));
    addressLabel = new JLabel();
    addressLabel.setText("");
    panel7.add(addressLabel,
        new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    specializationLabel = new JLabel();
    specializationLabel.setText("");
    panel7.add(specializationLabel,
        new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    directorFullNameLabel = new JLabel();
    directorFullNameLabel.setText("");
    panel7.add(directorFullNameLabel,
        new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return mainPanel;
  }

}
