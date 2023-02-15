package ru.sljuvi.shopmanager.util.report;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRException;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import ru.sljuvi.shopmanager.entity.Product;
import ru.sljuvi.shopmanager.entity.ProductInShop;

public class ProductReportGeneratorTests {

  @Test
  public void generateReportTest() throws IOException, JRException {
    Resource example = new ClassPathResource("reportexample.html");
    String expected = new String(Files.readAllBytes(Path.of(example.getFile().getPath()))).replaceAll("\\r\\n?", "\n");

    ProductReportGenerator reportGenerator = new ProductReportGenerator();
    File file = Files.createTempFile("tempreport", null).toFile();

    List<ProductInShop> productsInShop = new ArrayList<>();
    Product product = new Product();
    product.setName("Nvidia RTX 3050");
    product.setPrice(20000.0);

    ProductInShop productInShop = new ProductInShop();
    productInShop.setProduct(product);
    productInShop.setCount(5);

    productsInShop.add(productInShop);

    reportGenerator.generate(file, productsInShop);

    String actual = new String(Files.readAllBytes(Path.of(file.getPath()))).replaceAll("\\r\\n?", "\n");

    assertEquals(expected, actual);
  }
}
