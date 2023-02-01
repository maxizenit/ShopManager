package ru.sljuvi.shopmanager.util.xmlhandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Обработчик XML для сущностей.
 *
 * @param <T> класс сущности
 */
public abstract class XMLHandler<T> {

  private static final DocumentBuilder dBuilder;

  private static final String LIST_TEXT = "list";

  static {
    try {
      dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Наименование тега сущности.
   */
  private final String entityName;

  public XMLHandler(String entityName) {
    this.entityName = entityName;
  }

  /**
   * Парсит XML-файл в список сущностей.
   *
   * @param file XML-файл
   * @return список сущностей
   * @throws IOException  если возникла ошибка при работе с файлом
   * @throws SAXException если структура документа нарушена
   */
  public List<T> loadFromXML(File file) throws IOException, SAXException {
    List<T> result = new ArrayList<>();
    Document document = dBuilder.parse(file);
    document.getDocumentElement().normalize();

    NodeList elements = document.getElementsByTagName(entityName);
    for (int i = 0; i < elements.getLength(); ++i) {
      NamedNodeMap attrs = elements.item(i).getAttributes();
      result.add(createEntityFromAttrs(attrs));
    }

    return result;
  }

  /**
   * Сохраняет список сущностей как XML-файл.
   *
   * @param file     файл
   * @param entities список сущностей
   */
  public void saveToXML(File file, List<T> entities) {
    try (FileOutputStream stream = new FileOutputStream(file)) {
      if (!file.exists()) {
        file.createNewFile();
      }

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      transformerFactory.setAttribute("indent-number", 2);

      Transformer transformer = transformerFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      DOMSource source = new DOMSource(createDocument(entities));
      StreamResult result = new StreamResult(stream);

      transformer.transform(source, result);
    } catch (IOException | TransformerException e) {
      throw new RuntimeException(e);
    }
  }

  private Document createDocument(List<T> entities) {
    Document document = dBuilder.newDocument();
    Node list = document.createElement(LIST_TEXT);
    document.appendChild(list);

    entities.forEach(e -> {
      Element element = document.createElement(entityName);
      setAttrsFromEntity(element, e);
      list.appendChild(element);
    });

    return document;
  }

  /**
   * Создаёт сущность на основе переданных атрибутов.
   *
   * @param attrs атрибуты
   * @return сущность
   */
  protected abstract T createEntityFromAttrs(NamedNodeMap attrs);

  /**
   * Применяет к элементу документа атрибуты на основе полей сущности.
   *
   * @param element элемент документа
   * @param entity  сущность
   */
  protected abstract void setAttrsFromEntity(Element element, T entity);
}
