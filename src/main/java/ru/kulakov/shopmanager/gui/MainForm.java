package ru.kulakov.shopmanager.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.xml.sax.SAXException;
import ru.kulakov.shopmanager.entity.Product;
import ru.kulakov.shopmanager.entity.ProductInShop;
import ru.kulakov.shopmanager.entity.Salesman;
import ru.kulakov.shopmanager.entity.Shop;
import ru.kulakov.shopmanager.gui.dialog.entitydialog.ProductDialog;
import ru.kulakov.shopmanager.gui.dialog.entitydialog.SalesmanDialog;
import ru.kulakov.shopmanager.gui.dialog.entitydialog.ShopDialog;
import ru.kulakov.shopmanager.gui.entitytable.EntityTable;
import ru.kulakov.shopmanager.gui.entitytable.model.ProductEntityTableModel;
import ru.kulakov.shopmanager.gui.entitytable.model.SalesmanEntityTableModel;
import ru.kulakov.shopmanager.service.ProductInShopService;
import ru.kulakov.shopmanager.service.ProductService;
import ru.kulakov.shopmanager.service.SalesmanService;
import ru.kulakov.shopmanager.service.ShopService;
import ru.kulakov.shopmanager.util.report.ProductReportGenerator;
import ru.kulakov.shopmanager.util.xmlhandler.SalesmanXMLHandler;
import ru.kulakov.shopmanager.gui.entitytable.filter.ProductEntityTableFilter;
import ru.kulakov.shopmanager.gui.entitytable.model.ProductInShopEntityTableModel;

@Component
public class MainForm extends JFrame {

  private final ProductInShopService productInShopService;

  private final ProductService productService;

  private final SalesmanService salesmanService;

  private final ShopService shopService;

  private JPanel mainPanel;

  private JComboBox<Shop> shopCombo;

  private JButton addShopButton;

  private JButton editShopButton;

  private JButton removeShopButton;

  private EntityTable<ProductInShop> productInShopTable;

  private JButton incrementProductInShopButton;

  private JButton decrementProductInShopButton;

  private JButton saveProductsAsHTMLButton;

  private EntityTable<Product> productTable;

  private JButton addProductButton;

  private JButton editProductButton;

  private JButton removeProductButton;

  private JButton addProductInShopButton;

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

  private JTextField nameFilterField;

  private JTextField priceFromFilterField;

  private JTextField priceToFilterField;

  private JButton filterButton;

  private JButton clearFilterButton;
  private JTabbedPane tabbedPane1;

  @Autowired
  public MainForm(ProductInShopService productInShopService, ProductService productService,
                  SalesmanService salesmanService, ShopService shopService) {
    this.productInShopService = productInShopService;
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
    productInShopTable = new EntityTable<>(new ProductInShopEntityTableModel());
    productTable = new EntityTable<>(new ProductEntityTableModel());
    salesmanTable = new EntityTable<>(new SalesmanEntityTableModel());
  }

