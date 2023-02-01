package ru.sljuvi.shopmanager;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ShopManagerApplication {

  public static void main(String[] args) {
    new SpringApplicationBuilder(ShopManagerApplication.class).headless(false).run(args);
  }
}
