package rpc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wwj on 2017/8/17.
 */
public class ReferProxyFactory {

    private Invoker invoker;

    public ReferProxyFactory(Invoker invoker){
        this.invoker = invoker;
    }
    private ConcurrentHashMap<String, Object> refers = new ConcurrentHashMap<String, Object>();

    public <T> void refer(Class<T> clazz) {
        T proxy = (T) Proxy.newProxyInstance(
                this.getClass().getClassLoader(),
                new Class[]{clazz},
                new ReferInvocationHandler(clazz) );
        refers.put(clazz.getName(), proxy);
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) refers.get(clazz.getName());
    }


    public class ReferInvocationHandler implements InvocationHandler {
        private Class clazz;
        public ReferInvocationHandler(Class clazz){
            this.clazz = clazz;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Invocation invocation = new Invocation();
            invocation.setClassName(clazz.getName());
            invocation.setParamterTypes(method.getParameterTypes());
            invocation.setMethodName(method.getName());
            invocation.setArgs(args);
            return invoker.doInvoker(invocation);
        }
    }

}
