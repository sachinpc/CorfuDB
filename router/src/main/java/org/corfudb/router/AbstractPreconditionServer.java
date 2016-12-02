package org.corfudb.router;

import lombok.Getter;

import java.util.function.BiFunction;

/**
 * Created by mwei on 11/27/16.
 */
public abstract class AbstractPreconditionServer<M extends IRoutableMsg<T>, T,
        S extends AbstractPreconditionServer<M,T,S>>
        extends AbstractServer<M,T> implements IPreconditionServer<M,T,S> {

    @Getter
    final PreconditionFunction<M,T,S> preconditionFunction;

    public abstract PreconditionServerMsgHandler<M,T> getPreconditionMsgHandler();

    @Override
    final public ServerMsgHandler<M,T> getMsgHandler() {
        return getPreconditionMsgHandler();
    }

    public AbstractPreconditionServer(IServerRouter<M,T> router,
        PreconditionFunction<M,T,S> preconditionFunction) {
        super(router);
        this.preconditionFunction = preconditionFunction;
    }


}
