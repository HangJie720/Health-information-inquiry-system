package com.ijustyce.school.model;

import com.ijustyce.fastandroiddev.net.IResponseData;

import java.util.List;

/**
 * Created by yc on 16-3-17.    用户信息类，登陆返回的数据
 */
public class UserInfo extends IResponseData {

    private ResultEntity result;
    private DataEntity data;

    private String userId, pw;

    @Override
    public List getData() {
        return null;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public ResultEntity getResult() {
        return result;
    }

    public DataEntity getUserData() {
        return data;
    }

    public static class ResultEntity {
        private String code;
        private String msg;

        public void setCode(String code) {
            this.code = code;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }

    public static class DataEntity {
        private String id;
        private String email;
        private String pw;
        private String teacher;
        private String head;
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPw(String pw) {
            this.pw = pw;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public String getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getPw() {
            return pw;
        }

        public String getTeacher() {
            return teacher;
        }

        public String getHead() {
            return head;
        }
    }
}
