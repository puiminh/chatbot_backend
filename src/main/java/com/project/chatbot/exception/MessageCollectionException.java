package com.project.chatbot.exception;

public class MessageCollectionException extends Exception{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public MessageCollectionException(String message) {
        super(message);
    }
    public static String NotFoundException(String id) {
        return "Message with "+ id +" not found";
    }
    public static String SomeCustomMessage() {
        return ".....";
    }


}
