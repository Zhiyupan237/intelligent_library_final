package com.example.intelligent_library;

public class bookList {
    private String book_name;
    private String book_author;
    private String book_category;
    private int book_image;
    private boolean borrow_state;

    public bookList() {

    }

    public bookList(String book_name,String book_author,String book_category,int book_image,boolean borrow_state){
        this.book_name = book_name;
        this.book_author = book_author;
        this.book_category = book_category;
        this.book_image = book_image;
        this.borrow_state = borrow_state;
    }

    public String getbook_name() {
        return book_name;
    }

    public void setbook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getbook_author() {
        return book_author;
    }

    public void setbook_author(String book_author) {
        this.book_author = book_author;
    }

    public int getbook_image() {
        return book_image;
    }

    public void setbook_image(int book_image) {
        this.book_image = book_image;
    }

    public String getbook_category() {
        return book_category;
    }

    public void setbook_category(String book_category) {
        this.book_category = book_category;
    }

    public Boolean getborrow_state(){ return borrow_state; }

    public void setborrow_state(Boolean book_state){ this.borrow_state = book_state;}
}
