package me.wcy.music.model;

public class Clock {
    private int hour;
    private int minute;
    private String ringname;
    private String remindway;
    private boolean[] workday={false,false,false,false,false,false,false};

    public Clock(int hour, int minute, String ringname, String remindway, boolean[] workday){
        this.hour=hour;
        this.minute=minute;
        this.ringname=ringname;
        this.remindway=remindway;
        this.workday=workday;
    }
    public Clock() {
        ;
    }
    public void setTime(int hour,int minute) {
        this.hour = hour;
        this.minute=minute;
    }
    public String getTime(){
        return (hour+":"+minute);
    }
    public int getHour(){return this.hour;}
    public int getMinute(){return this.minute;}
    public void setRingname(String ringname) {
        this.ringname = ringname;
    }
    public String getRingname(){
        return ringname;
    }
    public void setWorkday(boolean Mon,boolean Tue,boolean Wed,boolean Thu,boolean Fri,boolean Sat,boolean Sun){
        workday[0]=Mon;
        workday[1]=Tue;
        workday[2]=Wed;
        workday[3]=Thu;
        workday[4]=Fri;
        workday[5]=Sat;
        workday[6]=Sun;
    }
    public void setWorkday(boolean[] workday){this.workday=workday;}
    public boolean[] getWorkday(){
        return this.workday;
    }
    public void setRemindway(String remindway) {
        this.remindway = remindway;
    }
    public String getRemindway(){
        return remindway;
    }

}
