package com.ldap;

import com.qingting.model.Context;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * Created by root on 9/18/15.
 */
public class AdManage implements iManage{
    private LdapContext ctx;
    private String base;
    private String strDomain;
    private boolean isConnected;
    private SearchControls constraints;
    public AdManage() {

        this.isConnected = false;

    }

    public boolean checkConnected(){
        return this.isConnected;
    }

    public boolean login(Context loginCtx){
        constraints = new SearchControls();
        constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);


        String ldapURL = "ldap://" + loginCtx.getStrUrl();
        this.base = loginCtx.getStrBase();
        String adminDN = "CN=" + loginCtx.getStrName() + "," + "CN=Users," + loginCtx.getStrBase();


        Properties env = new Properties();

        env.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        env.put(javax.naming.Context.PROVIDER_URL, ldapURL);
        env.put(javax.naming.Context.SECURITY_AUTHENTICATION, "simple");
        env.put(javax.naming.Context.SECURITY_PRINCIPAL, adminDN);
        env.put(javax.naming.Context.SECURITY_CREDENTIALS, loginCtx.getStrPassword());

        try {
            ctx = new InitialLdapContext(env, null);
            isConnected = true;
            System.out.println("connect success");
        } catch (NamingException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        strDomain = "";
        String[] dcList = base.split(",");
        for (String aDcList : dcList) {
            String dc = aDcList.split("=")[1];
            strDomain += dc;
            strDomain += ".";
        }

        strDomain = strDomain.substring(0, strDomain.length()-1);

        return isConnected;
    }

    public void updateGroupName(String oldName, String newName){

        String dn = getDnBysAMAccountName(oldName);
        List<ModificationItem> itemList = new ArrayList<ModificationItem>();

        ModificationItem itemDescription    = new ModificationItem( DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("sAMAccountName", newName) );
        itemList.add(itemDescription);
        ModificationItem[] ItemArray = itemList.toArray(new ModificationItem[itemList.size()]);
        try {
            ctx.modifyAttributes(dn, ItemArray);
        } catch (NamingException e) {

        }

    }

    public User getUserByName(String name, String ou){
        User user = new User("","");
        return user;
    }

    public void updateUserAttribute(User user){

    }

    public void getObjList(String ou){


    }

