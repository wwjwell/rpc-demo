package rpc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by wwj on 2017/8/17.
 */
public class Client implements Invoker{
    private AtomicInteger SEQ = new AtomicInteger(1);
    private Channel channel;
    private String host = "127.0.0.1";
    private int port = 5000;
    public Object doInvoker(Invocation invocation) throws Exception {
        return send(invocation);
    }


    public Client (){
    }

    public void connect(){
        Bootstrap b = new Bootstrap();
        b.group(new NioEventLoopGroup()).channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("codec", new Codec());
                        pipeline.addLast(new SimpleChannelInboundHandler<Object>() {
                            protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
                                if (msg instanceof Response) {
                                    DefaultFuture.received((Response) msg);
                                }else{
                                    System.err.println("not support object "+msg);
                                }
                            }
                        });
                    }
                });
        ChannelFuture future = b.connect(host,port);
        try {
            channel = future.sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Object send(Invocation invocation) throws Exception {
        Request request = new Request();
        request.setSeqId(SEQ.getAndIncrement());
        request.setData(invocation);
        DefaultFuture defaultFuture = new DefaultFuture(request.getSeqId(), request, 10000);
        channel.writeAndFlush(request);
        defaultFuture.send();
        return defaultFuture.get();

    }

}
