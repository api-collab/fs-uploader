package io.apicollab.uploader.fs.service;

import io.apicollab.uploader.fs.Constants;
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

    public static ApiDTO parse(String spec) {
        if (StringUtils.isBlank(spec)) {
            throw new RuntimeException(Constants.API_SPEC_EMPTY);
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
            throw new RuntimeException(Constants.API_SPEC_PARSE_FAILURE);
        } else {
            OpenAPI openAPI = result.getOpenAPI();
            Info info = openAPI.getInfo();
            // Check basic information
            String appId = info.getExtensions() == null ? null : info.getExtensions().get("x-app-id") == "null"
                    ? null : String.valueOf(info.getExtensions().get("x-app-id"));
            return ApiDTO.builder()
                    .applicationId(appId).build();
        }
    }

    private ApiDTO parseSwagger(String swaggerString) {
        Swagger swagger = new SwaggerParser().parse(swaggerString);
        if (swagger == null) {
            throw new RuntimeException(Constants.API_SPEC_PARSE_FAILURE);
        } else {
            io.swagger.models.Info info = swagger.getInfo();
            if (info == null) {
                throw new RuntimeException(Constants.API_SPEC_PARSE_FAILURE);
            }
            String appId = info.getVendorExtensions().get("x-app-id") == null
                    ? null : String.valueOf(info.getVendorExtensions().get("x-app-id"));
            return ApiDTO.builder()
                    .applicationId(appId).build();
        }
    }

}
