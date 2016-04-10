package com.mfamilys.mine.api;

/**
 * Created by mfamilys on 16-4-7.
 */

/*
*  Created by MFamilys on 16.04.05
*  Zhihu(link www.zhihu.com) Daily api
*  @author MFamilys
*  @version Mine 2.0
* */
public class Dailyapi {
    //version 1.0
    //public static String daliy_url="http://diy-devz.rhcloud.com/zhihu";

    //Version 2.0
    //最新消息(根据日期分为：id,图片,标题)
    public static String daily_url="http://news-at.zhihu.com/api/4/news/latest";
    //消息内容(根据Id分为内容,头像)
    public static String daily_details_url="http://news.at.zhihu.com/api/4/news/";
    //消息的附属消息
    public static String  daily_story_base_url="http://daily.zhihu.com/story/";
    //过往消息
    public static String daily_old_url="http://news.at.zhihu.com/api/4/news/before/";
}
