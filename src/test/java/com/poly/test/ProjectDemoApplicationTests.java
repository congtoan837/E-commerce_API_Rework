package com.poly.test;

import com.poly.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectDemoApplicationTests {
    @Test
    public void testRun(){
        System.out.print("Ok");
    }

    @Test
    public void testVatCalculator() {
        VatCalculator calc = new VatCalculator();
        double expected = 10;
        Assertions.assertEquals(calc.getValOnAmount(100), expected, "Incorrect amount.");
//        Assertions.assertEquals(calc.getValOnAmount(120), expected, "Incorrect amount.");
    }

    @Test
    public void testLogin() {

    }
}
