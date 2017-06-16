package com.ijustyce.school.model;

import com.ijustyce.fastandroiddev.net.IResponseData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yc on 16-3-19.    笔记 model
 */
public class BiJi extends IResponseData<BiJi.DataEntity> {

    private ResultEntity result;
    private List<DataEntity> data;

    public ResultEntity getResult() {
        return result;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public static class ResultEntity {
        private String code;
        private String msg;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

    public static class DataEntity implements Serializable{
        private String id;
        private String courseId;
        private String studentId;
        private String descript;
        private String info;
        private String head;
        private int teacher;
        private int isFuJian;

        public boolean isFuJian() {
            return isFuJian == 1;
        }

        public void setIsFuJian(int isFuJian) {
            this.isFuJian = isFuJian;
        }

        public String getHead() {
            return head;
        }

        public void setHead(String head) {
            this.head = head;
        }

        public int getTeacher() {
            return teacher;
        }

        public void setTeacher(int teacher) {
            this.teacher = teacher;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public String getStudentId() {
            return studentId;
        }

        public void setStudentId(String studentId) {
            this.studentId = studentId;
        }

        public String getDescript() {
            return descript;
        }

        public void setDescript(String descript) {
            this.descript = descript;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
