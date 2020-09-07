package ru.iliketobreathe.restaurantvote.util.exception;

public class LateVoteException extends RuntimeException {
    public LateVoteException(String message) {
        super(message);
    }
}