package com.mfamilys.mine.model.daily;

/**
 * Created by mfamilys on 16-4-9.
 */
public class DailyDetailsBean {
    //对应内容返回的数据
    //内容实体
    private String body;
    //图像
    private String image;
    //标题
    private String title;
    //css
    private String[] css;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getCss() {
        return css;
    }

    public void setCss(String[] css) {
        this.css = css;
    }
}
