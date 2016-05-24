package com.ldap;

import java.util.Map;


public class User {

    public User(String vName, String vOrgnization) {
        name = vName;
        pwd = "";
        orgnization = vOrgnization;
        description = "";
        title = "";
        officeRoom = "";
        department = "";
        company = "";
        lastname = "";
        city = "";
        province = "";
        streetAddress = "";
        homePhone = "";
        mobile = "";
        mail = "";
        regist = false;
    }

    private Map<String, String> attributes;
    private String name;
    private String pwd;
    private String orgnization;
    private String description;
    private String title;
    private String officeRoom;
    private String department;
    private String company;
    private String lastname;
    private String city;
    private String province;
    private String streetAddress;
    private String homePhone;
    private String mobile;
    private String mail;
    private boolean regist;

    public boolean isRegist() {
        return regist;
    }

    public void setRegist(boolean regist) {
        this.regist = regist;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getOrgnization() {
        return orgnization;
    }

    public void setOrgnization(String orgnization) {
        this.orgnization = orgnization;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOfficeRoom() {
        return officeRoom;
    }

    public void setOfficeRoom(String officeRoom) {
        this.officeRoom = officeRoom;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

}