package com.mfamilys.mine.model.daily;

/**
 * Created by mfamilys on 16-4-7.
 */
public class TopStory {
    //标题
    private String title;
    //图片
    private String image;
    //ID
    private int id;
    //内容实体
    private String body;
    //大图
    private String largepic;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLargepic() {
        return largepic;
    }

    public void setLargepic(String largepic) {
        this.largepic = largepic;
    }
}
