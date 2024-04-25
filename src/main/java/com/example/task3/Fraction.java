package com.example.task3;

import lombok.*;

@Getter
public class Fraction implements Fractionable {
    private int num;
    private int denum;

    public Fraction(int num, int denum) {
        this.num = num;
        setDenum(denum);
    }

    @Mutator
    public void setNum(int num) {
        this.num = num;
    }

    @Mutator
    public void setDenum(int denum) {
        if (denum == 0) throw new IllegalArgumentException("/ by zero error");
        this.denum = denum;
    }

    @Override
    @Cache(1000L)
    public double doubleValue() {
        System.out.println("invoke doubleValue() with " + num + " / " + denum);
        return (double) num / denum;
    }

    @Override
    @Cache()
    public double doubleValue_0L() {
        System.out.println("invoke doubleValue_0L() with " + num + " / " + denum);
        return (double) num / denum;
    }

    @Override
    @Cache(5000L)
    public String toString() {
        return "Fraction{" +
                "num=" + num +
                ", denum=" + denum +
                '}';
    }
}
