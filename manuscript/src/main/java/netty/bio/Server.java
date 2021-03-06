package netty.bio;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by lei on 2020/12/10.
 */
//@Slf4j
public class Server {
    //默认端口号
    private static int DEFAULT_PORT = 7777;
    //单例的ServerSocket
    private static ServerSocket serverSocket;

    public static void start() throws IOException{
        start(DEFAULT_PORT);
    }
    public synchronized static void start(int port) throws IOException{
        if(serverSocket !=null) return;
        try{
            serverSocket = new ServerSocket(port);
            //log.info("服务器已启动，端口号："+port);
            while (true){
                Socket socket = serverSocket.accept();
                new Thread(new ServerHandler(socket)).start();
            }
        }finally {
            if(serverSocket !=null){
                serverSocket.close();
                serverSocket = null;
            }
        }
    }

}
