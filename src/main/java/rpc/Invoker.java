package rpc;

/**
 * Created by wwj on 2017/8/17.
 */
public interface Invoker {
    Object doInvoker(Invocation invocation) throws Exception;
}
