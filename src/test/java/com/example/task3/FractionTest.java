package com.example.task3;

import lombok.Getter;

@Getter
public class FractionTest implements Fractionable {
    int num, denum, counter;

    public FractionTest(int num, int denum) {
        this.num = num;
        this.denum = denum;
    }

    @Override
    @Cache(1000L)
    public double doubleValue() {
        counter++;
        return (double) num / denum;
    }

    @Cache()
    public double doubleValue_0L() {
        counter++;
        return (double) num / denum;
    }

    @Override
    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Override
//    @Mutator
    public void setDenum(int denum) {
        this.denum = denum;
    }

    @Override
    @Cache(5000L)
    public String toString() {
        counter++;
        return "FractionTest{" +
                "num=" + num +
                ", denum=" + denum +
                ", counter=" + counter +
                '}';
    }
}
