package com.lscarp4.lscarpl4assessments.AssessorFiles;

public class Viva_Ques_pojo {

    String q_id;
    String question;

    public String getQ_id() {
        return q_id;
    }

    public void setQ_id(String q_id) {
        this.q_id = q_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQn_max_marks() {
        return qn_max_marks;
    }

    public void setQn_max_marks(String qn_max_marks) {
        this.qn_max_marks = qn_max_marks;
    }

    public String getNos_id() {
        return nos_id;
    }

    public void setNos_id(String nos_id) {
        this.nos_id = nos_id;
    }

    public String getNos_code() {
        return nos_code;
    }

    public void setNos_code(String nos_code) {
        this.nos_code = nos_code;
    }

    public String getNos_title() {
        return nos_title;
    }

    public void setNos_title(String nos_title) {
        this.nos_title = nos_title;
    }

    String qn_max_marks;
    String nos_id;
    String nos_code;
    String nos_title;

    public Viva_Ques_pojo(String q_id, String question, String qn_max_marks, String nos_id, String nos_code, String nos_title) {
        this.q_id = q_id;
        this.question = question;
        this.qn_max_marks = qn_max_marks;
        this.nos_id = nos_id;
        this.nos_code = nos_code;
        this.nos_title = nos_title;
    }



}
