package interceptors;

import dto.UserDto;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.util.logging.Logger;

public class testInterceptor {
    private final static Logger LOGGER = Logger.getLogger(testInterceptor.class.getName());
    @AroundInvoke
    public Object interceptorMethod(InvocationContext ictx) throws Exception{
        LOGGER.fine("in interceptor");
        return new UserDto();
    }
}
