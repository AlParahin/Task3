package com.example.task3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UtilsTest {
    private static FractionTest fraction;
    private static Fractionable fractionProxy;

    @BeforeEach
    void newTestObjects() {
        fraction = new FractionTest(2, 2);
        fractionProxy = Utils.cache(fraction);
        fractionProxy.setNum(fraction.getNum());
        fractionProxy.setDenum(fraction.getDenum());
    }

    @Test
    @DisplayName("Без кеширования: количество реальных выполнений > 1")
    void test_1_RealCounter() {
        fraction.doubleValue();
        fraction.doubleValue();
        Assertions.assertEquals(2, fraction.getCounter());
    }

    @Test
    @DisplayName("С кешированием: выполнение 1 раз")
    void test_2_CachedMethod_1() {
        fractionProxy.doubleValue();
        fractionProxy.doubleValue();
        fractionProxy.doubleValue();
        Assertions.assertEquals(1, fraction.getCounter());
    }

    @Test
    @DisplayName("С кешированием 2 операций: 2 выполнения")
    void test_3_CachedMethod_2() {
        fractionProxy.toString();
        fractionProxy.doubleValue();
        fractionProxy.doubleValue();
        fractionProxy.toString();
        fractionProxy.doubleValue();
        fractionProxy.toString();
        Assertions.assertEquals(2, fraction.getCounter());
    }

    @Test
    @DisplayName("С кешированием + Mutator: 2 выполнения")
    void test_4_MutatedMethod() {
        fractionProxy.doubleValue();
        fractionProxy.doubleValue();
        fractionProxy.doubleValue();
        fractionProxy.setNum(4);
        fractionProxy.doubleValue();
        fractionProxy.doubleValue();
        fractionProxy.doubleValue();
        Assertions.assertEquals(2, fraction.getCounter());
    }

    @Test
    @DisplayName("С кешированием + Mutator + обратный Mutator к предыдущему состоянию")
    void test_5_MutatedMethodResult() {
        fractionProxy.doubleValue();
        fractionProxy.doubleValue();
        fractionProxy.doubleValue();
        fractionProxy.setNum(4);
        fractionProxy.doubleValue();
        fractionProxy.doubleValue();
        fractionProxy.doubleValue();
        fractionProxy.setNum(2);
        fractionProxy.doubleValue();
        fractionProxy.doubleValue();
        fractionProxy.doubleValue();
        Assertions.assertEquals(2, fraction.getCounter());
    }

    @Test
    @DisplayName("C кэшированием, но с превышением срока кэша для doubleValue(): 3 выполнения")
    void test_6_LongMethod() throws InterruptedException {
        fractionProxy.doubleValue();
        fractionProxy.doubleValue();
        fractionProxy.toString();
        Thread.sleep(2000L);
        fractionProxy.doubleValue();
        fractionProxy.doubleValue();
        fractionProxy.toString();
        Assertions.assertEquals(3, fraction.getCounter());
    }

    @Test
    @DisplayName("C кэшированием, но с превышением срока кэша для doubleValue(): 3 выполнения")
    void test_7_EternalMethod() throws InterruptedException {
        fractionProxy.doubleValue_0L();
        fractionProxy.doubleValue_0L();
        Thread.sleep(10000L);
        fractionProxy.doubleValue_0L();
        fractionProxy.doubleValue_0L();
        Assertions.assertEquals(1, fraction.getCounter());
    }

    @Test
    @DisplayName("Исходное состояние: не вызывает кэширования")
    void test_8_Initial() {
        Assertions.assertEquals(0, fraction.counter);
    }

}