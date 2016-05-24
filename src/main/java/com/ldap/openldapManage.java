package com.ldap;

import com.qingting.model.Context;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * Created by root on 1/28/16.
 */
public class openldapManage implements iManage {
    private LdapContext ldapCtx;
    private String base;
    private boolean isConnected;
    private SearchControls constraints;

    public openldapManage() {

        this.isConnected = false;
        constraints = new SearchControls();

    }

    public boolean checkConnected() {
        return this.isConnected;
    }

    public boolean login(Context loginCtx){

        String ldapURL = "ldap://" + loginCtx.getStrUrl();
        this.base = loginCtx.getStrBase();
        String ou = loginCtx.getStrOgnizationUnit();
        String adminDN;
        if (ou.isEmpty()){
            adminDN = "CN=" + loginCtx.getStrName() + "," + base;
        }else {
            adminDN = "CN=" + loginCtx.getStrName() + "," + ou + "," + base;
        }

        String password= loginCtx.getStrPassword();

        Properties env = new Properties();


        env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(javax.naming.Context.PROVIDER_URL, ldapURL);
        env.put(javax.naming.Context.SECURITY_AUTHENTICATION, "simple");
        env.put(javax.naming.Context.SECURITY_PRINCIPAL, adminDN);
        env.put(javax.naming.Context.SECURITY_CREDENTIALS, password);

        try {
            ldapCtx = new InitialLdapContext(env, null);
            isConnected = true;
            System.out.println("connect success");
        } catch (NamingException e) {
            e.printStackTrace();

        }

        return isConnected;
    }

