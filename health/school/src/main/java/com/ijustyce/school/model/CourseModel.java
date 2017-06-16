package com.ijustyce.school.model;

import com.ijustyce.fastandroiddev.net.IResponseData;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yc on 16-3-5. 课程信息
 */
public class CourseModel extends IResponseData<CourseModel.DataEntity> {

    private ResultEntity result;
    private List<DataEntity> data;

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public ResultEntity getResult() {
        return result;
    }

    public List<DataEntity> getData() {
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

    public static class DataEntity implements Serializable {
        private String id;
        private String day;
        private String name;
        private String _time;
        private String address;
        private String teacherId;
        private Object teacherEmail;
        private String teacherName;

        public void setId(String id) {
            this.id = id;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void set_time(String _time) {
            this._time = _time;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setTeacherId(String teacherId) {
            this.teacherId = teacherId;
        }

        public void setTeacherEmail(Object teacherEmail) {
            this.teacherEmail = teacherEmail;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getId() {
            return id;
        }

        public String getDay() {
            return day;
        }

        public String getName() {
            return name;
        }

        public String get_time() {
            return _time;
        }

        public String getAddress() {
            return address;
        }

        public String getTeacherId() {
            return teacherId;
        }

        public Object getTeacherEmail() {
            return teacherEmail;
        }

        public String getTeacherName() {
            return teacherName;
        }
    }
}
