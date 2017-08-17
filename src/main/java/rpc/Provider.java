package rpc;

/**
 * Created by wwj on 2017/8/17.
 */
public class Provider {
    private Server server;
    public Provider(){
        server = new Server();
    }
    public void export(Object target, Class<?> clazz) {
        server.export(target, clazz);
    }

    public void start(){
        server.openServer();
    }
}
