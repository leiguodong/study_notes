package netty.io;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Bio 服务端
 */
public class Bio {
    public static void main(String[] args) throws Exception {
//        bio();
//        nioMulteThread();
//        start();
        Selector selector1 = Selector.open();
        Selector selector2 = Selector.open();
        Selector selector3 = Selector.open();
        //java nio start select poll epoll
        SocketMultiplexingThreads socketMultiplexingThreads = new SocketMultiplexingThreads();
        socketMultiplexingThreads.initServer();
        NioThread nt1 = new NioThread(socketMultiplexingThreads.selector1,2);
        NioThread nt2 = new NioThread(socketMultiplexingThreads.selector2);
        NioThread nt3 = new NioThread(socketMultiplexingThreads.selector3);
        nt1.start();
        try{
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        nt2.start();
        nt3.start();
        //end
    }


    /**
     * 阻塞io模型
     * @throws Exception
     */
    public static void  bio() throws Exception{
        //监听一个端口
        /**
         * socket() = 6fd
         * bind(6fd,9090)
         * listen(6fd)
         */
        ServerSocket server  = new ServerSocket(9090);
        System.out.println("监听9090端口");
        //阻塞接受连接请求
        /**
         * accept(6fd) ==>7fd
         */
        while (true){
            System.out.println("阻塞等待。。。。。");
        Socket socket = server.accept(); //阻塞
        System.out.println("获取到请求。。。。。");
        InputStream in = socket.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        //如果没有数据 阻塞
        System.out.println(reader.readLine());//阻塞

        }
    }
    /**
     * nio模型  non-blocking 非阻塞io
     * channel bytebuffer selector
     * 资源浪费 每次循环都得遍历 clients 即使没有任何连接信息
     */
    public  static void nioSingleThread() throws Exception{
        //定义一个channel数组
        LinkedList<SocketChannel> clients = new LinkedList<>();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(9090));
        ssc.configureBlocking(false);//设置为非阻塞
        while (true){
            //thread1 start一个单独一个线程执行
            Thread.sleep(1000);
            SocketChannel client = ssc.accept();//不会阻塞

            if(client == null){
                System.out.println("null....");
            }else{
                client.configureBlocking(false);
                int port = client.socket().getPort();
                System.out.println("client...port:"+port);
                clients.add(client);
            }
            //thread1 end
            //thread2 start
            ByteBuffer buffer  = ByteBuffer.allocate(4096);//可以在堆内 可以堆外
            for(SocketChannel socketChannel:clients){
                int num = socketChannel.read(buffer);// >0 -1 0  -1会导致空轮训 cpu100%
                if(num>0){
                    buffer.flip();
                    byte[] aaa = new byte[buffer.limit()];
                    buffer.get(aaa);
                    String b = new String(aaa);
                    System.out.println(socketChannel.socket().getPort()+" : "+b);
                    buffer.clear();
                }
            }
            //thead2 end
        }
    }
    /**
     * nio 多路复用器io
     * select  poll epoll kqueue
     */
    private static  ServerSocketChannel serverSocketChannel = null;
    private static Selector selector = null;
    private static  int port = 9090;
    public  static  void initServer(){
        try{
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.bind(new InetSocketAddress(port));
            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        }catch (Exception E){
            E.printStackTrace();
        }

    }
    public static  void start(){
        initServer();
        System.out.println("服务器启动了。。。。。。");
        try{
            while (true){
                //timeout 0代表阻塞等待 设置等待时间
                    System.out.println("空轮训------");
                while (selector.select(2000)>0){
                    System.out.println("有事件-------");
                    Set<SelectionKey> selectionKeySet = selector.selectedKeys();//从多路复用器取出有效的keys
                    Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        //处理 accept 连接 read 读 write 写
                        /**
                         * 服务端接收客户端连接事件SelectionKey.OP_ACCEPT(16)
                         * 客户端连接服务端事件SelectionKey.OP_CONNECT(8)
                         * 读事件SelectionKey.OP_READ(1)
                         * 写事件SelectionKey.OP_WRITE(4)
                         */
                        if(key.isAcceptable()){
                            acceptHandler(key);
                        }else if(key.isReadable()){
                            readHandler(key);
                        }
// else if(key.isWritable()){
//                            writeHandler(key);
//                        }else if(key.isConnectable()){
//                            conneciontHandler(key);
//                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void acceptHandler(SelectionKey key){
        try{
            ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
            SocketChannel client = ssc.accept();
            client.configureBlocking(false);
            ByteBuffer buffer = ByteBuffer.allocate(8192);
            client.register(selector,SelectionKey.OP_READ,buffer);
            System.out.println("------------");
            System.out.println("新客户端："+client.getRemoteAddress());
            System.out.println("------------");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void readHandler(SelectionKey key){
        try{
            SocketChannel ssc = (SocketChannel)key.channel();
            ByteBuffer buffer = (ByteBuffer)key.attachment();

            buffer.clear();
            int read = 0;
            try{
                while(true){
                    read = ssc.read(buffer);
                    if(read>0){
                        buffer.flip();
                        while (buffer.hasRemaining()){
                            ssc.write(buffer);
                            System.out.println("写出---"+buffer.toString());
                        }
                        buffer.clear();
                    }else if(read ==0){
                        break;
                    }else{// -1 close_wait  bug 死循环 cpu 100%  有可能客户端已经断开连接
                        ssc.close();
                       break;
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }catch (Exception e){

        }
    }

    public static class SocketMultiplexingThreads{
        private static ServerSocketChannel serverSocketChannel1 = null;
        private static Selector selector1 = null;
        private static Selector selector2 = null;
        private static Selector selector3 = null;
        private static int port1 = 9091;
        public void initServer() {
            try{
                serverSocketChannel1 = ServerSocketChannel.open();
                serverSocketChannel1.configureBlocking(false);
                serverSocketChannel1.bind(new InetSocketAddress(port1));

                selector1 = Selector.open();
                selector2 = Selector.open();
                selector3 = Selector.open();

                serverSocketChannel1.register(selector1,SelectionKey.OP_ACCEPT);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private static class NioThread extends Thread{

        Selector selector = null;
        static int selectors = 0;
        int id = 0;
        static BlockingDeque<SocketChannel>[] queue;
        static AtomicInteger idx = new AtomicInteger();
        NioThread(Selector sel,int n){
            this.selector = sel;
            this.selectors = n;
            queue = new LinkedBlockingDeque[selectors];
            for(int i = 0;i<n;i++){
                queue[i] = new LinkedBlockingDeque<>();
            }
            System.out.println("Boss 启动");
        }
        NioThread(Selector sel){
            this.selector = sel;
            id = idx.getAndIncrement()%selectors;
            System.out.println("Worker 启动"+id+" 启动");
        }
        @Override
        public void run(){
            try{
                while (true){
                    while (this.selector.select(10)>0){
                        System.out.println("有事件-------");
                        Set<SelectionKey> selectionKeySet = selector.selectedKeys();//从多路复用器取出有效的keys
                        Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                        while (iterator.hasNext()){
                            SelectionKey key = iterator.next();
                            iterator.remove();
                            //处理 accept 连接 read 读 write 写
                            //boss 线程只处理连接请求
                            if(key.isAcceptable()){
                                    acceptHandler(key);
                            }
// else if(key.isReadable()){
//                                //              readHandler(key);
//                            }
                        }
                    }
                    if(!queue[id].isEmpty()){
                        ByteBuffer buffer = ByteBuffer.allocate(8192);
                        SocketChannel client = queue[id].take();
                        client.register(selector,SelectionKey.OP_READ,buffer);
                        System.out.println("----------");
                        System.out.println("新客户端："+client.socket().getPort()+"分配到worker:"+id);
                        System.out.println("----------");

                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        public static void acceptHandler(SelectionKey key){
            try{
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                SocketChannel client = ssc.accept();
                client.configureBlocking(false);
                int num = idx.getAndIncrement()%selectors;
                queue[num].add(client);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    /**
     * netty io同步 处理异步Futuer
     */
}
