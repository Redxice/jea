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

import javax.inject.Inject;

import dao.UserDao;
import dto.RegisterDto;
import dto.UserDto;
import endpoints.security.Secured;
import helpers.RestHelper;
import endpoints.interceptors.testInterceptor;
import models.User;
import services.UserService;

import javax.ejb.Stateless;
import javax.interceptor.Interceptors;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.*;


import java.util.ArrayList;
import java.util.List;

@Stateless
@Path("users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    private UserDao userDao;
    @Inject
    private UserService userService;

    // method to test an interceptor.
    @GET
    @Interceptors(testInterceptor.class)
    @Path("/test")
    public UserDto getRandom(@Context UriInfo uriInfo) {
        Link self = Link.fromUriBuilder(uriInfo.getAbsolutePathBuilder())
                .rel("self").type("GET").build();
        UserDto userDto = new UserDto();
        userDto.setName("Paul");
        userDto.getLinks().add(self.toString());
        return userDto;
    }

    /**
     * Not secured since there is no sensitive content in UserDto
     */
    @GET
    public Response all(@Context UriInfo uriInfo) {
        List<User> users = userDao.getAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = new UserDto(user, uriInfo);
            userDtos.add(userDto);
        }
        return Response.ok(userDtos).build();
    }

    /**
     * @param httpServletRequest
     * @param uriInfo
     * @return the created user as userDTO
     */
    @POST
    public Response save(@Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo, RegisterDto registerDto) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        try {
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
                List<String> usernameAndPassword = RestHelper.getUsernameAndPassword(authorizationHeader);
                User new_user = userDao.save(new User(usernameAndPassword.get(0), usernameAndPassword.get(1),registerDto.getEmail(),registerDto.isTwoFactorEnabled()));
                return Response.ok(new UserDto(new_user, uriInfo)).build();
            }
        } catch (Exception e) {
            return Response.status(409).build();
        }

        return Response.status(400).build();
    }

    /**
     * This method updates the user
     * The user can only update himself
     */
    @PUT
    @Secured
    public Response update(UserDto userDto, @Context HttpServletRequest httpServletRequest, @Context UriInfo uriInfo) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            String name = RestHelper.getUsernameFromJWT(authorizationHeader);
            if (name.equals(userDto.getName())) {
                User user = userDao.findUserByName(name);
                user.setName(userDto.getName());
                user.setHoursPlayed(userDto.getHoursPlayed());
                user.setLevel(userDto.getLevel());
                User updated_user = userDao.update(user);
                return Response.ok(new UserDto(updated_user, uriInfo)).build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();

    }

    @GET
    @Path("{id}")
    public Response getUser(@PathParam("id") Long id, @Context UriInfo uriInfo) {
        User found_user = userService.find(id);
        if (found_user != null) {
            return Response.ok(new UserDto(found_user, uriInfo)).build();
        }
        return Response.status(404).build();
    }

    @DELETE
    @Secured
    @Path("{id}")
    public Response delete(@Context HttpServletRequest httpServletRequest, @PathParam("id") Long id) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
            String name = RestHelper.getUsernameFromJWT(authorizationHeader);
            Long id_user = userDao.findUserByName(name).getId();
            if (id_user.equals(id)) {
                User user = userDao.find(id);
                userDao.delete(user);
                return Response.noContent().build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

    @GET
    @Path("name/{name}")
    public Response getUserByName(@PathParam("name") String name) {
        User found_user = userDao.findUserByName(name);
        if (found_user != null) {
            return Response.ok(new UserDto(found_user)).build();
        }
        return Response.status(404).build();
    }

}