  private void initGUI() {
    refreshShopCombo();
    refreshProductTable();
    addShopButtonsListeners();
    addProductInShopButtonsListeners();
    addProductButtonsListeners();
    addSalesmanButtonsListeners();
    addFilterButtonsListeners();

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


  private void refreshProductTable() {
    productTable.updateEntities(productService.getAll());
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

  private void addProductInShopButtonsListeners() {
    incrementProductInShopButton.addActionListener(a -> {
      ProductInShop productInShop = productInShopTable.getSelectedEntity();

      if (productInShop != null) {
        productInShopService.increment(productInShop);
        fillAll((Shop) shopCombo.getSelectedItem());
      }
    });

    decrementProductInShopButton.addActionListener(a -> {
      ProductInShop productInShop = productInShopTable.getSelectedEntity();

      if (productInShop != null) {
        productInShopService.decrement(productInShop);
        fillAll((Shop) shopCombo.getSelectedItem());
      }
    });

    ProductReportGenerator reportGenerator = new ProductReportGenerator();
    JFileChooser fileChooser = new JFileChooser();

    saveProductsAsHTMLButton.addActionListener(a -> {
      Shop shop = (Shop) shopCombo.getSelectedItem();

      if (shop != null) {
        List<ProductInShop> productsInShop = productInShopService.getByShop(shop);

        if (!productsInShop.isEmpty()) {
          int result = fileChooser.showSaveDialog(this);

          if (result == JFileChooser.APPROVE_OPTION) {
            try {
              reportGenerator.generate(fileChooser.getSelectedFile(), productsInShop);
            } catch (JRException | IOException e) {
              showMessageDialog(e.getMessage());
            }
          }
        }
      }
    });
  }

  private void addProductButtonsListeners() {
    addProductButton.addActionListener(
            a -> new ProductDialog(this, productService::save, this::refreshProductTable, null));

    editProductButton.addActionListener(a -> {
      Product product = productTable.getSelectedEntity();

      if (product != null) {
        new ProductDialog(this, productService::save, () -> {
          refreshProductTable();
          fillAll((Shop) shopCombo.getSelectedItem());
        }, product);
      }
    });

    removeProductButton.addActionListener(a -> {
      Product product = productTable.getSelectedEntity();

      if (product != null) {
        productService.remove(product);
        refreshProductTable();
        fillAll((Shop) shopCombo.getSelectedItem());
      }
    });

    addProductInShopButton.addActionListener(a -> {
      Product product = productTable.getSelectedEntity();
      Shop shop = (Shop) shopCombo.getSelectedItem();

      if (product != null && shop != null) {
        productInShopService.addProductInShop(shop, product);
        fillAll(shop);
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

  private void addFilterButtonsListeners() {
    filterButton.addActionListener(a -> {
      ProductEntityTableFilter filter = new ProductEntityTableFilter();

      filter.setName(nameFilterField.getText());

      String priceFrom = priceFromFilterField.getText();
      filter.setPriceFrom(StringUtils.hasText(priceFrom) ? Double.parseDouble(priceFrom) : null);

      String priceTo = priceToFilterField.getText();
      filter.setPriceTo(StringUtils.hasText(priceTo) ? Double.parseDouble(priceTo) : null);

      productTable.filter(filter);
    });

    clearFilterButton.addActionListener(a -> {
      nameFilterField.setText(null);
      priceFromFilterField.setText(null);
      priceToFilterField.setText(null);

      productTable.clearFilter();
    });
  }

  private void refreshProductInShopTable(Shop shop) {
    productInShopTable.updateEntities(productInShopService.getByShop(shop));
  }

  private void refreshSalesmanTable(Shop shop) {
    salesmanTable.updateEntities(salesmanService.getByShop(shop));
  }

  private void fillAll(Shop shop) {
    if (shop != null) {
      refreshProductInShopTable(shop);
      refreshSalesmanTable(shop);

      addressLabel.setText(shop.getAddress());
      specializationLabel.setText(shop.getSpecialization());
      directorFullNameLabel.setText(shop.getDirectorFullName());
    } else {
      clearAll();
    }
  }

  private void clearAll() {
    productInShopTable.clear();
    salesmanTable.clear();

    addressLabel.setText(null);
    specializationLabel.setText(null);
    directorFullNameLabel.setText(null);
  }

  /** Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    createUIComponents();
    mainPanel = new JPanel();
    mainPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
    mainPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    shopCombo = new JComboBox();
    panel1.add(shopCombo, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
    panel1.add(panel2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_VERTICAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    addShopButton = new JButton();
    addShopButton.setText("Добавить");
    panel2.add(addShopButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    editShopButton = new JButton();
    editShopButton.setText("Изменить");
    panel2.add(editShopButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    removeShopButton = new JButton();
    removeShopButton.setText("Удалить");
    panel2.add(removeShopButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label1 = new JLabel();
    label1.setText("Магазин");
    panel1.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
    mainPanel.add(panel3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    panel3.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Справка о магазине", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    final JLabel label2 = new JLabel();
    label2.setText("Адрес");
    panel3.add(label2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(13, 16), null, 0, false));
    final JLabel label3 = new JLabel();
    label3.setText("Специализация");
    panel3.add(label3, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(13, 16), null, 0, false));
    final JLabel label4 = new JLabel();
    label4.setText("ФИО директора");
    panel3.add(label4, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(13, 16), null, 0, false));
    addressLabel = new JLabel();
    addressLabel.setText("");
    panel3.add(addressLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    specializationLabel = new JLabel();
    specializationLabel.setText("");
    panel3.add(specializationLabel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    directorFullNameLabel = new JLabel();
    directorFullNameLabel.setText("");
    panel3.add(directorFullNameLabel, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    tabbedPane1 = new JTabbedPane();
    mainPanel.add(tabbedPane1, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
    final JPanel panel4 = new JPanel();
    panel4.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane1.addTab("Товары", panel4);
    final JPanel panel5 = new JPanel();
    panel5.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
    panel4.add(panel5, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    panel5.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Фильтр товаров", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    final JLabel label5 = new JLabel();
    label5.setText("Название");
    panel5.add(label5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label6 = new JLabel();
    label6.setText("Цена");
    panel5.add(label6, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel6 = new JPanel();
    panel6.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
    panel5.add(panel6, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final JLabel label7 = new JLabel();
    label7.setText("от");
    panel6.add(label7, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label8 = new JLabel();
    label8.setText("до");
    panel6.add(label8, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    priceFromFilterField = new JTextField();
    panel6.add(priceFromFilterField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    priceToFilterField = new JTextField();
    panel6.add(priceToFilterField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    nameFilterField = new JTextField();
    panel5.add(nameFilterField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    final JPanel panel7 = new JPanel();
    panel7.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
    panel5.add(panel7, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    filterButton = new JButton();
    filterButton.setText("Поиск");
    panel7.add(filterButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    clearFilterButton = new JButton();
    clearFilterButton.setText("Очистить");
    panel7.add(clearFilterButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel8 = new JPanel();
    panel8.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    panel4.add(panel8, new GridConstraints(0, 0, 2, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final JPanel panel9 = new JPanel();
    panel9.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    panel8.add(panel9, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    panel9.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Все товары", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    panel9.add(productTable, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final JPanel panel10 = new JPanel();
    panel10.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
    panel9.add(panel10, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(504, 69), null, 0, false));
    addProductButton = new JButton();
    addProductButton.setText("Добавить");
    panel10.add(addProductButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    editProductButton = new JButton();
    editProductButton.setText("Изменить");
    panel10.add(editProductButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    removeProductButton = new JButton();
    removeProductButton.setText("Удалить");
    panel10.add(removeProductButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    addProductInShopButton = new JButton();
    addProductInShopButton.setText("Добавить в текущий магазин");
    panel10.add(addProductInShopButton, new GridConstraints(1, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel11 = new JPanel();
    panel11.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    panel8.add(panel11, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    panel11.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Товары в магазине", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    panel11.add(productInShopTable, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final JPanel panel12 = new JPanel();
    panel12.setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
    panel11.add(panel12, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(504, 69), null, 0, false));
    incrementProductInShopButton = new JButton();
    incrementProductInShopButton.setText("Добавить 1 единицу");
    panel12.add(incrementProductInShopButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    saveProductsAsHTMLButton = new JButton();
    saveProductsAsHTMLButton.setText("Сохранить как отчёт в HTML");
    panel12.add(saveProductsAsHTMLButton, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    decrementProductInShopButton = new JButton();
    decrementProductInShopButton.setText("Удалить 1 единицу");
    panel12.add(decrementProductInShopButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel13 = new JPanel();
    panel13.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane1.addTab("Продавцы", panel13);
    final JPanel panel14 = new JPanel();
    panel14.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    panel13.add(panel14, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(504, 121), null, 0, false));
    panel14.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Продавцы", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    panel14.add(salesmanTable, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final JPanel panel15 = new JPanel();
    panel15.setLayout(new GridLayoutManager(2, 3, new Insets(0, 0, 0, 0), -1, -1));
    panel14.add(panel15, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    addSalesmanButton = new JButton();
    addSalesmanButton.setText("Добавить");
    panel15.add(addSalesmanButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    editSalesmanButton = new JButton();
    editSalesmanButton.setText("Изменить");
    panel15.add(editSalesmanButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    removeSalesmanButton = new JButton();
    removeSalesmanButton.setText("Удалить");
    panel15.add(removeSalesmanButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    loadSalesmanFromXMLButton = new JButton();
    loadSalesmanFromXMLButton.setText("Загрузить из XML");
    panel15.add(loadSalesmanFromXMLButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    saveSalesmanToXMLButton = new JButton();
    saveSalesmanToXMLButton.setText("Сохранить в XML");
    panel15.add(saveSalesmanToXMLButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    replaceSalesmanFromXMLButton = new JButton();
    replaceSalesmanFromXMLButton.setText("Заменить из XML");
    panel15.add(replaceSalesmanFromXMLButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
  }

  /** @noinspection ALL */
  public JComponent $$$getRootComponent$$$() {
    return mainPanel;
  }

}
