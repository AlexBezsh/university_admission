package com.bezshtanko.university_admission;

import com.bezshtanko.university_admission.model.enrollment.Enrollment;
import com.bezshtanko.university_admission.model.enrollment.EnrollmentStatus;
import com.bezshtanko.university_admission.model.faculty.Faculty;
import com.bezshtanko.university_admission.model.faculty.FacultyStatus;
import com.bezshtanko.university_admission.model.mark.Mark;
import com.bezshtanko.university_admission.model.subject.Subject;
import com.bezshtanko.university_admission.model.subject.SubjectType;
import com.bezshtanko.university_admission.model.user.User;
import com.bezshtanko.university_admission.model.user.UserRole;
import com.bezshtanko.university_admission.model.user.UserStatus;
import com.bezshtanko.university_admission.transfer.UserDTO;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestData {

    public static final Subject TEST_SUBJECT_1 = Subject.builder()
            .id(1L)
            .type(SubjectType.EXAM)
            .nameUa("Математика")
            .nameEn("Mathematics")
            .build();

    public static final Subject TEST_SUBJECT_2 = Subject.builder()
            .id(2L)
            .type(SubjectType.SCHOOL)
            .nameUa("Англійська мова")
            .nameEn("English")
            .build();


    public static final Faculty TEST_FACULTY = Faculty.builder()
            .id(1L)
            .nameEn("Faculty")
            .nameUa("Факультет")
            .status(FacultyStatus.ACTIVE)
            .descriptionEn("Description in English")
            .descriptionUa("Опис українською")
            .stateFundedPlaces(20)
            .contractPlaces(30)
            .subjects(Arrays.asList(TEST_SUBJECT_1, TEST_SUBJECT_2))
            .build();

    public static final User TEST_USER = User.builder()
            .id(1L)
            .fullName("User Full Name")
            .email("email@e")
            .password("Strong1")
            .status(UserStatus.ACTIVE)
            .roles(Collections.singleton(UserRole.ENTRANT))
            .city("City")
            .region("Region")
            .education("Education")
            .build();

    public static final String TEST_USER_HASHED_PASSWORD = "$2a$10$NX.Vcv7DWw7LRNA6Qr40c.9kk3.aNtUFijWrxF3upduG9M01K5JpW";


    public static final Mark TEST_MARK_1 = Mark.builder()
            .id(1L)
            .subject(TEST_SUBJECT_1)
            .mark(new BigDecimal("90"))
            .build();


    public static final Mark TEST_MARK_2 = Mark.builder()
            .id(1L)
            .subject(TEST_SUBJECT_2)
            .mark(new BigDecimal("100"))
            .build();


    public static final Enrollment TEST_ENROLLMENT = Enrollment.builder()
            .id(1L)
            .user(TEST_USER)
            .faculty(TEST_FACULTY)
            .status(EnrollmentStatus.NEW)
            .marks(Arrays.asList(TEST_MARK_1, TEST_MARK_2))
            .build();

    static {
        List<Enrollment> enrollments = Collections.singletonList(TEST_ENROLLMENT);
        TEST_FACULTY.setEnrollments(enrollments);
        TEST_USER.setEnrollments(enrollments);
        TEST_ENROLLMENT.setUser(TEST_USER);
        TEST_MARK_1.setEnrollment(TEST_ENROLLMENT);
        TEST_MARK_2.setEnrollment(TEST_ENROLLMENT);
    }

    public static final UserDTO TEST_USER_DTO = new UserDTO(TEST_USER);

}
