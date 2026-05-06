package com.projectit210.constant;

/**
 * Đường dẫn (URL paths) cho các trang - dùng trong controllers
 */
public final class ApiPath {

    private ApiPath() {}

    // Auth
    public static final String AUTH = "/auth";
    public static final String LOGIN = "/auth/login";
    public static final String REGISTER = "/auth/register";
    public static final String LOGOUT = "/auth/logout";

    // Student
    public static final String STUDENT = "/student";
    public static final String STUDENT_DASHBOARD = "/student/dashboard";
    public static final String STUDENT_BOOK = "/student/book";
    public static final String STUDENT_SESSIONS = "/student/sessions";
    public static final String STUDENT_HISTORY = "/student/academic-history";

    // Lecturer
    public static final String LECTURER = "/lecturer";
    public static final String LECTURER_DASHBOARD = "/lecturer/dashboard";
    public static final String LECTURER_PENDING = "/lecturer/pending-sessions";
    public static final String LECTURER_EVALUATE = "/lecturer/evaluate";

    // Admin
    public static final String ADMIN = "/admin";
    public static final String ADMIN_DASHBOARD = "/admin/dashboard";
    public static final String ADMIN_EQUIPMENTS = "/admin/equipments";
    public static final String ADMIN_BORROWINGS = "/admin/borrowings";

    // Profile
    public static final String PROFILE = "/profile";
}
