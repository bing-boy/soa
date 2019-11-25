package com.rmi;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import com.loadbalance.NodeInfo;

public class RmiUtil {


	/**
	 * @param host
	 * @param port
	 * @param id
	 */
	public void startRmiServer(String host, String port, String id) {
		try {
			SoaRmi soaRmi = new SoaRmiImpl();
			LocateRegistry.createRegistry(Integer.valueOf(port));
			Naming.bind("rmi://" + host + ":" + port + "/" + id, soaRmi);
			System.out.println("rmi server start !");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
	}

	public SoaRmi startRmiClient(NodeInfo nodeInfo, String id) {
		String host = nodeInfo.getContextPath();
		String port = nodeInfo.getProt();

		try {
			return (SoaRmi) Naming.lookup("rmi://" + host + ":" + port + "/" + id);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		return null;
	}
}
