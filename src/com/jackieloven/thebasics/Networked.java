package com.jackieloven.thebasics;

/** indicates that implementing class is capable of receiving messages across the network */
// This is an interface for the Patient and Server classes that is overridden in both
public interface Networked {
	/** handle message received from the network */
	public void msgReceived(Object msgObj, NetComm sender);
}
