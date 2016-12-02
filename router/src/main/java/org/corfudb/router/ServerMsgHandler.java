package org.corfudb.router;

import io.netty.channel.ChannelHandlerContext;

import java.lang.annotation.Annotation;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/** This class automatically routes messages to methods on an IServer
 * which are annotated with the @ServerHandler(msgtype) annotation.
 *
 * Created by mwei on 11/23/16.
 */
public class ServerMsgHandler<M extends IRoutableMsg<T>, T>
    extends AbstractMsgHandler<M,T> {

    private final IServer<M,T> server;

    /** Construct a new instance of ServerMsgHandler. */
    public ServerMsgHandler(IServer<M,T> server) {
        this.server = server;
    }

    /** Handle an incoming routable message.
     *
     * @param message   The message to handle.
     * @param ctx       The channel handler context.
     * @return          True, if the message was handled.
     *                  False otherwise.
     */
    @SuppressWarnings("unchecked")
    public boolean handle(M message, IChannel<M> ctx) {
        if (handlerMap.containsKey(message.getMsgType())) {
            M ret = handlerMap.get(message.getMsgType()).handle(message, ctx);
            if (ret != null) {
                if (message instanceof IRespondableMsg) {
                    server.getRouter().sendResponse(ctx, (IRespondableMsg)message,
                            (IRespondableMsg)ret);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Add a handler to this message handler.
     *
     * @param messageType The type of CorfuMsg this handler will handle.
     * @param handler     The handler itself.
     * @return This handler, to support chaining.
     */
    @Override
    public ServerMsgHandler<M, T> addHandler(T messageType, Handler handler) {
        super.addHandler(messageType, handler);
        return this;
    }

    /**
     * Generate handlers for a particular client.
     *
     * @param caller              The context that is being used. Call MethodHandles.lookup() to obtain.
     * @param o                   The object that implements the client.
     * @param annotationType
     * @param typeFromAnnoationFn @return
     */
    @Override
    public <A extends Annotation> ServerMsgHandler<M,T> generateHandlers(MethodHandles.Lookup caller, Object o, Class<A> annotationType, Function<A, T> typeFromAnnoationFn) {
        super.generateHandlers(caller, o, annotationType, typeFromAnnoationFn);
        return this;
    }
}
