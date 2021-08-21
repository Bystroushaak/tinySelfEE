package eu.rfox.tinySelfEE.vm.primitives;

public class InvalidParametersException extends Exception {
    public InvalidParametersException() {
    }

    public InvalidParametersException(String message) {
        super(message);
    }
}
