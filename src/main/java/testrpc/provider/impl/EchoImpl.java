package test.provider.impl;

import test.api.Echo;

import java.util.UUID;

/**
 * Created by wwj on 2017/8/17.
 */
public class EchoImpl implements Echo {
    public String sayHello(String who) {
        return who + " Hello";
    }

    public String echo(String who) {
        return String.format("uuid=%s server ack %s", UUID.randomUUID().toString().substring(0,10), who);
    }
}
