package org.daniel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class Library {
    private final String bookFilePath;
    private final String userFilePath;

    public Library(String bookFilePath, String userFilePath) {
        this.bookFilePath = bookFilePath;
        this.userFilePath = userFilePath;
    }

    public void runTerminal() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        int userId = -1;
        String userName = "Not Logged In";

        //noinspection InfiniteLoopStatement
        while (true) {
            System.out.print(userName + ">");
            String[] input = reader.readLine().split(" ");
            switch (input[0]) {
                case "new":
                    if (userId != 0) {
                        System.out.println("Insufficient Permission");
                        break;
                    }
                    if (input[1].equals("user")) {
                        System.out.println(newUser(input[2]));
                    }
                    break;
                case "loan":
                    if (Integer.parseInt(input[3]) == userId || userId == 0) {
                        System.out.println(loan(Integer.parseInt(input[1]), Integer.parseInt(input[3])));
                        break;
                    }
                    System.out.println("Insufficient Permission");
                    break;
                case "return":
                    System.out.println(bookReturn(Integer.parseInt(input[1])));
                    break;
                case "report":
                    switch (input[1]) {
                        case "user":
                            userReport(input[2]);
                            break;
                        case "timesLoaned":
                            timesLoaned();
                            break;
                        case "loanedBooks":
                            loanedBooks();
                            break;
                    }
                    break;
                case "login":
                    userId = Integer.parseInt(input[1]);
                    if (userId == 0) {
                        userName = "admin";
                    } else {
                        User[] users = FileUtility.readUsers(userFilePath);
                        int finalUserId = userId;
                        User user = Arrays.stream(users).filter(u -> u.getId() == finalUserId).findFirst().orElse(null);
                        try {
                            userName = Objects.requireNonNull(user).getName();
                        } catch (Exception e) {
                            System.out.println("User could not be found");
                        }
                    }
                    break;
            }
        }

    }

    private void loanedBooks() {
        Book[] books = FileUtility.readBooks(bookFilePath);
        Book[] loaned = Arrays.stream(books).filter(b -> b.getLocation() != 0).toArray(Book[]::new);
        if (loaned.length == 0) {
            System.out.println("No books are loaned");
        }
        for (Book b : loaned) {
            System.out.println(b.getId() + ":" + b.getTitle() + " is loaned to " + b.getLocation());
        }
    }

    private void timesLoaned() {
        Book[] books = FileUtility.readBooks(bookFilePath);
        books = Arrays.stream(books).sorted(Comparator.comparing(Book::getTimesLoaned).reversed()).toArray(Book[]::new);
        for (Book b : books) {
            System.out.println(b.getTimesLoaned() + "  |  " + b.getTitle());
        }
    }

    private void userReport(String userId) {
        Book[] books = FileUtility.readBooks(bookFilePath);
        books = Arrays.stream(books).filter(b -> b.getLocation() == Integer.parseInt(userId)).toArray(Book[]::new);
        for (Book b : books) {
            System.out.println(b.getTitle());
        }

    }

    private String newUser(String name) {
        User[] users = FileUtility.readUsers(userFilePath);
        ArrayList<User> userList = new ArrayList<>(Arrays.asList(users));
        int newId = 1 + userList.stream().mapToInt(User::getId).max().orElse(0);
        userList.add(new User(newId, name));
        FileUtility.write(userList.toArray(), userFilePath);
        return String.format("`%s` has joined the library", name);
    }

    private String loan(int bookId, int userId) {
        Book[] books = FileUtility.readBooks(bookFilePath);
        Book book = Arrays.stream(books).filter(b -> b.getId() == bookId).findFirst().orElse(null);
        User[] users = FileUtility.readUsers(userFilePath);
        User user = Arrays.stream(users).filter(u -> u.getId() == userId).findFirst().orElse(null);
        // TODO Make these exceptions instead of passing strings around
        if (book == null) {
            return "That book doesn't exist!";
        }
        if (user == null) {
            return "That user doesn't exist!";
        }
        if (book.getLocation() != 0) {
            return "That book is already out on loan!";
        }
        book.incrementTimesLoaned();
        book.setLocation(userId);
        FileUtility.write(books, bookFilePath);
        return String.format("`%s` has been loaned to `%s`", book.getTitle(), user.getName());
    }


    private String bookReturn(int bookId) {
        Book[] books = FileUtility.readBooks(bookFilePath);
        Book book = Arrays.stream(books).filter(b -> b.getId() == bookId).findFirst().orElse(null);
        if (Objects.requireNonNull(book).getLocation() == 0) {
            return "That book is already at the library";
        }
        book.setLocation(0);
        FileUtility.write(books, bookFilePath);
        return String.format("`%s` successfully returned", book.getTitle());
    }
}
