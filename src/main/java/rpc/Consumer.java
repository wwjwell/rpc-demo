package rpc;

/**
 * Created by wwj on 2017/8/17.
 */
public class Consumer {
    ReferProxyFactory referProxyFactory;
    private Client client = new Client();
    public Consumer(){
        referProxyFactory = new ReferProxyFactory(client);
    }

    public <T> void refer(Class<T> clazz){
        referProxyFactory.refer(clazz);
    }

    public <T> T getProxy(Class<T> clazz) {
        return referProxyFactory.getProxy(clazz);
    }


    public void start(){
        client.connect();
    }

}
