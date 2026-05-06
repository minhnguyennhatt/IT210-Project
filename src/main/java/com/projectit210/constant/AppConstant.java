package com.projectit210.constant;

/**
 * Hằng số chung của ứng dụng
 */
public final class AppConstant {

    private AppConstant() {}

    // Session/Request attribute keys
    public static final String CURRENT_USER = "currentUser";
    public static final String CURRENT_USER_ID = "currentUserId";

    // Thời gian tối thiểu trước buổi tư vấn để có thể hủy (giờ)
    public static final int CANCEL_HOURS_BEFORE = 24;

    // Các giá trị performance level
    public static final String PERFORMANCE_EXCELLENT = "Xuất sắc";
    public static final String PERFORMANCE_GOOD = "Giỏi";
    public static final String PERFORMANCE_AVERAGE = "Khá";
    public static final String PERFORMANCE_BELOW_AVERAGE = "Trung bình";
    public static final String PERFORMANCE_POOR = "Yếu";
}
