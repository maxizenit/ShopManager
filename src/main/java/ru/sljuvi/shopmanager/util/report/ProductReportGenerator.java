package ru.sljuvi.shopmanager.util.report;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.sljuvi.shopmanager.entity.Product;
import ru.sljuvi.shopmanager.entity.ProductInShop;

/**
 * Генератор HTML-отчёта JasperReports для товаров в магазине.
 */
public class ProductReportGenerator extends ReportGenerator<ProductInShop, ProductDataBean> {

  /**
   * Файл с шаблоном отчёта.
   */
  private static final Resource REPORT_PATTERN = new ClassPathResource("productreport.jrxml");

  public ProductReportGenerator() {
    super(REPORT_PATTERN);
  }

  @Override
  protected ProductDataBean createDataBeanFromEntity(ProductInShop entity) {
    ProductDataBean dataBean = new ProductDataBean();

    dataBean.setName(entity.getProduct().getName());
    dataBean.setPrice(entity.getProduct().getPrice().toString());
    dataBean.setCount(entity.getCount().toString());

    return dataBean;
  }
}
