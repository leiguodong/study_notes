package netty.bio;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by lei on 2020/12/10.
 */
@Slf4j
public class ServerHandler implements Runnable {
    private Socket socket;
    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            //true 自动刷新
            out = new PrintWriter(socket.getOutputStream(),true);
            String expression;
            String result;
            while (true){
                if((expression = in.readLine())==null) break;
          //      log.info("服务端收到信息："+expression);
//                result = Calculator.cal(expression);
            }

        }catch (Exception e){
            e.printStackTrace();
//            log.error(e.getLocalizedMessage());
        }finally {
        }
        if(out !=null){
            out.close();
        }
        //流关闭会自动关闭socket
        if(socket != null){
            try{
                socket.close();
            }catch (IOException E){
                E.printStackTrace();
            }
            socket = null;
        }
    }
}
