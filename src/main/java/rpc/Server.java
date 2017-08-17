package rpc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by wwj on 2017/8/17.
 */
public class Server {
    private int port = 5000;
    private ConcurrentHashMap<String, Invoker> provders = new ConcurrentHashMap<String, Invoker>();

    public void export(final Object target, Class<?> clazz){
        provders.put(clazz.getName(), new Invoker() {
            public Object doInvoker(Invocation invocation) throws Exception {
                Method method = target.getClass().getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                return method.invoke(target, invocation.getArgs());
            }
        });
    }

    public Server(){

    }

    public void openServer(){
        ServerBootstrap b = new ServerBootstrap();
        b.group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("codec", new Codec());
                        pipeline.addLast(new SimpleChannelInboundHandler<Object>() {
                            protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
                                if (msg instanceof Request) {
                                    Request request = (Request) msg;
                                    Response response = new Response();
                                    response.setSeqId(request.getSeqId());
                                    Invocation invocation = request.getData();
                                    Invoker invoker = provders.get(invocation.getClassName());
                                    if (invoker == null) {
                                        response.setStatus(Response.EXCEPTION);
                                        response.setData(new Exception("not export"));
                                    }else{
                                        try {
                                            Object result = invoker.doInvoker(invocation);
                                            response.setStatus(Response.OK);
                                            response.setData(result);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            response.setStatus(Response.EXCEPTION);
                                            response.setData(e);
                                        }
                                    }
                                    ctx.writeAndFlush(response);
                                } else {
                                    System.err.println("not support object " + msg);
                                }
                            }


                        });
                    }
                });
        try {
            ChannelFuture future = b.bind(port).sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
