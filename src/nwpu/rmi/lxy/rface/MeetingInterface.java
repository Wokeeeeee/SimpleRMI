package nwpu.rmi.lxy.rface;

import nwpu.rmi.lxy.bean.Meeting;
import nwpu.rmi.lxy.bean.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.UUID;
import java.util.Vector;

public interface MeetingInterface extends Remote {
    //register [username] [password]
    public String userRegister(String name,String pwd) throws RemoteException;

    //add [username] [password] [otherusername] [start] [end] [title]
    public String addMeeting( String participant, Date start, Date end, String title) throws RemoteException;

    //delete [username] [password] [meetingid]
    public String deleteMeeting(String uuid) throws RemoteException;

    //query [username] [password] [start] [end]
    public String queryMeeting(Date start, Date end) throws RemoteException;

    //clear [username] [password]
    public String clearMeeting() throws RemoteException;

    public String menu() throws RemoteException;

    public String getUserList() throws RemoteException;

    public String getMeetingList() throws RemoteException;

    public String logIn(String name,String pwd) throws RemoteException;

    public String logOut() throws RemoteException;

    public String currentStatus() throws RemoteException;
}
