package com.example.task3;

public class AppStarter {
    public static void main(String... args) throws Exception {
        Fraction fr = new Fraction(2, 2);
        Fractionable num = Utils.cache(fr);
        num.setNum(fr.getNum());
        num.setDenum(fr.getDenum()); // без этих мутаторов начальное состояние пустое
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит

        num.setNum(4);
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит

        num.setNum(2);
        num.doubleValue();// sout молчит
        num.doubleValue();// sout молчит

        Thread.sleep(1500L);
        System.out.println("after 1500 ms sleep");
        num.doubleValue();// sout сработал
        num.doubleValue();// sout молчит

        if (!CachingHandler.executorService.isShutdown()) CachingHandler.executorService.shutdown();
    }
}
