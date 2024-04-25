package com.example.task3;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class CachingHandler<T> implements InvocationHandler {
    private final T currentObject;
    private final Map<Method, Object> results = new HashMap<>();

    public CachingHandler(T currentObject) {
        this.currentObject = currentObject;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object resultObject;
        Method currentMethod;
        currentMethod = currentObject.getClass().getMethod(method.getName(), method.getParameterTypes());

        if (currentMethod.isAnnotationPresent(Cache.class)) {
            if (results.containsKey(currentMethod)) {
                return results.get(currentMethod); // cached before
            }
            resultObject = method.invoke(currentObject, args);
            results.put(currentMethod, resultObject);
            return resultObject;
        }

        if (currentMethod.isAnnotationPresent(Mutator.class)) {
            System.out.println(currentMethod.getName() + " is Mutator");
//            results.clear();
        }
        return method.invoke(currentObject, args);
    }
}
