package rpc;

import java.io.Serializable;

/**
 * Created by wwj on 2017/8/17.
 */
public class Invocation implements Serializable {
    private static final long serialVersionUID = -615712407256946194L;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] args;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParamterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }


}
