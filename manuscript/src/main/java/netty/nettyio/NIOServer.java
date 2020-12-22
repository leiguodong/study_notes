package netty.nettyio;

import org.apache.ibatis.annotations.SelectKey;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by lei on 2020/12/15.
 */
public class NIOServer
{
    private static final int BUF_SIZE = 1024;
    private static final int  PORT = 8080;
    private static final int TIMEOUT =- 3000;

    public static void main(String[] args) {
        selector();
    }

    private static void selector() {
        Selector selector = null;
        ServerSocketChannel ssc = null;
        try{
            //开启选择器可以理解为 select poll epoll的封装
            selector = Selector.open();
            ssc = ServerSocketChannel.open();
            ssc.socket().bind(new InetSocketAddress(PORT));
            ssc.configureBlocking(false);
            ssc.register(selector,SelectionKey.OP_ACCEPT);
            while (true){
                if(selector.select(TIMEOUT)==0){
                    System.out.println("当前无任务");
                    continue;
                }
                Iterator<SelectionKey> iter = (Iterator<SelectionKey>) selector.selectedKeys();
                while (iter.hasNext()){
                    SelectionKey key = iter.next();
                    // 判断是 读 写 连接
                    if(key.isAcceptable()){
                        handleAccept(key);
                    }
                    if(key.isConnectable()){
                        System.out.println("已连接");
                    }
                    if(key.isReadable()){
                        handleRead(key);
                    }
                    if(key.isWritable()){
                        handleWrite(key);
                    }
                }
            }

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (selector != null) {
                    selector.close();
                }
                if(ssc !=null){
                    ssc.close();
                }
            } catch (IOException ee) {
                ee.printStackTrace();
            }
        }
    }

    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel sc = (SocketChannel)key.channel();
        ByteBuffer buf = (ByteBuffer)key.attachment();
        long byteRead = sc.read(buf);
        while (byteRead >0){
            buf.flip();
            while (buf.hasRemaining()){
                System.out.println((char) buf.get());
            }
            System.out.println("  ");
            buf.clear();
            byteRead = sc.read(buf);
        }
        if(byteRead ==-1){
            sc.close();
        }
    }

    private static void handleAccept(SelectionKey key) throws IOException{
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel sc = serverSocketChannel.accept();
        sc.configureBlocking(false);
        sc.register(key.selector(), SelectionKey.OP_READ, ByteBuffer.allocateDirect(BUF_SIZE));
    }
    private static void handleWrite(SelectionKey key) throws IOException{
        ByteBuffer buf = (ByteBuffer)key.attachment();
        buf.flip();
        SocketChannel sc = (SocketChannel)key.channel();
        while (buf.hasRemaining()){
            sc.write(buf);
        }
        buf.compact();

    }
}