    public String getDnBysAMAccountName(String name){
        String Dn = "";


        String con = "sAMAccountName=" + name;
        NamingEnumeration en = null;
        try {
            en = ctx.search(base, con, constraints);
        } catch (NamingException e) {
            e.printStackTrace();
        }

        Object obj = en.nextElement();
        if (obj instanceof SearchResult) {
            SearchResult si = (SearchResult) obj;

            Attributes attrs = si.getAttributes();
            if (attrs == null) {
                System.out.println("No   attributes ");
            } else {
                for (NamingEnumeration ae = attrs.getAll(); ae.hasMoreElements(); ) {
                    Attribute attr = null;
                    try {
                        attr = (Attribute) ae.next();
                    } catch (NamingException e) {
                        e.printStackTrace();
                    }
                    String attrId = attr.getID();
                    try {
                        for (Enumeration vals = attr.getAll(); vals.hasMoreElements(); ) {
                            Object o = vals.nextElement();
                            if (attrId.equals("distinguishedName")) {
                                Dn = (String) o;
                                break;
                            }
                        }
                    } catch (NamingException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return Dn;
    }

    public void getGroupList(String ou){

        NamingEnumeration en;
        if (!ou.isEmpty()){
            ou = "ou=" + ou;
            base = ou + "," + base;
        }

        constraints.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        try {
            en = ctx.search(base, "objectClass=organizationalUnit", constraints);
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
//                                if (attrId.equals("ou")){
                                    String strOU;
                                    if (o instanceof byte[]){
                                        strOU = new String((byte[]) o);
                                    }
                                    else{
                                        strOU = (String)o;
                                    }
                                    System.out.println(attrId + " : " + strOU);
//                                    getGroupList(strOU);

//                                }
                            }

                        }
                    }
                }
            }
        } catch (NamingException e) {
            e.printStackTrace();
        }

    }

    public void getUserList(){

        NamingEnumeration en = null;
        try {
            //AD
            en = ctx.search(base, "objectClass=user", constraints);
            //OpenLDAP
            //en = ctx.search(base, "objectClass=inetOrgPerson", constraints);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        while (en != null && en.hasMoreElements()) {
            System.out.println("============================================================");
            Object obj = en.nextElement();
            if (obj instanceof SearchResult) {
                SearchResult si = (SearchResult) obj;

//                System.out.println("name:   " + si.getName());

                Attributes attrs = si.getAttributes();
                if (attrs == null) {
                    System.out.println("No   attributes ");
                } else {
                    for (NamingEnumeration ae = attrs.getAll(); ae.hasMoreElements(); ) {
                        Attribute attr = null;
                        try {
                            attr = (Attribute) ae.next();
                        } catch (NamingException e) {
                            e.printStackTrace();
                        }
                        String attrId = attr.getID();


                        try {
                            for (Enumeration vals = attr.getAll(); vals.hasMoreElements(); ) {
                                Object o = vals.nextElement();
                                if (attrId.equals("objectGUID") ||
                                        attrId.equals("objectSid")
                                        ){
                                    continue;
                                }
                                System.out.print(attrId + ":   ");

                                if (o instanceof byte[])
                                    System.out.println(new String((byte[]) o));
                                else
                                    System.out.println(o);
                            }
                        } catch (NamingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                System.out.println(obj);
            }
            System.out.println();
        }
    }

    private byte[] encodePassword(String password) throws UnsupportedEncodingException {
        String newQuotedPassword = "\"" + password + "\"";
        return newQuotedPassword.getBytes("UTF-16LE");
    }

    public void createUser(User user){
        String LastName         = user.getLastname();
        String displayname      = LastName + user.getName();

        String dn = "CN=" + user.getName();
        if (user.getOrgnization().isEmpty()){
            dn =dn + "," + base;
        }else {
            dn =dn + "," + user.getOrgnization() + "," + base;
        }

        String princpalName = displayname + "@" + strDomain;

        Attributes personAttributes = new BasicAttributes();
        Attribute objclassSet = new BasicAttribute("objectClass");
        objclassSet.add("top");
        objclassSet.add("person");
        objclassSet.add("organizationalPerson");
        objclassSet.add("user");

        personAttributes.put(objclassSet);

        personAttributes.put("uid", dn);
        personAttributes.put("cn", user.getName());
        personAttributes.put("sn", "wang");
        personAttributes.put("givenName", user.getName() );
        personAttributes.put("displayname", displayname);
        personAttributes.put( "userPrincipalName", princpalName );
        personAttributes.put( "sAMAccountName", displayname );

        personAttributes.put("homeDirectory", "/home/users/"+user.getName());
        personAttributes.put("userPassword", user.getPwd());
        personAttributes.put("userAccountControl", "66048");

        try {
            ctx.bind(dn, null, personAttributes);
        } catch (NamingException e) {
//            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void deleteUser(String name, String ou){
        String dn = "CN=" + name;

        if (ou.isEmpty()){
            dn =dn + "," + base;
        }else {
            dn =dn + "," + ou + "," + base;
        }

        try {
            ctx.unbind(dn);
        } catch (NamingException e) {
            System.out.println(e.getMessage());
        }

    }

    public void createOu(String name){
        Attributes personAttributes = new BasicAttributes();
        Attribute objclassSet = new BasicAttribute("objectClass");
        objclassSet.add("top");
        objclassSet.add("organizationalUnit");
        personAttributes.put(objclassSet);

        String dn = name + "," + base;

        try {
            ctx.bind(dn, null, personAttributes);
        } catch (NamingException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createGroup(){


    }

    public List<String> getsAMAccountNameList(){

        List<String> nameList = new ArrayList<String>();
        NamingEnumeration en = null;
        try {
            en = ctx.search(base, "objectClass=group", constraints);
        } catch (NamingException e) {
            e.printStackTrace();
        }
        while (en != null && en.hasMoreElements()) {
//            System.out.println("============================================================");
            Object obj = en.nextElement();
            if (obj instanceof SearchResult) {
                SearchResult si = (SearchResult) obj;
                Attributes attrs = si.getAttributes();
                if (attrs == null) {
                    System.out.println("No   attributes ");
                } else {
                    for (NamingEnumeration ae = attrs.getAll(); ae.hasMoreElements(); ) {
                        Attribute attr = null;
                        try {
                            attr = (Attribute) ae.next();
                        } catch (NamingException e) {
                            e.printStackTrace();
                        }
                        String attrId = attr.getID();


                        try {
                            for (Enumeration vals = attr.getAll(); vals.hasMoreElements(); ) {
                                Object o = vals.nextElement();
                                if (attrId.equals("sAMAccountName")){
                                    String name = "";
                                    if (o instanceof byte[])
                                        name = new String((byte[]) o);
                                    else
                                        name = (String)o;
                                    nameList.add(name);
//                                    System.out.println(name);
                                    break;
                                }
                            }
                        } catch (NamingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                System.out.println(obj);
            }
        }

        return nameList;
    }
}
