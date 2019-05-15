package models;/*
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

import com.warrenstrange.googleauth.GoogleAuthenticatorKey;

import javax.persistence.*;
import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.security.Key;
import java.util.ArrayList;
import java.util.List;


@Entity
@NamedQueries({
        @NamedQuery(name = "User.findOne", query = "select u from User u where u.id = :id"),
        @NamedQuery(name = "User.getAll", query = "select u from User u"),
        @NamedQuery(name ="User.validate", query = "select u from User u where u.password = :password and u.name= :name"),
        @NamedQuery(name ="User.findByName", query = "select u from User u where  u.name = :name"),
        @NamedQuery(name ="User.deleteById", query = "delete from User u where  u.id = :id")
    }
)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User {

    @Id
    @GeneratedValue
    private long id;
    /**
     * TODO: replace name with mail in checks
     */
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String name;
    private String password;
    private int level;
    private int hoursPlayed;
    @OneToMany(mappedBy="owner", orphanRemoval=true)
    private List<Forum> forums = new ArrayList<>();
    @OneToMany(mappedBy="owner", orphanRemoval=true)
    private List<Message> messages = new ArrayList<>();
    private String twoFactorAuthKey;
    private boolean twoFactorEnabled;
    public User() {
    }

    public User(String name, String password,String email,boolean twoFactorEnabled) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.twoFactorEnabled = twoFactorEnabled;
    }

    @Override
    public String toString() {
        return "id :"+this.id+" name :"+this.name;
    }

    public long getId(){
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getHoursPlayed() {
        return hoursPlayed;
    }

    public void setHoursPlayed(int hoursPlayed) {
        this.hoursPlayed = hoursPlayed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Forum> getForums() {
        return forums;
    }

    public void setForums(List<Forum> forums) {
        this.forums = forums;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }


    public void setTwoFactorAuthKey(String twoFactorAuthKey) {
        this.twoFactorAuthKey = twoFactorAuthKey;
    }

    public String getTwoFactorAuthKey() {
        return twoFactorAuthKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public void setTwoFactorEnabled(boolean twoFactorEnabled) {
        this.twoFactorEnabled = twoFactorEnabled;
    }
}
