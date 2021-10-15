package nwpu.rmi.lxy.server;

import nwpu.rmi.lxy.bean.Meeting;
import nwpu.rmi.lxy.bean.User;
import nwpu.rmi.lxy.rface.MeetingInterface;

import java.lang.invoke.VolatileCallSite;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class MeetingManager extends UnicastRemoteObject implements MeetingInterface {
    private Vector<User> users = new Vector<>();
    private Vector<Meeting> meetings = new Vector<>();
    private String userName;
    private String userPwd;


    /**
     * 构造函数
     * 设置name和pwd为null
     *
     * @throws RemoteException
     */
    public MeetingManager() throws RemoteException {
        super();
        userName = null;
        userPwd = null;
    }

    /**
     * 用户注册
     * 如果用户已存在返回错误信息
     *
     * @param name
     * @param pwd
     * @return
     * @throws RemoteException
     */
    @Override
    public String userRegister(String name, String pwd) throws RemoteException {
        for (User u : users) {
            if (u.getName().equals(name)) {
                return "register FAIL: user name already exists";
            }
        }
        users.add(new User(name, pwd));
        return "register SUCCESS";
    }

    /**
     * 用户登录
     * 如果用户信息出错或用户不存在，返回错误信息
     *
     * @param name
     * @param pwd
     * @return
     * @throws RemoteException
     */
    @Override
    public String logIn(String name, String pwd) throws RemoteException {
        for (User u : users) {
            if (u.getName().equals(name) && u.getPassword().equals(pwd)) {
                userName = name;
                userPwd = pwd;
                return "log in SUCCESS.";
            }
        }
        return "log in FAIL: name or password wrong " + name + ", please register first or check if password correct.";
    }

    /**
     * 退出登录
     *
     * @return
     * @throws RemoteException
     */
    @Override
    public String logOut() throws RemoteException {
        userName = null;
        userPwd = null;
        return "log out SUCCESS.";
    }


    /**
     * 检查用户是否存在
     *
     * @param name
     * @return
     */
    public boolean isUserExist(String name) {
        for (User u : users) {
            if (u.getName().equals(name)) return true;
        }
        return false;
    }

    /**
     * 检查当前是否处于登录状态
     *
     * @return
     */
    public boolean isLogin() {
        return userName != null && userPwd != null;
    }

    /**
     * 添加会议信息，添加之前需要把time用String转成Date
     * parti是否存在
     * initor和parti重合
     * meeting 时间出错
     * meeting 时间重合
     *
     * @param participant
     * @param start
     * @param end
     * @param title
     * @return
     * @throws RemoteException
     */
    @Override
    public String addMeeting(String participant, Date start, Date end, String title) throws RemoteException {
        if (!isLogin()) return "please log in first.";
        if (!isUserExist(participant)) return "add meeting FAIL: your participant dosn't exist.";
        if (participant.equals(userName)) return "add meeting FAIL: participant is the same with initiator";
        for (Meeting m : meetings) {
            //在其他会议结束之前开始 在其他会议开始之后结束-->时间重叠
            if (start.after(end)) return "add meeting FAIL: meeting start time must be ealier than the end time.";
            if (start.before(m.getEnd_time()) && end.after(m.getStart_time()))
                return "add meeting FAIL: your meeting time overlaps with other meetings.";
        }
        Meeting m = new Meeting(userName, userPwd, start, end, title);
        meetings.add(m);
        return "add meeting SUCCESS:\n your meeting info:  " + m.toString();
    }

    /**
     * 根据会议id删除会议
     * 会议id不存在
     *
     * @param uuid
     * @return
     * @throws RemoteException
     */
    @Override
    public String deleteMeeting(String uuid) throws RemoteException {
        if (!isLogin()) return "please log in first.";
        Iterator<Meeting> it = meetings.iterator();
        boolean isUserCreatMeeting = false;
        while (it.hasNext()) {
            Meeting meeting = it.next();
            if (meeting.getId().equals(uuid)) {
                it.remove();
                return "delete meeting SUCCESS.";
            }
        }
        return "delete meeting FAIL: there is no meeting with the id.";
    }

    /**
     * 在特定时间里查到的所有符合条件的会议记录,按时间排序
     * 开始结束
     * 特定时间内不存在会议
     * @param start
     * @param end
     * @return
     * @throws RemoteException
     */
    @Override
    public String queryMeeting(Date start, Date end) throws RemoteException {
        if (!isLogin()) return "please log in first.";
        if (start.after(end)) return "query meeting FAIL: start time must be earlier than end time.";
        Vector<Meeting> anwsers = new Vector<>();
        for (Meeting m : meetings) {
            if (start.before(m.getStart_time()) && end.after(m.getEnd_time())) anwsers.add(m);
        }
        if (anwsers.isEmpty()) return "query Meeting FAIL: there is no meeting in the given time period.";

        //按照会议的开始时间进行排序
        Collections.sort(anwsers, new Comparator<Meeting>() {
            @Override
            public int compare(Meeting o1, Meeting o2) {
                return o1.getStart_time().before(o2.getStart_time()) ? 1 : 0;
            }
        });
        //输出排好序的会议
        StringBuilder sb = new StringBuilder();
        sb.append(" ---Query Meeting").append("( ").append(start).append(" - ").append(end).append(" )---\n");
        for (Meeting m : anwsers) {
            sb.append(m.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * 删除某用户创建的所有会议
     * 该用户并未创建会议
     * @return
     * @throws RemoteException
     */
    @Override
    public String clearMeeting() throws RemoteException {
        if (!isLogin()) return "please log in first.";
        Iterator<Meeting> it = meetings.iterator();
        boolean isUserCreatMeeting = false;
        while (it.hasNext()) {
            Meeting meeting = it.next();
            if (meeting.getInitiator().equals(userName)) {
                it.remove();
                isUserCreatMeeting = true;
            }
        }
        if (!isUserCreatMeeting) return "clear meeting FAIL: the user didn't create any meeting.";
        return "clear meeting SUCCESS.";
    }

    /**
     * 获得当前的用户名单
     *
     * @return
     * @throws RemoteException
     */
    @Override
    public String getUserList() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append("----User List----\n");
        for (User u : users) {
            sb.append(u.toString()).append("\n");
        }
        return sb.toString();
    }

    /**
     * 获得当前的会议例子
     *
     * @return
     * @throws RemoteException
     */
    @Override
    public String getMeetingList() throws RemoteException {
        StringBuilder sb = new StringBuilder();
        sb.append("----Meeting List----\n");
        for (Meeting m : meetings) {
            sb.append(m.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String currentStatus() throws RemoteException {
        if (isLogin()) return "current user is " + userName;
        return "no user now, please log in";
    }

    @Override
    public String menu() throws RemoteException {
        return new StringBuilder().append("RMI menu\n")
                .append("1.add -arguments:[otherusername] [start] [end] [title]\n")
                .append("2.delete -arguments:[meetingid]\n")
                .append("3.clear -arguments:no args\n")
                .append("4.query -arguments:[start] [end]\n")
                .append("5.help -arguments:no args\n")
                .append("6.register -arguments:[name] [password]\n")
                .append("7.userList -arguments:no args\n")
                .append("8.meetingList -arguments:no args\n")
                .append("9.login -arguments: [name] [pwd]\n")
                .append("10.logout -arguments: no args\n")
                .append("11.currentUser -arguments: no args\n")
                .append("12.quit -arguments:no args\n").toString();
    }
}



