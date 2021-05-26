package dubbo.my_dubbo.tomcat;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lei on 2021/5/26.
 */
public class HttpServerHandler {
    public void handler(HttpServletRequest request, HttpServletResponse response){
        //处理逻辑
        try {
            Invocation invocation = JSONObject.parseObject(request.getInputStream(),Invocation.class);
            invocation.getInterfaceName();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
