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

/**
 * Created by mwei on 11/23/16.
 */
public class ClientMsgHandler<M extends IRoutableMsg<T>, T>
    extends AbstractMsgHandler<M,T> {

    /** The client. */
    private AbstractClient client;

    /** Construct a new instance of ClientMsgHandler. */
    public ClientMsgHandler(AbstractClient client) {
        this.client = client;
    }

    /** Handle an incoming message.
     *
     * @param message   The message to handle.
     * @param channel   The channel handler context.
     * @return          True, if the message was handled.
     *                  False otherwise.
     */
    @SuppressWarnings("unchecked")
    public boolean handle(M message, IChannel<M> channel) {
        if (handlerMap.containsKey(message.getMsgType())) {
            handlerMap.get(message.getMsgType()).handle(message, channel);
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
    public AbstractMsgHandler<M, T> addHandler(T messageType, Handler handler) {
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
    public <A extends Annotation> ClientMsgHandler<M,T> generateHandlers(MethodHandles.Lookup caller, Object o, Class<A> annotationType, Function<A, T> typeFromAnnoationFn) {
        super.generateHandlers(caller, o, annotationType, typeFromAnnoationFn);
        return this;
    }


}
