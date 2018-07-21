package io.apicollab.uploader.fs;

public class UploaderException extends RuntimeException {

    private static final long serialVersionUID = -1427996904257692145L;

    public UploaderException(String message) {
        super(message);
    }

    public UploaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
