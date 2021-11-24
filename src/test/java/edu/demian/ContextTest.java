package edu.demian;


import edu.demian.configs.SpringConfig;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = SpringConfig.class)
public class ContextTest {

  @Test
  public void simpleTest() {
    assertTrue(true);
  }

}
