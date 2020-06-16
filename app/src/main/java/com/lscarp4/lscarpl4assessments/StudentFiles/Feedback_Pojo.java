package com.lscarp4.lscarpl4assessments.StudentFiles;

public class Feedback_Pojo {

    public Integer getQnum() {
        return qnum;
    }

    public void setQnum(Integer qnum) {
        this.qnum = qnum;
    }

    Integer qnum;
    String ques;
    String option1;
    String option2;

    public String getQues() {
        return ques;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getOption5() {
        return option5;
    }

    public void setOption5(String option5) {
        this.option5 = option5;
    }

    String option3;
    String option4;
    String option5;

    public Feedback_Pojo(Integer qnum,String ques, String option1, String option2, String option3, String option4, String option5) {
        this.qnum = qnum;
        this.ques = ques;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.option5 = option5;
    }


}
