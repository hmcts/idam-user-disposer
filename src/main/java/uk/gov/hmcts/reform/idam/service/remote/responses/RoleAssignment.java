package uk.gov.hmcts.reform.idam.service.remote.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class RoleAssignment {
    String id;
    String actorIdType;
    String actorId;
    String roleType;
    String roleName;
    String classification;
    String grantType;
    String roleCategory;
    Boolean readOnly;
}
