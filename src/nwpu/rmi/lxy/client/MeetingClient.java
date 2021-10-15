package nwpu.rmi.lxy.client;

import nwpu.rmi.lxy.bean.User;
import nwpu.rmi.lxy.rface.MeetingInterface;

import java.io.Reader;
import java.rmi.Naming;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class MeetingClient {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy/MM/dd/HH:mm");

        try {
            MeetingInterface meeting = (MeetingInterface) Naming.lookup("meetingManager");

            //输出菜单
            System.out.println(meeting.menu());
            while (true) {
                System.out.print("please enter your command > ");
                if (!in.hasNextLine()) break;

                String cmd[] = in.nextLine().split(" ");

                switch (cmd[0]) {
                    case "register":
                        if (cmd.length != 3) {
                            System.out.println("register command fail: ".toUpperCase() + " register -arguments [username] [password]");
                            break;
                        }
                        System.out.println(meeting.userRegister(cmd[1], cmd[2]));
                        break;
                    case "add":
                        if (cmd.length < 5) {
                            System.out.println("add command fail: ".toUpperCase() + " add -arguments [otherusername] [start] [end] [title]");
                            break;
                        }
                        try {
                            Date start = myFmt.parse(cmd[2]);
                            Date end = myFmt.parse(cmd[3]);
                            String title = "";
                            for (int i = 4; i < cmd.length; i++) {
                                title += cmd[i];
                                if (i != cmd.length - 1) title += " ";
                            }
                            System.out.println(meeting.addMeeting(cmd[1], start, end, title));
                        } catch (ParseException e) {
                            System.out.println("DATE FORMAT ERROR:" + " correct format is like YYYY/MM/DD/HH:mm");
                        }
                        break;
                    case "delete":
                        if (cmd.length != 2) {
                            System.out.println("delete command fail: ".toUpperCase() + "delete -argument delete [meetingid]");
                            break;
                        }
                        System.out.println(meeting.deleteMeeting(cmd[1]));
                        break;
                    case "clear":
                        if (cmd.length != 1) {
                            System.out.println("clear command fail".toUpperCase() + ": no arguments needed");
                            break;
                        }
                        System.out.println(meeting.clearMeeting());
                        break;
                    case "query":
                        try {
                            if (cmd.length != 3) {
                                System.out.println("query command fail".toUpperCase() + ": arguments: [start] [end]");
                                break;
                            }
                            Date start_ = myFmt.parse(cmd[1]);
                            Date end_ = myFmt.parse(cmd[2]);
                            System.out.println(meeting.queryMeeting(start_, end_));
                        } catch (ParseException e) {
                            System.out.println("Date FORMAT WRONG: correct format is like YYYY/MM/DD/HH:mm");

                        }
                        break;
                    case "help":
                        System.out.println(meeting.menu());
                        break;
                    case "logout":
                        System.out.println(meeting.logOut());
                        break;
                    case "login":
                        if (cmd.length != 3) {
                            System.out.println("log in fail:".toUpperCase() + " argument clear [username] [password]");
                            break;
                        }
                        System.out.println(meeting.logIn(cmd[1], cmd[2]));
                        break;
                    case "quit":
                        return;
                    case "meetingList":
                        System.out.println(meeting.getMeetingList());
                        break;
                    case "userList":
                        System.out.println(meeting.getUserList());
                        break;
                    case "currentUser":
                        System.out.println(meeting.currentStatus());
                        break;
                    default:
                        System.out.println("there is no command named " + cmd[0] + ", you can use \"help\" to list all provided command.");
                        break;
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
