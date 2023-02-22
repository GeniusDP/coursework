package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CalculatorTest {

  @Test
  void plus() {
    Assertions.assertEquals(Calculator.plus(2, 2), 4);
    Assertions.assertEquals(Calculator.plus(2, -20), -18);
  }

  @Test
  void minus() {
    Assertions.assertEquals(Calculator.minus(2, 2), 0);
    Assertions.assertEquals(Calculator.minus(2, -20), 22);
  }

  @Test
  void mult() {
    Assertions.assertEquals(Calculator.mult(2, 5), 10);
    Assertions.assertEquals(Calculator.mult(124, 0), 0);
  }

  @Test
  void div() {
    Assertions.assertEquals(Calculator.div(9, 2), 4);
    Assertions.assertEquals(Calculator.div(2, -20), 0);
    Assertions.assertThrows(IllegalArgumentException.class, () -> Calculator.div(-20, 0));

  }
}