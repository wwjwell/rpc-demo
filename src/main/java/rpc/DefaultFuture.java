package rpc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wwj on 2017/8/17.
 */
public class DefaultFuture {
    private static final ConcurrentHashMap<Integer, DefaultFuture> FUTURES = new ConcurrentHashMap<Integer, DefaultFuture>();
    private final int seqId;
    private final Request request;
    private final int timeout;
    private volatile Response  response;

    private volatile long sendTime;

    private final Lock lock = new ReentrantLock();
    private final Condition done = lock.newCondition();
    public DefaultFuture(int seqId,Request request,int timeout) {
        this.seqId = seqId;
        this.request = request;
        this.timeout = timeout>0?timeout:10000;
        FUTURES.put(seqId, this);
    }


    public void send(){
        this.sendTime = System.currentTimeMillis();
    }

    public static void received(Response response) {
        DefaultFuture future = FUTURES.remove(response.getSeqId());
        if (future != null) {
            future.doReceived(response);
        } else {
            System.err.println("The timeout response finally returned at "
                    + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date())
                    + ", response " + response);
        }
    }

    private void doReceived(Response res) {
        lock.lock();
        try {
            response = res;
            if (done != null) {
                done.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public Object get() throws Exception {
        return get(timeout);
    }

    public Object get(int timeout) throws Exception {
        if (timeout <= 0) {
            timeout = 10000;
        }
        if (!isDone()) {
            long start = System.currentTimeMillis();
            lock.lock();
            try {
                while (!isDone()) {
                    done.await(timeout, TimeUnit.MILLISECONDS);
                    if (isDone() || System.currentTimeMillis() - start > timeout) {
                        break;
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
            if (! isDone()) {
                throw new Exception("not down time out");
            }
        }
        return returnFromResponse();
    }


    private Object returnFromResponse() throws Exception {
        Response res = response;
        if (res == null) {
            throw new Exception("response cannot be null");
        }
        if (res.getStatus() == Response.OK) {
            return res.getData();
        }
        if (res.getStatus() == Response.TIMEOUT) {
            throw new Exception("time out");
        }
        if (res.getStatus() == Response.EXCEPTION) {
            throw (Exception) res.getData();
        }
        throw new Exception("error ,res.status="+res.getStatus());
    }

    public boolean isDone() {
        return response != null;
    }


}
