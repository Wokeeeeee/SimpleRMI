package nwpu.rmi.lxy.server;

import nwpu.rmi.lxy.rface.MeetingInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class MeetingServer {
    public static void main(String[] args) {
        try{
            LocateRegistry.createRegistry(1099);
            MeetingInterface meetingManager=new MeetingManager();
            Naming.rebind("meetingManager",meetingManager);
            System.out.println("Meeting Server is ready.");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