    public void createUser(User user) {
        String displayname;
        if (!user.getLastname().isEmpty()){
            displayname = user.getLastname() + user.getName();
        }else {
            displayname = user.getName();
        }
        String dn = "CN=" + user.getName();
        if (user.getOrgnization().isEmpty()) {
            dn = dn + "," + base;
        } else {
            dn = dn + "," + user.getOrgnization() + "," + base;
        }

        int uID = getMaxUid() + 1;

        String sID = String.valueOf(uID);

        Attributes personAttributes = new BasicAttributes();
        Attribute objclassSet = new BasicAttribute("objectClass");
        objclassSet.add("top");
        objclassSet.add("posixAccount");
        objclassSet.add("inetOrgPerson");

        personAttributes.put(objclassSet);

        personAttributes.put("uid", dn);
        personAttributes.put("cn", user.getName());

        personAttributes.put("sn", user.getName());

        personAttributes.put("givenName", user.getName());
        personAttributes.put("displayname", displayname);
        personAttributes.put("uidNumber", sID);
        personAttributes.put("gidNumber", "100");
        personAttributes.put("homeDirectory", "/home/users/" + user.getName());
        if(!user.getPwd().isEmpty()){
            personAttributes.put("userPassword", user.getPwd());
        }


        try {
            ldapCtx.bind(dn, null, personAttributes);
        } catch (NamingException e) {
           e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private int getMaxUid() {
        int MaxUidNumber = 1000;
        NamingEnumeration en;
        try {
            constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
            en = ldapCtx.search(base, "objectClass=inetOrgPerson", constraints);
            while (en != null && en.hasMoreElements()) {
                Object obj = en.nextElement();
                if (obj instanceof SearchResult) {
                    SearchResult si = (SearchResult) obj;

                    Attributes attrs = si.getAttributes();

                    if (attrs != null) {
                        for (NamingEnumeration ae = attrs.getAll(); ae.hasMoreElements(); ) {
                            Attribute attr = (Attribute) ae.next();
                            String attrId = attr.getID();
                            for (Enumeration vals = attr.getAll(); vals.hasMoreElements(); ) {
                                Object o = vals.nextElement();

                                if (attrId.equals("uidNumber")) {
                                    String strUid;
                                    if (o instanceof byte[])
                                        strUid = new String((byte[]) o);
                                    else
                                        strUid = (String) o;
                                    int num = Integer.parseInt(strUid);
                                    if (MaxUidNumber <= num) {
                                        MaxUidNumber = num;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (NamingException e) {
            System.out.println(e.getMessage());
        }
        return MaxUidNumber;
    }

    public void deleteUser(String name, String ou) {
        String dn = "CN=" + name;
        if (ou.isEmpty()) {
            dn = dn + "," + base;
        } else {
            dn = dn + "," + ou + "," + base;
        }
        try {
            ldapCtx.unbind(dn);
        } catch (NamingException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean createOu(String name) {
        Attributes personAttributes = new BasicAttributes();
        Attribute objclassSet = new BasicAttribute("objectClass");
        objclassSet.add("top");
        objclassSet.add("organizationalUnit");
        personAttributes.put(objclassSet);
        String dn = name + "," + base;
        try {
            ldapCtx.bind(dn, null, personAttributes);
        } catch (NamingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void updateUserAttribute(User user)  {

        String name             = user.getName();
        String orgnization      = user.getOrgnization();
        String title            = user.getTitle();
        String officeroom       = user.getOfficeRoom();
        String sn               = user.getName();
        String displayname      = user.getLastname() + name;
        String city             = user.getCity();
        String province         = user.getProvince();
        String streetaddress    = user.getStreetAddress();
        String homephone        = user.getHomePhone();
        String mobile           = user.getMobile();
        String description      = user.getDescription();
        String mail             = user.getMail();
        String password         = user.getPwd();
        User oldUer             = getUserByName(name, orgnization);
        List<ModificationItem> itemList = new ArrayList<ModificationItem>();
        if ( !"".equals(name)){
            ModificationItem itemGivenName      = new ModificationItem( DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("givenName", name) );
            ModificationItem itemDisplayName    = new ModificationItem( DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("displayname", displayname) );

            itemList.add(itemGivenName);
            itemList.add(itemDisplayName);
        }

        if ( !"".equals(description)){
            ModificationItem itemDescription    = new ModificationItem( DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("description", description) );
            itemList.add(itemDescription);
        }else if (!"".equals(oldUer.getDescription())){
            ModificationItem itemDescription    = new ModificationItem( DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("description") );
            itemList.add(itemDescription);
        }

        if ( !"".equals(title)){
            ModificationItem itemTitle          = new ModificationItem( DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("title", title) );
            itemList.add(itemTitle);
        }else if (!"".equals(oldUer.getTitle())){
            ModificationItem itemTitle          = new ModificationItem( DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("title") );
            itemList.add(itemTitle);
        }

        if ( !"".equals(city)){
            ModificationItem itemCity           = new ModificationItem( DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("l", city) );
            itemList.add(itemCity);
        }else if (!"".equals(oldUer.getCity())){
            ModificationItem itemCity           = new ModificationItem( DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("l") );
            itemList.add(itemCity);
        }

        if ( !"".equals(province)){
            ModificationItem itemProvince       = new ModificationItem( DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("st", province) );
            itemList.add(itemProvince);
        }else if (!"".equals(oldUer.getProvince())){
            ModificationItem itemProvince       = new ModificationItem( DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("st") );
            itemList.add(itemProvince);
        }

        if ( !"".equals(streetaddress)){
            ModificationItem itemStreetaddress  = new ModificationItem( DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("streetaddress", streetaddress) );
            itemList.add(itemStreetaddress);
        }else if (!"".equals(oldUer.getStreetAddress())){
            ModificationItem itemStreetaddress  = new ModificationItem( DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("streetaddress") );
            itemList.add(itemStreetaddress);
        }

        if ( !"".equals(homephone)){
            ModificationItem itemHomephone      = new ModificationItem( DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("homephone", homephone) );
            itemList.add(itemHomephone);
        }else if (!"".equals(oldUer.getHomePhone())){
            ModificationItem itemHomephone      = new ModificationItem( DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("homephone") );
            itemList.add(itemHomephone);
        }

        if ( !"".equals(mobile)){
            ModificationItem itemMobile         = new ModificationItem( DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("mobile", mobile) );
            itemList.add(itemMobile);
        }else if (!"".equals(oldUer.getMobile())){
            ModificationItem itemMobile         = new ModificationItem( DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("mobile") );
            itemList.add(itemMobile);
        }

        if ( !"".equals(sn)){
            ModificationItem itemSn             = new ModificationItem( DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("sn", sn) );
            itemList.add(itemSn);
        }else if (!"".equals(oldUer.getLastname())){
            ModificationItem itemSn             = new ModificationItem( DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("sn") );
            itemList.add(itemSn);
        }

        if ( !"".equals(officeroom)){
            ModificationItem itemOfficeroom     = new ModificationItem( DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("physicaldeliveryofficename", officeroom) );
            itemList.add(itemOfficeroom);
        }else if (!"".equals(oldUer.getOfficeRoom())){
            ModificationItem itemOfficeroom     = new ModificationItem( DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("physicaldeliveryofficename") );
            itemList.add(itemOfficeroom);
        }

        if ( !"".equals(mail) ){
            ModificationItem itemMail           = new ModificationItem( DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("mail", mail) );
            itemList.add(itemMail);
        }else if (!"".equals(oldUer.getMail())){
            ModificationItem itemMail           = new ModificationItem( DirContext.REMOVE_ATTRIBUTE, new BasicAttribute("mail") );
            itemList.add(itemMail);
        }

        if ( !"".equals(password)){
            ModificationItem itemPwd = new ModificationItem( DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userPassword", password) );
            itemList.add(itemPwd);
        }

        ModificationItem[] ItemArray = itemList.toArray(new ModificationItem[itemList.size()]);

        String dn;
        if ( !"".equals(orgnization) )
        {
            dn = "CN=" + user.getName() + "," + orgnization + "," + base;
        }else {
            dn = "CN=" + user.getName() + "," + base;
        }

        try {
            ldapCtx.modifyAttributes(dn, ItemArray);
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void getUserList() {
        NamingEnumeration en;
        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
        try {
            en = ldapCtx.search(base, "objectClass=inetOrgPerson", constraints);
            while (en != null && en.hasMoreElements()) {
                Object obj = en.nextElement();
                if (obj instanceof SearchResult) {
                    SearchResult si = (SearchResult) obj;
                    Attributes attrs = si.getAttributes();
                    if (attrs != null) {
                        for (NamingEnumeration ae = attrs.getAll(); ae.hasMoreElements(); ) {
                            Attribute attr = (Attribute) ae.next();
                            String attrId = attr.getID();
                            for (Enumeration vals = attr.getAll(); vals.hasMoreElements(); ) {
                                Object o = vals.nextElement();
                                if (attrId.equals("objectGUID") ||
                                        attrId.equals("objectSid")
                                        ) {
                                    continue;
                                }
                                System.out.print(attrId + ":   ");

                                if (o instanceof byte[])
                                    System.out.println(new String((byte[]) o));
                                else
                                    System.out.println(o);
                            }
                        }
                    }
                }
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public void getGroupList(){
        NamingEnumeration en;
        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
        try {
            en = ldapCtx.search(base, "objectClass=organizationalUnit", constraints);
            while (en != null && en.hasMoreElements()) {
                System.out.println("============================================================");
                Object obj = en.nextElement();
                if (obj instanceof SearchResult) {
                    SearchResult si = (SearchResult) obj;
                    Attributes attrs = si.getAttributes();
                    if (attrs != null){
                        for (NamingEnumeration ae = attrs.getAll(); ae.hasMoreElements(); ) {
                            Attribute attr = (Attribute) ae.next();
                            String attrId = attr.getID();
                            for (Enumeration vals = attr.getAll(); vals.hasMoreElements(); ) {
                                Object o = vals.nextElement();
                                if (attrId.equals("ou")){
                                    String strOU;
                                    if (o instanceof byte[]){
                                        strOU = new String((byte[]) o);
                                    }
                                    else{
                                        strOU = (String)o;
                                    }
                                    System.out.println(attrId + " : " + strOU);
                                }
                            }
                        }
                    }
                }
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }

    }

    public void getObjList(String ou){

        NamingEnumeration en;
        if (!ou.isEmpty()){
            base = ou + "," + base;
        }

        constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        try {
            en = ldapCtx.search(base, "objectClass=*", constraints);
            while (en != null && en.hasMoreElements()) {

                Object obj = en.nextElement();
                if (obj instanceof SearchResult) {
                    SearchResult si = (SearchResult) obj;
                    Attributes attrs = si.getAttributes();
                    if (attrs != null){
                        System.out.println("============================================================");
                        for (NamingEnumeration ae = attrs.getAll(); ae.hasMoreElements(); ) {
                            Attribute attr = (Attribute) ae.next();
                            String attrId = attr.getID();

                            for (Enumeration vals = attr.getAll(); vals.hasMoreElements(); ) {
                                Object o = vals.nextElement();
                                if (attrId.equals("ou")){
                                    String strOU;
                                    if (o instanceof byte[]){
                                        strOU = new String((byte[]) o);
                                    }
                                    else{
                                        strOU = (String)o;
                                    }
                                    System.out.println(attrId + " : " + strOU);
                                    continue;
                                }else if(attrId.equals("cn")){
                                    String strCN;
                                    if (o instanceof byte[]){
                                        strCN = new String((byte[]) o);
                                    }
                                    else{
                                        strCN = (String)o;
                                    }
                                    System.out.println(attrId + " : " + strCN);
                                    continue;
                                }
                            }

                        }
                    }
                }
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }

    }

    public User getUserByName(String name, String ou){
        User user = new User(name,ou);
        NamingEnumeration en;
        String searchBase;
        if (ou.isEmpty()){
            searchBase = base;
        }else{
            searchBase = ou + "," + base;
        }
        constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        try {
            String filter = "(&(objectClass=inetOrgPerson)(cn=" + name + "))";
            en = ldapCtx.search(searchBase, filter, constraints);
            while (en != null && en.hasMoreElements()) {

                Object obj = en.nextElement();
                if (obj instanceof SearchResult) {
                    SearchResult si = (SearchResult) obj;
                    Attributes attrs = si.getAttributes();
                    if (attrs != null){
                        System.out.println("============================================================");
                        for (NamingEnumeration ae = attrs.getAll(); ae.hasMoreElements(); ) {
                            Attribute attr = (Attribute) ae.next();
                            String key = attr.getID();
                            String value = "";

                            for (Enumeration vals = attr.getAll(); vals.hasMoreElements(); ) {
                                Object o = vals.nextElement();

                                if (o instanceof byte[]){
                                    value = new String((byte[]) o);
                                }
                                else{
                                    value = (String)o;
                                }
                                System.out.println(key + " : " + value);
                            }

                            if (key.contains("objectGUID") || attr.getID().contains("objectSid")) {
                                continue;
                            }

                            if (key.contentEquals("name")) {
                                user.setName(value);
                                continue;
                            }

                            if (key.contentEquals("company")) {
                                user.setCompany(value);
                                continue;
                            }

                            if (key.contentEquals("department")) {
                                user.setDepartment(value);
                                continue;
                            }

                            if (key.contentEquals("title")) {
                                user.setTitle(value);
                                continue;
                            }

                            if (key.contentEquals("description")) {
                                user.setDescription(value);
                                continue;
                            }

                            if (key.contentEquals("sn")) {
                                user.setLastname(value);
                                continue;
                            }

                            if (key.contentEquals("l")) {
                                user.setCity(value);
                                continue;
                            }

                            if (key.contentEquals("st")) {
                                user.setProvince(value);
                                continue;
                            }

                            if (key.contentEquals("streetAddress")) {
                                user.setStreetAddress(value);
                                continue;
                            }

                            if (key.contentEquals("homePhone")) {
                                user.setHomePhone(value);
                                continue;
                            }

                            if (key.contentEquals("mobile")) {
                                user.setMobile(value);
                                continue;
                            }

                            if (key.contentEquals("physicalDeliveryOfficeName")) {
                                user.setOfficeRoom(value);
                                continue;
                            }

                            if (key.contentEquals("mail")) {
                                user.setMail(value);
                                continue;
                            }
                        }
                    }
                }
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }

        return user;
    }
}
