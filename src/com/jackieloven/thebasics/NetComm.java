package com.jackieloven.thebasics;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/** encapsulates networked communication with a single client or server;
    this class is modified from PlayerSocket.java in Andrew's HW3 submission */
// This is the network communication class that handles all communication methods, Server and Patient are dependent on this
public class NetComm {
	/** object that is notified when a new message is received */
	private Networked node;
	/** client socket that is endpoint for network communication */
	private Socket socket;
	/** reads messages related to this client over the network */
	private ObjectInputStream in;
	/** writes messages related to this client over the network */
	private ObjectOutputStream out;

	/** constructor for network communication object; throws IOException if fails */
	public NetComm(Socket newSocket, Networked networkNode) throws IOException
    {
		if (newSocket == null) {
			throw new NullPointerException("Socket cannot be null");
		}
		socket = newSocket;
		node = networkNode;
		try {
			out = new ObjectOutputStream(socket.getOutputStream()); // always add before input stream, see http://stackoverflow.com/questions/8088557/getinputstream-blocks
			in = new ObjectInputStream(socket.getInputStream());
			new Thread(new NetCommReader()).start();
		}
		// pass exceptions onto caller
		catch (IOException ex) {
			throw ex;
		}
	}

	/** write specified object to output stream */
	public void write(Object obj) {
		try {
			out.writeObject(obj);
			out.flush();
			out.reset();
		}
		// print error message if unknown error
		// (if I don't know what causes the error then I can't write code to handle it)
		catch (IOException ex) {
			System.out.println("Network write error: " + ex.toString());
		}
	}

	/** close connection */
	public void close() {
		try {
			out.writeObject(new CloseConnectionMsg());
			out.flush();
		}
		catch (Exception ex) {
			// ignore exceptions
		}
	}

	/** thread to receive messages */
	private class NetCommReader implements Runnable
    {
		/** keep polling for new messages, and forward received messages to network node
		    (this is automatically called when thread starts) */
		public void run() {
			Object msgObj;
			while (true) { // poll for messages until the program exits
				msgObj = read();
				if (msgObj != null) {
                    node.msgReceived(msgObj, NetComm.this); // forward message to network node
					if (msgObj instanceof CloseConnectionMsg) {
                        return; // stop polling for messages if disconnected
					}
				}
			}
		}

		/** returns object read from input stream, or null if no new message,
		    or a CloseConnectionMsg if either client or server disconnected from network */
		private Object read() {
			try {
				return in.readObject();
			}
			// exceptions below are thrown when client disconnected
			catch (EOFException ex) {
				return new CloseConnectionMsg();
			}
			catch (SocketException ex) {
				return new CloseConnectionMsg();
			}
			catch (StreamCorruptedException ex) {
				System.out.println("Input stream corrupted: " + ex.getMessage());
				return new CloseConnectionMsg();
			}
			// print stack trace and return null if unknown error
			// (if I don't know what causes the error then I can't write code to handle it)
			catch (Exception ex) {
				System.out.println("Network read error:");
				ex.printStackTrace();
				return null;
			}
		}
	}
}
