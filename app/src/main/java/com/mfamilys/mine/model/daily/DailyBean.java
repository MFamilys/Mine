package com.mfamilys.mine.model.daily;

/**
 * Created by mfamilys on 16-4-7.
 */
public class DailyBean {

    //日期
    private String date;
    //资讯实体
    private StoryBean[] stories;
    //热门资讯
  //  private TopStory[] topStories;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public StoryBean[] getStories() {
        return stories;
    }

    public void setStories(StoryBean[] stories) {
        this.stories = stories;
    }

/*
    public TopStory[] getTopStories() {
        return topStories;
    }

    public void setTopStories(TopStory[] topStories) {
        this.topStories = topStories;
    }
*/

}
