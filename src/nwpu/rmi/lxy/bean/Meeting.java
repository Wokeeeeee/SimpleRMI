package nwpu.rmi.lxy.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Meeting implements Serializable {
    private Date start_time;
    private Date end_time;
    private String title;
    private String initiator;
    private String participant;
    private String id;

    public Meeting(String _initiator,String _participant,Date _start,Date _end,String _title){
        start_time=_start;
        end_time=_end;
        title=_title;
        initiator=_initiator;
        participant=_participant;
        id=UUID.randomUUID().toString();
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInitiator() {
        return initiator;
    }

    public void setInitiator(String initiator) {
        this.initiator = initiator;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Meeting |" +
                "start_time=" + start_time +
                ", end_time=" + end_time +
                ", title='" + title + '\'' +
                ", initiator='" + initiator + '\'' +
                ", participant='" + participant + '\'' +
                ", id=" + id +
                ' ';
    }
}
