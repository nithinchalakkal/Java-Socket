package com.Washtoom.estibyan_WS_Lock;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

public class MyService extends Service {

	public MyService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "Service was Created", Toast.LENGTH_LONG).show();
		Server service = new Server(this);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// Perform your long running operations here.
		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}

}

class Server {
	MyService activity;
	ServerSocket serverSocket;
	String message = "";
	static final int socketServerPORT = 9000;

	public Server(MyService activity) {
		this.activity = activity;
		Thread socketServerThread = new Thread(new SocketServerThread());
		socketServerThread.start();
	}

	public int getPort() {
		return socketServerPORT;
	}

	public void onDestroy() {
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private class SocketServerThread extends Thread {

		int count = 0;

		@Override
		public void run() {
			try {
				serverSocket = new ServerSocket(socketServerPORT);

				while (true) {
					Socket socket = serverSocket.accept();
					++count;
					message += "" + count + ":   Call from "
							+ socket.getInetAddress() + ":" + socket.getPort()
							+ "\n";

					SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(
							socket, count);
					socketServerReplyThread.run();

					// App will restart using below code

					Intent mStartActivity = new Intent(activity,
							MainActivity.class);
					int mPendingIntentId = 123456;
					PendingIntent mPendingIntent = PendingIntent.getActivity(
							activity, mPendingIntentId, mStartActivity,
							PendingIntent.FLAG_CANCEL_CURRENT);
					AlarmManager mgr = (AlarmManager) activity
							.getSystemService(activity.ALARM_SERVICE);
					mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100,
							mPendingIntent);
					System.exit(0);

				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	private class SocketServerReplyThread extends Thread {

		private Socket hostThreadSocket;

		SocketServerReplyThread(Socket socket, int c) {
			hostThreadSocket = socket;

		}

		@Override
		public void run() {
			OutputStream outputStream;
			String msgReply = "<html> <body><h1>Done</h1></body></html>  ";

			String OUTPUT = "<html><body>Done!</html></body>";
			String OUTPUT_HEADERS = "HTTP/1.1 200 OK\r\n"
					+ "Content-Type: text/html\r\n" + "Content-Length: ";
			String OUTPUT_END_OF_HEADERS = "\r\n\r\n";

			try {
				outputStream = hostThreadSocket.getOutputStream();

				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
						new BufferedOutputStream(outputStream), "UTF-8"));
				out.write(OUTPUT_HEADERS + OUTPUT.length()
						+ OUTPUT_END_OF_HEADERS + OUTPUT);
				out.flush();
				out.close();

				// message += "replayed: " + msgReply + "\n";

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// message += "Something wrong! " + e.toString() + "\n";
			}

		}

	}
}