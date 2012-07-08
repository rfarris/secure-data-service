package org.slc.sli.api.ldap;

import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;

/**
 * LDAPTemplate mapper for converting User object from the LDAP user context.
 * 
 * @author dliu
 * 
 */
public class UserContextMapper implements ContextMapper {
    @Override
    public Object mapFromContext(Object ctx) {
        DirContextAdapter context = (DirContextAdapter) ctx;
        User user = new User();
        user.setFirstName(context.getStringAttribute("givenName"));
        user.setLastName(context.getStringAttribute("sn"));
        user.setUid(context.getStringAttribute("uid"));
        user.setEmail(context.getStringAttribute("mail"));
        user.setHomeDir(context.getStringAttribute("homeDirectory"));
        
        // TODO figure out consistent ways to set user password with either plain text or MD5 hash
        // user.setPassword(context.getStringAttribute("userPassword"));

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
                    String key = pairArray[0].trim();
                    String value = pairArray[1].trim();
                    if (key.equals("tenant")) {
                        user.setTenant(value);
                    } else if (key.equals("edOrg")) {
                        user.setEdorg(value);
                    }
                }
            }
        }
        return user;
    }
    
}
