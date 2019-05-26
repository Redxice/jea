package endpoints.interceptors;

import dto.UserDto;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

public class logger  {
    private final static Logger LOGGER = Logger.getLogger(testInterceptor.class.getName());
    @AroundInvoke
    public Object interceptorMethod(InvocationContext ictx) throws Exception{
        Object[] parameters = ictx.getParameters();
        if (parameters.length > 0 && parameters[0] instanceof String) {
            String param = (String) parameters[0];
            LOGGER.info("User deleted forum"+param);
        }
        return ictx.proceed();
    }
}
