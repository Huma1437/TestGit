package com.lscarp4.lscarpl4assessments.StudentFiles;

public class MexamsPojo {

    String student_id;
    String exam_status;
    String exam_name;
    String exam_start_date;
    String exam_end_date;
    String exam_response;
    String exam_duration;
    String enrollment_form_required;
    String enrollment_form_status;
    String student_feedback;

    public String getStudent_feedback() {
        return student_feedback;
    }

    public void setStudent_feedback(String student_feedback) {
        this.student_feedback = student_feedback;
    }

    public String getFeedback_form_status() {
        return feedback_form_status;
    }

    public void setFeedback_form_status(String feedback_form_status) {
        this.feedback_form_status = feedback_form_status;
    }

    String feedback_form_status;


    public String getExam_name() {
        return exam_name;
    }

    public void setExam_name(String exam_name) {
        this.exam_name = exam_name;
    }

    public String getExam_start_date() {
        return exam_start_date;
    }

    public void setExam_start_date(String exam_start_date) {
        this.exam_start_date = exam_start_date;
    }

    public String getExam_end_date() {
        return exam_end_date;
    }

    public void setExam_end_date(String exam_end_date) {
        this.exam_end_date = exam_end_date;
    }

    public String getEnrollment_form_required() {
        return enrollment_form_required;
    }

    public void setEnrollment_form_required(String enrollment_form_required) {
        this.enrollment_form_required = enrollment_form_required;
    }




    public String getExam_status() {
        return exam_status;
    }

    public void setExam_status(String exam_status) {
        this.exam_status = exam_status;
    }

    public String getExam_response() {
        return exam_response;
    }

    public void setExam_response(String exam_response) {
        this.exam_response = exam_response;
    }

    public String getExam_duration() {
        return exam_duration;
    }

    public void setExam_duration(String exam_duration) {
        this.exam_duration = exam_duration;
    }

    public String getEnrollment_form_status() {
        return enrollment_form_status;
    }

    public void setEnrollment_form_status(String enrollment_form_status) {
        this.enrollment_form_status = enrollment_form_status;
    }




    public MexamsPojo(String student_id, String exam_status, String exam_name, String exam_start_date, String exam_end_date,
                      String exam_response, String exam_duration, String enrollment_form_required, String enrollment_form_status,
                        String student_feedback,String feedback_form_status) {

        this.student_id = student_id;
        this.exam_status = exam_status;
        this.exam_name = exam_name;
        this.exam_start_date = exam_start_date;
        this.exam_end_date = exam_end_date;
        this.exam_response = exam_response;
        this.exam_duration = exam_duration;
        this.enrollment_form_required = enrollment_form_required;
        this.enrollment_form_status = enrollment_form_status;
        this.student_feedback = student_feedback;
        this.feedback_form_status = feedback_form_status;
    }




}
