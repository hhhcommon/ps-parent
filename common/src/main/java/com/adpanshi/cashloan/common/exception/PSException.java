package com.adpanshi.cashloan.common.exception;

/**
 * Created by zsw on 2018/6/23 0023.
 */
public class PSException  extends RuntimeException
{
    public PSException()
    {
    }

    public PSException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public PSException(String message)
    {
        super(message);
    }

    public PSException(Throwable cause)
    {
        super(cause);
    }
}

