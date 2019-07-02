package me.wcy.music.enums;

/**
 * 播放模式
 *
 */
public enum PlayModeEnum {
    LOOP(0),//循环播放
    SHUFFLE(1),//随机播放
    SINGLE(2);//单曲循环

    private int value;

    PlayModeEnum(int value) {
        this.value = value;
    }

    public static PlayModeEnum valueOf(int value) {
        switch (value) {
            case 1:
                return SHUFFLE;
            case 2:
                return SINGLE;
            case 0:
            default:
                return LOOP;
        }
    }

    public int value() {
        return value;
    }
}
