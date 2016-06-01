
package me.shenj.lyric;

/**
 *  <p>
 *  实体类，时间戳与之对应歌词行
 * </P>
 */
public class Lyric {
    private Long time;
    private String content;

    public Lyric(Long time, String content) {
        this.time = time;
        this.content = content;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
