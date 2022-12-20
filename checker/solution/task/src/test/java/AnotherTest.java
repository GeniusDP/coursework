import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AnotherTest {

  @Test
  public void someAnotherTest(){
    Assertions.assertEquals(true, true);
  }

  @Test
  public void failedTest(){
    Assertions.assertEquals(true, false);
  }


}
