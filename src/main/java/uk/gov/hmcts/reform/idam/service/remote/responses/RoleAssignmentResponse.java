package uk.gov.hmcts.reform.idam.service.remote.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RoleAssignmentResponse {

    @JsonProperty("roleAssignmentResponse")
    private List<RoleAssignment> roleAssignments;


}
