package interceptors;

import dto.UserDto;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class testInterceptor {
    @AroundInvoke
    public Object interceptorMethod(InvocationContext ictx) throws Exception{
        System.out.println("hello");
        return new UserDto();
    }
}
