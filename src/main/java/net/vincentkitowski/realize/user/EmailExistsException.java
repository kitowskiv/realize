package net.vincentkitowski.realize.user;

public class EmailExistsException extends Exception {

    public EmailExistsException(String message) {
        super(message);
    }

}
