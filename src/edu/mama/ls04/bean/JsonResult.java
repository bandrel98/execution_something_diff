package edu.mama.ls04.bean;

public class JsonResult {
    private boolean success;
    private String msg;
    private Object data;

    public JsonResult(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    public JsonResult(boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
