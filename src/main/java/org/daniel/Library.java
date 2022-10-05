package org.daniel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Library {
    private String bookFilePath;
    private String userFilePath;

    public Library(String bookFilePath, String userFilePath) {
        this.bookFilePath = bookFilePath;
        this.userFilePath = userFilePath;
    }

    public void runTerminal() throws IOException {
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(System.in));
        while (true) {
            System.out.print(">");
            String[] input = reader.readLine().split(" ");
            switch (input[0]) {
                case "new":
                    if (input[1].equals("user")) {
                        System.out.println(newUser(input[2]));
                    }
                    break;
                case "loan":
                    System.out.println(loan(Integer.parseInt(input[1]), Integer.parseInt(input[3])));
                    break;
                case "return":
                    System.out.println(bookReturn(Integer.parseInt(input[1])));
                    break;
                case "report":
                    report(input[1]);
                    break;
            }
        }

    }

    public String newUser(String name) {
        User[] users = FileUtility.readUsers(userFilePath);
        ArrayList<User> userList = new ArrayList<>(Arrays.asList(users));
        int newId = 1 + userList.stream().mapToInt(u -> u.getId()).max().orElse(0);
        userList.add(new User(newId, name));
        FileUtility.write(userList.toArray(), userFilePath);
        return String.format("`%s` has joined the library", name);
    }

    public String loan(int bookId, int userId) {
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
        if (book.getLocation() != 0){
            return "That book is already out on loan!";
        }
        book.incrementTimesLoaned();
        book.setLocation(userId);
        FileUtility.write(books, bookFilePath);
        return String.format("`%s` has been loaned to `%s`", book.getTitle(), user.getName());
    }


    public String bookReturn(int bookId) {
        Book[] books = FileUtility.readBooks(bookFilePath);
        Book book = Arrays.stream(books).filter(b -> b.getId() == bookId).findFirst().orElse(null);
        if(book.getLocation()==0){
            return "That book is already at the library";
        }
        book.setLocation(0);
        FileUtility.write(books, bookFilePath);
        return String.format("`%s` successfully returned",book.getTitle());
    }

    public void report(String type) {

    }

}
