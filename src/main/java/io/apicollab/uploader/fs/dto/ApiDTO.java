package io.apicollab.uploader.fs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiDTO implements Serializable {

    private static final long serialVersionUID = -8123598175475245637L;

    @JsonProperty("id")
    private String apiId;
    private String applicationId;
}
