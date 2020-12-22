package netty.bio.Tomcat;

import netty.bio.Tomcat.http.MyServlet;

import java.io.FileInputStream;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by lei on 2020/12/16.
 */
public class MyTomcat {
    //j2ee标准
    //Servlet
    //Request response

    //配置好启动端口，默认8080 ServerSocket ip:localhost
    //2、配置web.xml 自己写的Servlet继承HttpServlet
    // servlet-name servlet-class url-pattern
    //3、读取配置 url-pattern 和 Servlet建立一个映射关系  Map servletMapping
    //4、HTTP请求，发送的数据就是字符串，有规律的字符串（http协议）
    //5、从协议内容中拿到url,把相应的Servlet用反射进行实例化
    //6、调用实例化对象的service()方法，执行具体的逻辑doGET() doPost()
    //7、Request(inputStream的封装)/response（outputStream的封装）
    private int port =8080;
    private ServerSocket serverSocket;
    private Map<String,MyServlet> servletMap = new HashMap<>();
    private Properties webxml = new Properties();

    private void init(){
        try {
            FileInputStream webxmlPath = new FileInputStream("web.properties");
            webxml.load(webxmlPath);
            for(Object k:webxml.keySet()){
                String key = k.toString();
               // if()
               // String servletName = k.
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
