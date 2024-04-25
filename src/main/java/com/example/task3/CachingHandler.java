package com.example.task3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachingHandler<T> implements InvocationHandler {

    public static ExecutorService executorService = Executors.newFixedThreadPool(2);

    private final T currentObject;
    private final Map<State, Map<Method, Result>> states = new HashMap<>();

    private Map<Method, Result> curStateResults;
    private State curState;

    public CachingHandler(T currentObject) {
        this.currentObject = currentObject;
        curStateResults = new ConcurrentHashMap<>();
        curState = new State();
        states.put(curState, curStateResults);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object resultObject;
        Method currentMethod = currentObject.getClass().getMethod(method.getName(), method.getParameterTypes());

        if (currentMethod.isAnnotationPresent(Cache.class)) {
            long time = currentMethod.getAnnotation(Cache.class).value();
            Result result = curStateResults.get(currentMethod);
            if (result != null) {
                if (time == 0) {
                    result.expiryTime = 0L;
                    curStateResults.put(currentMethod, result);
                    return result.value; // cached before
                }
                if (result.expiryTime > System.currentTimeMillis()) {
                    result.expiryTime = System.currentTimeMillis() + time;
                    curStateResults.put(currentMethod, result);
                    return result.value; // cached before
                }
            }

            resultObject = method.invoke(currentObject, args);
            result = new Result(System.currentTimeMillis() + time, resultObject);
            if (time == 0) result.expiryTime = 0L;
            curStateResults.put(currentMethod, result);
            return resultObject; // cached now
        }

        if (currentMethod.isAnnotationPresent(Mutator.class)) {
            curState = new State(curState, currentMethod, args);
            if (states.containsKey(curState)) {
                curStateResults = states.get(curState);
            } else {
                curStateResults = new ConcurrentHashMap<>();
                states.put(curState, curStateResults);
            }
            CachingHandler.executorService.execute(new CacheCleaner());
        }
        return method.invoke(currentObject, args);
    }

    private class CacheCleaner implements Runnable {
        @Override
        public void run() {
            for (Map<Method, Result> valuesMap : states.values()) {
                for (Method method : valuesMap.keySet()) {
                    Result result = valuesMap.get(method);
                    if (result.expiryTime == 0) {
                        continue;
                    }
                    if (result.expiryTime <= System.currentTimeMillis()) {
                        valuesMap.remove(method);
                    }
                }
            }
        }
    }
}
