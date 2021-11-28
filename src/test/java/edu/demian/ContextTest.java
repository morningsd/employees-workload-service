package edu.demian;

import edu.demian.configs.JpaConfig;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = JpaConfig.class)
public class ContextTest {

  @Test
  public void simpleTest() {
  }

}
