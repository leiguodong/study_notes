package netty.nettyio;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;


/**
 * Created by lei on 2020/12/14.
 */
public class NettyServer {
    private static final String IP = "172.0.0.1";
    private static final int port = 6666;
    //定义核心线程数
    private static final int BIZGROUPSIZE =Runtime.getRuntime().availableProcessors()*2;
    private static final int BIZSTHREADSIZE = 100;
    private  static final EventLoopGroup baseGroup = new NioEventLoopGroup(BIZGROUPSIZE);
    private  static final EventLoopGroup workGroup = new NioEventLoopGroup(BIZSTHREADSIZE);

    public static void start() throws Exception{
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture cf = serverBootstrap.group(baseGroup,workGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<Channel>() {
                @Override
                protected void initChannel(Channel channel) throws Exception{
                  ChannelPipeline pipeline = channel.pipeline();
                   pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                   pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                   pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                   pipeline.addLast(new TcpServerhandler());
                 }
        }).bind(IP,port).sync();
        //serverBootstrap.bind(IP,port).sync();
        cf.channel().closeFuture().sync();
        System.out.println("Server start");
    }
    protected static void shutdown(){
        workGroup.shutdownGracefully();
        baseGroup.shutdownGracefully();
    }

    public static void main(String[] args) {
        System.out.println("启动server....");
        try {
            NettyServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            NettyServer.shutdown();
        }
    }

}
