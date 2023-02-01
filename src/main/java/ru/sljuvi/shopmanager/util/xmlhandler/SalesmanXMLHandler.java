package ru.sljuvi.shopmanager.util.xmlhandler;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import ru.sljuvi.shopmanager.entity.Salesman;
import ru.sljuvi.shopmanager.service.ShopService;

/**
 * Обработчик XML для продавцов.
 */
public class SalesmanXMLHandler extends XMLHandler<Salesman> {

  private static final String ENTITY_NAME = "salesman";

  private static final String FULL_NAME_ATTR = "fullName";

  private static final String SHOP_ID_ATTR = "shopId";

  private final ShopService shopService;

  public SalesmanXMLHandler(ShopService shopService) {
    super(ENTITY_NAME);
    this.shopService = shopService;
  }

  @Override
  protected Salesman createEntityFromAttrs(NamedNodeMap attrs) {
    Salesman salesman = new Salesman();

    salesman.setFullName(attrs.getNamedItem(FULL_NAME_ATTR).getNodeValue());
    salesman.setShop(
        shopService.getById(Integer.parseInt(attrs.getNamedItem(SHOP_ID_ATTR).getNodeValue())));

    return salesman;
  }

  @Override
  protected void setAttrsFromEntity(Element element, Salesman entity) {
    element.setAttribute(FULL_NAME_ATTR, entity.getFullName());
    element.setAttribute(SHOP_ID_ATTR, entity.getShop().getId().toString());
  }
}
