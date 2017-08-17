package test.provider;

import test.api.Echo;
import test.provider.impl.EchoImpl;
import rpc.Provider;

/**
 * Created by wwj on 2017/8/17.
 */
public class TestProvider {
    public static void main(String[] args) {
        Provider provider = new Provider();
        provider.export(new EchoImpl(), Echo.class);
//        test.provider.export(new CalImpl(), Cal.class);
        provider.start();
    }
}
