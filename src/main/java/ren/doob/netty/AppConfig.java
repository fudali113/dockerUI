package ren.doob.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * @author fudali
 * @package ren.doob.netty
 * @class AppConfig
 * @date 2016-2-25
 * <p>
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓    Code
 * 　　┏┛┻━━━┛┻┓  is
 * 　　┃　　　　　　　┃  far
 * 　　┃　　　━　　　┃  away
 * 　　┃　┳┛　┗┳　┃  from
 * 　　┃　　　　　　　┃  bug
 * 　　┃　　　┻　　　┃  with
 * 　　┃　　　　　　　┃  the
 * 　　┗━┓　　　┏━┛  animal
 * 　　　　┃　　　┃      protecting
 * 　　　　┃　　　┃神兽保佑,代码无bug
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * <p>
 * ━━━━━━感觉萌萌哒━━━━━━
 */
@Configurable
public class AppConfig {

    public void bind(int port) throws Exception {
        EventLoopGroup group1 = new NioEventLoopGroup() ;
        EventLoopGroup group2 = new NioEventLoopGroup() ;
        try{
            ServerBootstrap sb = new ServerBootstrap();
            sb.group(group1,group2)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new DispatcherServletChannelInitializer());

            ChannelFuture cf = sb.bind(port).sync();//绑定端口，同步等待成功
            cf.channel().closeFuture().sync();//等待服务端监听端口关闭
            System.out.println("netty 绑定端口"+port+"成功！");
        }finally {
            group1.shutdownGracefully();
            group2.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception{
        int port = 3133;
        if(args != null && args.length > 0){
            port = Integer.parseInt(args[0]);
        }
        new AppConfig().bind(port);
    }
}
