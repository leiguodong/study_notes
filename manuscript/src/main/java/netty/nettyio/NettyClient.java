package netty.nettyio;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by lei on 2020/12/14.
 */
//public class NettyClient implements Runnable{
    //@Override
//    public void run() {
//        EventLoopGroup group = new NioEventLoopGroup();
//        try{
//            Bootstrap bootstrap = new Bootstrap();
//            bootstrap.group(group);
//            bootstrap.channel(NioSocketChannel.class)
//                    .option(ChannelOption.TCP_NODELAY,true)
//                    .handler(new ChannelInitializer<SocketChannel>() {
//                        @Override
//                        protected void initChannel(SocketChannel socketChannel) throws Exception {
//                            ChannelPipeline pipeline = socketChannel.pipeline();
//                            pipeline.addLast("frameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4))
//                        }
//                    })
//        }
//    }
//}
