package chapter4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * netty框架使用阻塞io
 * */
public class NettyOioServer {

    public static void main(String[] args) throws Exception {
        NettyOioServer nettyOioServer = new NettyOioServer();
        nettyOioServer.server(1234);
    }


    public void server(int port) throws Exception{
        final ByteBuf buf = Unpooled.unreleasableBuffer(Unpooled.copiedBuffer(
                "Hi!\r\n", Charset.forName("UTF-8")
        ));
        EventLoopGroup group = new OioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(OioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        protected void initChannel(SocketChannel ch) throws Exception {

                            ch.pipeline().addLast(new ChannelHandlerAdapter(){

                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    ByteBuf in = (ByteBuf) msg;
                                    System.out.println("Server received msg: " + in.toString(CharsetUtil.UTF_8));
                                }

                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    ctx.writeAndFlush(buf.duplicate())
                                            .addListener(ChannelFutureListener.CLOSE);
                                }
                            });
                        } //initChannel
                    }); //childHandler
            ChannelFuture f = bootstrap.bind().sync();
            f.channel().closeFuture().sync();

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            group.shutdownGracefully().sync();
        }


    }
}
