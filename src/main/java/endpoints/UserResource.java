package endpoints;/*
 * Copyright (c) 2017 Payara Foundation and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://github.com/payara/Payara/blob/master/LICENSE.txt
 * See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * The Payara Foundation designates this particular file as subject to the "Classpath"
 * exception as provided by the Payara Foundation in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

import com.sun.jndi.toolkit.url.Uri;
import dao.UserDao;
import dto.UserDto;
import models.User;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import security.*;

@Stateless
@Path("users")
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class UserResource {

    @Inject
    private UserDao userDao;
    @GET
    @Path("/test")
    public UserDto getRandom(@Context UriInfo uriInfo){
        Link self = Link.fromUriBuilder(uriInfo.getAbsolutePathBuilder())
                .rel("self").type("GET").build();
        UserDto userDto = new UserDto();
        userDto.setName("Paul");
        userDto.getLinks().add(self);
        return userDto;
    }
    @GET
    public Response all(@Context UriInfo uriInfo) {
        List<User> users = userDao.getAll();
        List<UserDto> userDtos = new ArrayList<>();
        for(User user : users){
            UserDto userDto = new UserDto(user,uriInfo);
            userDtos.add(userDto);
        }
        return Response.ok(userDtos).build();
    }

    @POST
    public UserDto save(User user, @Context UriInfo uriInfo) {
        User new_user = userDao.save(user);
        return new UserDto(new_user,uriInfo);
    }

    @PUT
    public UserDto update(User user, @Context UriInfo uriInfo) {
        User updated_user= userDao.update(user);
        return new UserDto(updated_user,uriInfo);
    }

    @GET
    @Secured
    @Path("{id}")
    public UserDto getUser(@PathParam("id")Long id, @Context UriInfo uriInfo){
        User found_user= userDao.find(id);
        return new UserDto(found_user,uriInfo);
    }

    @DELETE
    @Secured
    @Path("{id}")
    public void delete(@PathParam("id") Long id) {
        User user = userDao.find(id);
        userDao.delete(user);
    }

}
