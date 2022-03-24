package com.example.library_2;

import java.util.HashMap;
import java.util.Map;

public class Book {

    static Map<String, String> genre = new HashMap<String, String>();
    static {
        genre.put("A. C. Пушкин", "Роман в стихах");
        genre.put("Ф. М. Достоевский", "Психологическая проза");
        genre.put("М. В. Ломоносов", "Русская ода");
        genre.put("Н. М. Карамзин", "Синтементалистическая проза");
    }

    String title;
    String author;
    String genre_of;
    int year;
    int coverId;

    public Book(String title, String author, int year, int coverId){
        this.title = title;
        this.author = author;
        this.year = year;
        this.coverId = coverId;
        if (genre.containsKey(author)){
            genre_of= genre.get(author);
        } else {
            genre_of = "Неизвестно";
        }

    }

    @Override
    public String toString() {
        return title + "/" + author + "/" + year + "/" + coverId + "/";
    }
}
