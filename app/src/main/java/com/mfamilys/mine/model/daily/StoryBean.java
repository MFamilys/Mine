package com.mfamilys.mine.model.daily;

/**
 * Created by mfamilys on 16-4-7.
 */
public class StoryBean {
    //标题
    private String title;
    //图片数组
    private String[] images;
    //内容ID
    private int id;
    //内容实体
    private String body;
    //大图
    private String largepic;
    //是否收藏标志
    private boolean isCollected;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(int collected) {
        isCollected = (collected ==1 ?true:false);
    }
}
