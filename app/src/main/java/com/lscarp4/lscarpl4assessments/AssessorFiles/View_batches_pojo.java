package com.lscarp4.lscarpl4assessments.AssessorFiles;

public class View_batches_pojo {

    String fname;

    public String getStd_id() {
        return std_id;
    }

    public void setStd_id(String std_id) {
        this.std_id = std_id;
    }

    String std_id;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getAdhar() {
        return adhar;
    }

    public void setAdhar(String adhar) {
        this.adhar = adhar;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getTheory() {
        return theory;
    }

    public void setTheory(String theory) {
        this.theory = theory;
    }

    String lname;
    String adhar;
    String mobile;
    String email;
    String uname;
    String pwd;
    String otp;
    String theory;

    public String getStd_result() {
        return std_result;
    }

    public void setStd_result(String std_result) {
        this.std_result = std_result;
    }

    String std_result;


    public View_batches_pojo(String fname, String lname, String adhar, String mobile, String email, String uname, String pwd, String otp, String theory, String std_id, String std_reslt) {
        this.fname = fname;
        this.lname = lname;
        this.adhar = adhar;
        this.mobile = mobile;
        this.email = email;
        this.uname = uname;
        this.pwd = pwd;
        this.otp = otp;
        this.theory = theory;
        this.std_id = std_id;
        this.std_result = std_reslt;

    }




}
