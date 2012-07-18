/*
 * Copyright 2012 Shared Learning Collaborative, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.slc.sli.sandbox.idp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.CollectingAuthenticationErrorCallback;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Component;

/**
 * Returns users that can be logged in as
 *
 */
@Component
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @Autowired
    LdapTemplate ldapTemplate;

    @Value("${sli.simple-idp.userSearchAttribute}")
    private String userSearchAttribute;

    @Value("${sli.simple-idp.userObjectClass}")
    private String userObjectClass;

    @Value("${sli.simple-idp.groupSearchAttribute}")
    private String groupSearchAttribute;

    @Value("${sli.simple-idp.groupObjectClass}")
    private String groupObjectClass;

    public UserService() {
    }

    UserService(String userSearchAttribute, String userObjectClass, String groupSearchAttribute, String groupObjectClass) {
        this.userSearchAttribute = userSearchAttribute;
        this.userObjectClass = userObjectClass;
        this.groupSearchAttribute = groupSearchAttribute;
        this.groupObjectClass = groupObjectClass;
    }

    /**
     * Holds user information
     */
    public static class User {
        String userId;
        List<String> roles;
        Map<String, String> attributes;

        public User() {
        }

        public User(String userId, List<String> roles, Map<String, String> attributes) {
            this.userId = userId;
            this.roles = roles;
            this.attributes = attributes;
        }

        public String getUserId() {
            return userId;
        }

        public List<String> getRoles() {
            return roles;
        }

        public Map<String, String> getAttributes() {
            return attributes;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }

    /**
     * Authenticate the user for the given realm and return back a User object with
     * all user attributes and roles loaded. If sandbox mode then it ensures the realm provided
     * matches with the realm stored in the users LDAP description attribute (Realm=xxx)
     *
     * @param realm
     * @param userId
     * @param password
     * @return
     * @throws AuthenticationException
     */
    public User authenticate(String realm, String userId, String password) throws AuthenticationException {
        CollectingAuthenticationErrorCallback errorCallback = new CollectingAuthenticationErrorCallback();
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", userObjectClass)).and(new EqualsFilter(userSearchAttribute, userId));
        DistinguishedName dn = new DistinguishedName("ou=" + realm);
        boolean result = ldapTemplate.authenticate(dn, filter.toString(), password, errorCallback);
        if (!result) {
            Exception error = errorCallback.getError();
            if (error == null) {
                LOG.error("SimpleIDP Authentication Eception");
            } else {
                LOG.error("SimpleIDP Authentication Exception", error);
            }
            throw new AuthenticationException(error);
        }

        User user = getUser(realm, userId);
        user.roles = getUserGroups(realm, userId);
        
        return user;
    }

    /**
     *
     * @param realm
     *            The realm under which the user exists
     * @param userId
     *            The id of the user
     * @return
     */
    public User getUser(String realm, String userId) {
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", userObjectClass)).and(new EqualsFilter(userSearchAttribute, userId));
        DistinguishedName dn = new DistinguishedName("ou=" + realm);
        User user = (User) ldapTemplate.searchForObject(dn, filter.toString(), new PersonContextMapper());
        user.userId = userId;
        return user;
    }
    
    public void updateUser(String realm, User user, String newPass){
    	LOG.info("Update User with Fake Password");
    	user.attributes.put("Password", newPass);
    	//DirContextAdapter context = new DirContextAdapter(buildUserDN(realm, user));
    	DirContextAdapter context = (DirContextAdapter) ldapTemplate.lookupContext(buildUserDN(realm, user.getAttributes().get("uid")));
    	mapUserToContext(context, user, newPass);
    	ldapTemplate.modifyAttributes(context);
    }

    /**
     *
     * @param realm
     *            The realm under which the user exists
     * @param userId
     *            The id of the user
     * @return List of roles assigned to this user
     */
    public List<String> getUserGroups(String realm, String userId) {
        DistinguishedName dn = new DistinguishedName("ou=" + realm);
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", groupObjectClass)).and(
                new EqualsFilter(groupSearchAttribute, userId));
        @SuppressWarnings("unchecked")
        List<String> groups = ldapTemplate.search(dn, filter.toString(), new GroupContextMapper());
        return groups;
    }

    private DistinguishedName buildUserDN(String realm, String uid) {
    	DistinguishedName dn = new DistinguishedName("cn=" + uid + ",ou=people,ou=" + realm);
    	return dn;
    }
    
    private void mapUserToContext(DirContextAdapter context, User user, String newPass) {
         context.setAttributeValues("objectclass", new String[] { "inetOrgPerson", "posixAccount", "top" });
         context.setAttributeValue("givenName", user.getAttributes().get("givenName"));
         context.setAttributeValue("sn", user.getAttributes().get("sn"));
         context.setAttributeValue("uid", user.getAttributes().get("uid"));
         context.setAttributeValue("uidNumber", user.getAttributes().get("uidNumber"));
         context.setAttributeValue("gidNumber", user.getAttributes().get("gidNumber"));
         context.setAttributeValue("cn", user.getAttributes().get("userName"));
         //context.setAttributeValue("mail", user.getAttributes().get("mail"));
         context.setAttributeValue("homeDirectory", user.getAttributes().get("homeDirectory"));
         
         context.setAttributeValue("displayName", "sunsetAdmin");
         context.setAttributeValue("userPassword", newPass);
         
         String description = "";
         if (user.getAttributes().get("tenant") != null) {
             description += "tenant=" + user.getAttributes().get("tenant");
         }
         if (user.getAttributes().get("edOrg") != null) {
             description += ",edOrg=" + user.getAttributes().get("edOrg");
         }
         if(!"".equals(description)) {
             context.setAttributeValue("description", "tenant=" + user.getAttributes().get("tenant") + "," + "edOrg=" + user.getAttributes().get("edOrg"));
         }
 
     }

    /**
     * LDAPTemplate mapper for getting attributes from the person context. Retrieves cn and
     * description, parsing the value of description by line and then by key=value.
     * retrieve pwdMustChange for force password reset
     * @author scole, ychen
     *
     */
    static class PersonContextMapper implements ContextMapper {
        @Override
        public Object mapFromContext(Object ctx) {
            DirContextAdapter context = (DirContextAdapter) ctx;
            User user = new User();
            Map<String, String> attributes = new HashMap<String, String>();
 
            attributes.put("userName", context.getStringAttribute("cn"));
            attributes.put("givenName", context.getStringAttribute("givenName"));
            attributes.put("sn", context.getStringAttribute("sn"));
            attributes.put("uid", context.getStringAttribute("cn"));
            
            attributes.put("uidNumber", context.getStringAttribute("uidNumber"));
            attributes.put("gidNumber", context.getStringAttribute("gidNumber"));

            
            //attributes.put("mail", context.getStringAttribute("mail"));
            attributes.put("homeDirectory", context.getStringAttribute("homeDirectory"));
            
            String emailToken = context.getStringAttribute("displayName");
            if(emailToken==null) emailToken = "";
            attributes.put("emailToken", emailToken);
            
            String description = context.getStringAttribute("description");
            if (description != null && description.length() > 0) {
                String[] pairs;
                if (description.indexOf("\n") > 0) {
                    pairs = description.split("\n");
                } else if (description.indexOf(",") > 0) {
                    pairs = description.split(",");
                } else {
                    pairs = description.split(" ");
                }
                for (String pair : pairs) {
                    pair = pair.trim();
                    String[] pairArray = pair.split("=", 2);
                    if (pairArray.length == 2) {
                        String value = pairArray[1].trim();
                        if (value.length() > 0) {
                            attributes.put(pairArray[0].trim(), value);
                        }
                    }
                }
            }
            user.attributes = attributes;
            return user;
        }
    }

    /**
     * LDAPTemplate mapper for getting group names.
     *
     * @author scole
     *
     */
    static class GroupContextMapper implements ContextMapper {
        @Override
        public Object mapFromContext(Object ctx) {
            DirContextAdapter context = (DirContextAdapter) ctx;
            String group = context.getStringAttribute("cn");
            return group;
        }

    }
}
