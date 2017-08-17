package testrpc.consumer;

import rpc.Consumer;
import testrpc.api.Echo;

import java.util.Random;

/**
 * Created by wwj on 2017/8/17.
 */
public class TestConsumer {
    public static void main(String[] args) {
        Consumer consumer = new Consumer();
        consumer.start();
        consumer.refer(Echo.class);
//        consumer.refer(Cal.class);
        final Echo echoProxy = consumer.getProxy(Echo.class);
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long start = System.currentTimeMillis();
            String[] randomName = {"赵大锤🔨","李大炮🚀","张麻子✂️","王二小🐯","钱胖胖💰","👌表情符"};
            String echo = echoProxy.echo(randomName[new Random().nextInt(randomName.length)]);
            System.out.println(Thread.currentThread().getId() + " " + echo + " cost=" + (System.currentTimeMillis() - start));
            System.out.println(Thread.currentThread().getId() + " " + echoProxy.sayHello(randomName[new Random().nextInt(randomName.length)]));

        }
    }
}
