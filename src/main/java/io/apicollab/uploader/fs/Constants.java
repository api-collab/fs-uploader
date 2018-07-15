package io.apicollab.uploader.fs;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Constants {

    public static final String INPUT_FOLDER = "api_input";
    public static final String PROCESSED_FOLDER = "api_processed";
    public static final String ERROR_FOLDER = "api_error";

    public static final String INVALID_FILE_TYPE = "Unsupported file type";
    public static final String CANNOT_READ_FILE_CONTENT = "Cannot read file content";
    public static final String APP_ID_NOT_FOUND = "Application ID not found in api spec";
    public static final String API_SPEC_EMPTY = "API Specification is empty";
    public static final String API_SPEC_PARSE_FAILURE = "Cannot parse API spec";
    public static final String UPLOAD_FAILED = "API Specification upload failed";
}
