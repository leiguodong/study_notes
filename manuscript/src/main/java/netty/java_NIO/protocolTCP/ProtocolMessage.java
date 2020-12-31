package netty.java_NIO.protocolTCP;

/**
 * 自定义协议消息类
 * 用于封装消息
 * Created by lei on 2020/12/31.
 */
public class ProtocolMessage {
    private int len;
    private byte[] bytes;

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
}
