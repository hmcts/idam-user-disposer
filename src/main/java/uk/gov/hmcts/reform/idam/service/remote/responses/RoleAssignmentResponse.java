package uk.gov.hmcts.reform.idam.service.remote.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleAssignmentResponse {

    @JsonProperty("roleAssignmentResponse")
    private List<RoleAssignment> roleAssignments;


}
