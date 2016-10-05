package com.example.gandhi.notebook;

/**
 * Created by Gandhi on 5/13/2016.
 */
public class Note {
    private String title, message;
    private long noteId, dateCreatedMilli;
    private Category category;

    public enum Category{PERSONAL, TECHNICAL, QUOTE, FINANCE}

    public Note(String title, String message, Category category) {
        this.title = title;
        this.message = message;
        this.noteId = 0;
        this.dateCreatedMilli = 0;
        this.category = category;
    }

    public Note(String title, String message, long noteId, long dateCreatedMilli, Category category) {
        this.title = title;
        this.message = message;
        this.noteId = noteId;
        this.dateCreatedMilli = dateCreatedMilli;
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Category getCategory() {
        return category;
    }

    public long getId() {
        return noteId;
    }

    public long getDate() {
        return dateCreatedMilli;
    }

    public String toString(){
        return "ID: " + noteId + " Title: " + title + " Message: " + message + " IconID: " + category.name()
                + " Date: ";
    }

    public int getAssociatedDrawable(){
        return categoryToDrawable(category);
    }

    public static int categoryToDrawable(Category noteCategory){
        switch (noteCategory){
            case PERSONAL:
                return R.drawable.p;
            case TECHNICAL:
                return R.drawable.t;
            case FINANCE:
                return R.drawable.f;
            case QUOTE:
                return R.drawable.q;
        }
        return R.drawable.p;
    }
}
