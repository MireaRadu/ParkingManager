package services;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Observer extends Remote, Serializable {
    void update() throws RemoteException;
}
