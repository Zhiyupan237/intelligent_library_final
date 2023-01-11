package com.example.intelligent_library;

import java.util.Date;

public class borrowRecord {
    private String useraccount;
    private int booK_id;
    private int book_image;
    private Date borrow_time;
    private Date return_time;

    public borrowRecord() {

    }

    public borrowRecord(String useraccount, int booK_id, int book_image, Date borrow_time, Date return_time){
        this.useraccount = useraccount;
        this.booK_id = booK_id;
        this.book_image = book_image;
        this.borrow_time = borrow_time;
        this.return_time = return_time;
    }

    public String getUseraccount(){return useraccount;}

    public void setUseraccount(String useraccount){this.useraccount=useraccount;}

    public int getBooK_id(){return booK_id;};

    public void setBooK_id(int id){this.booK_id=booK_id;}

    public int getBook_image() {
        return book_image;
    }

    public void setBook_image(int book_image) {
        this.book_image = book_image;
    }

    public Date getBorrow_time(){return borrow_time;}

    public void setBorrow_time(Date borrow_time){this.borrow_time=borrow_time;}

    public Date getReturn_time(){return return_time;}

    public void setReturn_time(Date return_time){this.return_time =return_time;}


}
