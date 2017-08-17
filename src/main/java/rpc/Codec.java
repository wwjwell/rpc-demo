package rpc;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by wwj on 2017/8/17.
 */
public class Codec extends ChannelDuplexHandler {
    private ByteBuf in;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            try {

                ByteBuf buf = (ByteBuf)msg;
                if (in == null) {
                    in = ctx.alloc().buffer();
                }
                in.writeBytes(buf.slice());
                int readableBytes = in.readableBytes();
                if (in.readableBytes() < 4) {
                    return;
                }
                in.markReaderIndex();
                int len = in.readInt();
                if (readableBytes < len+4) {
                    in.resetReaderIndex();
                    return;
                }

                byte[] bytes = new byte[len];
                in.readBytes(bytes);
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
                Object o = ois.readObject();
                ctx.fireChannelRead(o);
            } catch (Exception e) {
                if (null != in) {
                    in.release();
                    in = null;
                }
                throw e;
            }
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        try {
            ByteBuf buffer = ctx.alloc().buffer();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(msg);

            byte[] bytes = baos.toByteArray();
            buffer.writeInt(bytes.length);
            buffer.writeBytes(bytes);
            ctx.write(buffer,promise);
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
