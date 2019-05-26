import endpoints.MessageResource;
import endpoints.UserResource;
import endpoints.security.AuthenticationFilter;
import endpoints.AuthenticationEndpoint;
import endpoints.ForumEndpoint;
import endpoints.security.CorsFilter;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(UserResource.class);
        resources.add(MessageResource.class);
        resources.add(AuthenticationFilter.class);
        resources.add(AuthenticationEndpoint.class);
        resources.add(CorsFilter.class);
        resources.add(ForumEndpoint.class);
        resources.add(ObjectMapperContextResolver.class);
    }
}
