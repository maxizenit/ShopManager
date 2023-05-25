package ru.kulakov.shopmanager.util.report;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.springframework.core.io.Resource;

/**
 * Генератор HTML-отчёта JasperReports.
 *
 * @param <E> класс сущности
 * @param <D> класс датабина
 */
public abstract class ReportGenerator<E, D> {

  /**
   * Файл с шаблоном отчёта.
   */
  private final Resource reportPattern;

  public ReportGenerator(Resource reportPattern) {
    this.reportPattern = reportPattern;
  }

  /**
   * Генерирует и записывает отчёт в файл.
   *
   * @param outputFile файл
   * @param entities   список сущностей
   * @throws JRException если возникло исключение при создании отчёта
   * @throws IOException если возникло исключение при работе с файлом
   */
  public void generate(File outputFile, List<E> entities) throws JRException, IOException {
    List<D> dataBeans = new ArrayList<>();
    entities.forEach(e -> dataBeans.add(createDataBeanFromEntity(e)));

    JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataBeans);

    JasperDesign jasperDesign = JRXmlLoader.load(reportPattern.getInputStream());
    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
    JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dataSource);

    JasperExportManager.exportReportToHtmlFile(jasperPrint, outputFile.getCanonicalPath());
  }

  /**
   * Создаёт датабин из сущности.
   *
   * @param entity сущность
   * @return датабин на основе сущности
   */
  protected abstract D createDataBeanFromEntity(E entity);
}
