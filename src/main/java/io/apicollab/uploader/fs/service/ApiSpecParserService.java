package io.apicollab.uploader.fs.service;

import io.apicollab.uploader.fs.Constants;
import io.apicollab.uploader.fs.UploaderException;
import io.apicollab.uploader.fs.dto.ApiDTO;
import io.swagger.models.Swagger;
import io.swagger.parser.OpenAPIParser;
import io.swagger.parser.SwaggerParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Returns metadata from a parsed API specification file
 */
@UtilityClass
@Slf4j
class ApiSpecParserService {

    private static final String APP_ID_ATTRIBUTE = "x-app-id";

    public static ApiDTO parse(String spec) {
        if (StringUtils.isBlank(spec)) {
            throw new UploaderException(Constants.API_SPEC_EMPTY);
        }
        spec = spec.trim();
        ApiDTO result;
        if (spec.contains("openapi:") || spec.contains("openapi\"")) {
            // OAS Spec
            result = parseOAS(spec);
        } else {
            result = parseSwagger(spec);
        }
        return result;
    }

    private ApiDTO parseOAS(String oasString) {
        SwaggerParseResult result = new OpenAPIParser().readContents(oasString, null, null);
        if (result.getOpenAPI() == null || !result.getMessages().isEmpty()) {
            log.error(result.getMessages().toString());
            throw new UploaderException(Constants.API_SPEC_PARSE_FAILURE);
        }
        OpenAPI openAPI = result.getOpenAPI();
        Info info = openAPI.getInfo();
        // Get app id
        String appId = null;
        if (info.getExtensions() != null) {
            appId = String.valueOf(info.getExtensions().get(APP_ID_ATTRIBUTE));
        }
        if ("null".equals(appId)) {
            appId = null;
        }
        return ApiDTO.builder()
                .applicationId(appId).build();
    }

    private ApiDTO parseSwagger(String swaggerString) {
        Swagger swagger = new SwaggerParser().parse(swaggerString);
        if (swagger == null) {
            throw new UploaderException(Constants.API_SPEC_PARSE_FAILURE);
        }
        io.swagger.models.Info info = swagger.getInfo();
        if (info == null) {
            throw new UploaderException(Constants.API_SPEC_PARSE_FAILURE);
        }
        // Get app id
        String appId = info.getVendorExtensions().get(APP_ID_ATTRIBUTE) == null
                ? null : String.valueOf(info.getVendorExtensions().get(APP_ID_ATTRIBUTE));
        return ApiDTO.builder()
                .applicationId(appId).build();
    }

}
