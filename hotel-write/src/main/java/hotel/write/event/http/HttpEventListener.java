package hotel.write.event.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpEventListener extends SimpleChannelInboundHandler<FullHttpRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        String uri = fullHttpRequest.uri();
        System.out.println("Handling: " + uri+' '+channelHandlerContext.name()+" >"+channelHandlerContext);

        /*
        final Handler handler = WebServer.this.getHandler(request.uri());
        if (handler == null) {
            writeNotFound(ctx, request);
        } else {
            try {
                handler.handle(ctx, request);
            } catch (final Throwable ex) {
                ex.printStackTrace();
                writeInternalServerError(ctx, request);
            }
        }
        */

    }
}
