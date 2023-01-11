package com.example.intelligent_library;

public class user {
    private String username;
    private String useraccount;
    private String userpassword;

    public user() {

    }

    public user(String username, String useraccount, String userpassword, int book_image, boolean borrow_state){
        this.username = username;
        this.useraccount = useraccount;
        this.userpassword = userpassword;
    }

    public String getusername() {
        return username;
    }

    public void setbook_name(String username) {
        this.username = username;
    }

    public String getuseraccount() {
        return useraccount;
    }

    public void setuseraccount(String useraccount) {
        this.useraccount = useraccount;
    }


    public String getuserpassword() {
        return userpassword;
    }

    public void setuserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

}
