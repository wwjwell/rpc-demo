package rpc;

import java.io.Serializable;

/**
 * Created by wwj on 2017/8/17.
 */
public class Request implements Serializable {
    private static final long serialVersionUID = -1774002570160608366L;
    private int seqId;
    private Invocation data;

    public int getSeqId() {
        return seqId;
    }

    public void setSeqId(int seqId) {
        this.seqId = seqId;
    }

    public Invocation getData() {
        return data;
    }

    public void setData(Invocation data) {
        this.data = data;
    }
}
