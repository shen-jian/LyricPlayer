## 歌词播放控件
**使用方式**  

```xml
<me.shenj.lyric.LyricView
    android:id="@+id/lyricView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginTop="10dp"
    android:padding="10dp"
    lyric:animDuration="400"
    lyric:currentTextColor="#FF0E8DC4"
    lyric:lineSpace="15dp"
    lyric:maxLine="10"
    lyric:textColor="#ffffff"
    lyric:textSize="20sp" />
```
**公共方法**  

```java
/**
 * 传入歌词内容
 * @param lyrics 歌词列表
 */
public void setLyricContent(List<Lyric> lyrics);

/**
* 指定当前播放时间，刷新歌词
* @param time
* @param animation 是否动画滚动
*/
public void setCurrentTime(long time, boolean animation);
```

