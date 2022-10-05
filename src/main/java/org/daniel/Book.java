package org.daniel;

public class Book {
    private int id;
    private String title;
    private String author;
    private String genre;
    private String subGenre;
    private String publisher;
    private int timesLoaned;
    private int location;

    public Book(int id, String title, String author, String genre, String subGenre, String publisher, int timesLoaned, int location) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.subGenre = subGenre;
        this.publisher = publisher;
        this.timesLoaned = timesLoaned;
        this.location = location;
    }

    public void incrementTimesLoaned() {
        timesLoaned++;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getSubGenre() {
        return subGenre;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getTimesLoaned() {
        return timesLoaned;
    }

    public int getLocation() {
        return location;
    }
}