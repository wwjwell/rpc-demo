package rpc;

import java.io.Serializable;

/**
 * Created by wwj on 2017/8/17.
 */
public class Response implements Serializable {
    private static final long serialVersionUID = -5361306872921716220L;
    private int seqId;
    private Object data;
    private int status;

    public static final int OK = 0;
    public static final int TIMEOUT = 2;
    public static final int EXCEPTION = 1;
    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
