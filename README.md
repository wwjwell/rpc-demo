#RPC DEMO
## provider
```java
    Provider provider = new Provider();
    provider.export(new EchoImpl(), Echo.class); //export api
    provider.start(); //provider start
```
## consumer
```java
    Consumer consumer = new Consumer();
    consumer.refer(Echo.class);
    //consumer.refer(Cal.class);

    consumer.start();
    Echo echoProxy = consumer.getProxy(Echo.class);
    echoProxy.echo("wwj");
    
```
## 测试GIT2