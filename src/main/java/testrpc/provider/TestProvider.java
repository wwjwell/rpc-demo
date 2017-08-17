package testrpc.provider;

import testrpc.api.Echo;
import testrpc.provider.impl.EchoImpl;
import rpc.Provider;

/**
 * Created by wwj on 2017/8/17.
 */
public class TestProvider {
    public static void main(String[] args) {
        Provider provider = new Provider();
        provider.export(new EchoImpl(), Echo.class);
//        testrpc.provider.export(new CalImpl(), Cal.class);
        provider.start();
    }
}
