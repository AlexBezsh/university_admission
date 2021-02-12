package com.bezshtanko.university_admission.transfer;

import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import com.bezshtanko.university_admission.model.user.UserRole;
import com.bezshtanko.university_admission.model.user.UserStatus;
import com.bezshtanko.university_admission.model.user.User;
import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UserDTO {

    private Long id;
    private String fullName;
    private String email;
    private UserStatus status;
    private Set<UserRole> roles;
    private String city;
    private String region;
    private String education;
    private List<Enrollment> enrollments;

    public UserDTO(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.status = user.getStatus();
        this.roles = user.getRoles();
        this.city = user.getCity();
        this.region = user.getRegion();
        this.education = user.getEducation();
        this.enrollments = user.getEnrollments();
    }

}
