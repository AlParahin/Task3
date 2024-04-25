package com.example.task3;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UtilsTest {
    private static AnyClass realObject;
    private static AnyClassable proxyObject;

    private int resultCheck;

    @BeforeEach
    void newObjects() {
        realObject = new AnyClass();
        proxyObject = Utils.cache(realObject);
    }

    @Test
    @DisplayName("Без кеширования")
    void testRealMethod() {
        resultCheck = realObject.doubleValue();
        resultCheck = realObject.doubleValue();
        resultCheck = realObject.doubleValue();
        Assertions.assertEquals(8,resultCheck);
    }

    @Test
    @DisplayName("С кешированием")
    void testCachedMethod() {
        resultCheck = proxyObject.doubleValue();
        resultCheck = proxyObject.doubleValue();
        resultCheck = proxyObject.doubleValue();
        Assertions.assertEquals(2,resultCheck);
    }

    @Test
    @DisplayName("С кешированием + Mutator")
    void testMutatedMethod() {
        proxyObject.setValue(3);
        resultCheck = proxyObject.doubleValue();
        resultCheck = proxyObject.doubleValue();
        resultCheck = proxyObject.doubleValue();
        Assertions.assertEquals(6,resultCheck);
    }
}

interface AnyClassable {
    int doubleValue();
    void setValue(int someInt);
}

class AnyClass implements AnyClassable{
    private int someInt;
    public AnyClass() {
        this.someInt = 1;
    }

    @Cache
    public int doubleValue() {
        this.someInt += this.someInt;
        return this.someInt;
    }

    @Mutator
    public void setValue(int someInt) {
        this.someInt = someInt;
    }
}