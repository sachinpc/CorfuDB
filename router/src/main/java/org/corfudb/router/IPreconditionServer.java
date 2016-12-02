package org.corfudb.router;

import io.netty.channel.ChannelHandlerContext;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by mwei on 11/27/16.
 */
public interface IPreconditionServer<M extends IRoutableMsg<T>, T,
        S extends IPreconditionServer<M,T,S>>
        extends IServer<M,T> {

    @FunctionalInterface
    interface PreconditionFunction<M extends IRoutableMsg<T>,T,
            S extends IPreconditionServer<M,T,S>> {
        boolean check(M message, IChannel<M> channel, S server);
    }

    PreconditionFunction<M,T,S> getPreconditionFunction();
}
