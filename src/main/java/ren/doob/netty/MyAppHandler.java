package ren.doob.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.stream.ChunkedStream;
import io.netty.util.CharsetUtil;

import java.io.*;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
/**
 * @author fudali
 * @package ren.doob.netty
 * @class MyAppHandler
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

public class MyAppHandler extends SimpleChannelInboundHandler<FullHttpRequest>{

    private final Servlet servlet;

    private final ServletContext servletContext;

    public MyAppHandler(Servlet servlet) {
        this.servlet = servlet;
        this.servletContext = servlet.getServletConfig().getServletContext();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (!request.decoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }

        if (request.uri().equals("/") || request.uri() == null){
            FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
            response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");

            StringBuffer sb = new StringBuffer();
            FileReader file = new FileReader("D:\\fudali1\\doob\\src\\main\\webapp\\index.html");
            try {
                BufferedReader reader = new BufferedReader(file);
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                file.close();
            }

            response.content().writeBytes(Unpooled.copiedBuffer(sb, CharsetUtil.UTF_8));

            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        }

        MockHttpServletRequest servletRequest = createServletRequest(request);
        MockHttpServletResponse servletResponse = new MockHttpServletResponse();

        this.servlet.service(servletRequest, servletResponse);

        HttpResponseStatus status = HttpResponseStatus.valueOf(servletResponse.getStatus());
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status);

        for (String name : servletResponse.getHeaderNames()) {
            for (Object value : servletResponse.getHeaderValues(name)) {
                response.headers().addObject(name , value);
            }
        }

        // Write the initial line and the header.
        ctx.write(response);

        InputStream contentStream = new ByteArrayInputStream(servletResponse.getContentAsByteArray());

        // Write the content.
        ChannelFuture writeFuture = ctx.write(new ChunkedStream(contentStream));
        writeFuture.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        if (ctx.channel().isActive()) {
            sendError(ctx, INTERNAL_SERVER_ERROR);
        }
        ctx.close();
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status);
        response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
        response.content().writeBytes(Unpooled.copiedBuffer(
                "Failure: " + status.toString() + "\r\n",
                CharsetUtil.UTF_8));

        System.out.println("http chuli shibai");

        // Close the connection as soon as the error message is sent.
        ctx.write(response).addListener(ChannelFutureListener.CLOSE);
    }

    private MockHttpServletRequest createServletRequest(FullHttpRequest httpRequest) {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(httpRequest.uri()).build();

        System.out.println(httpRequest.uri());

        MockHttpServletRequest servletRequest = new MockHttpServletRequest(this.servletContext);
        servletRequest.setRequestURI(uriComponents.getPath());
        servletRequest.setPathInfo(uriComponents.getPath());
        servletRequest.setMethod(httpRequest.method().toString());

        System.out.println(uriComponents.getPath() +"--->"+httpRequest.method().toString());

        if (uriComponents.getScheme() != null) {
            servletRequest.setScheme(uriComponents.getScheme());
        }
        if (uriComponents.getHost() != null) {
            servletRequest.setServerName(uriComponents.getHost());
        }
        if (uriComponents.getPort() != -1) {
            servletRequest.setServerPort(uriComponents.getPort());
        }

        for (CharSequence name : httpRequest.headers().names()) {
            for (CharSequence value : httpRequest.headers().getAll(name)) {
                servletRequest.addHeader(name.toString(), value.toString());
            }
        }

        servletRequest.setContent(httpRequest.content().array());

        try {
            if (uriComponents.getQuery() != null) {
                String query = UriUtils.decode(uriComponents.getQuery(), "UTF-8");
                servletRequest.setQueryString(query);
            }

            for (Entry<String, List<String>> entry : uriComponents.getQueryParams().entrySet()) {
                for (String value : entry.getValue()) {
                    servletRequest.addParameter(
                            UriUtils.decode(entry.getKey(), "UTF-8"),
                            UriUtils.decode(value, "UTF-8"));
                }
            }
        }
        catch (UnsupportedEncodingException ex) {
            // shouldn't happen
        }

        return servletRequest;
    }

}
