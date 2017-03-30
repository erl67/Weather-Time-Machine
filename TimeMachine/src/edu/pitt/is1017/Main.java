package src.edu.pitt.is1017;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.json.JsonException;
import javax.json.JsonObject;

public class Main {

	public static void main(String[] args) {

		String dsKey = "472f1ba38a5f3d13407fdb589d975c8c/";
		String dsUrl = "https://api.darksky.net/forecast/";
		String dsLoc = "37.8267,-122.4233,";
		String dsTime = "946684800";

		String testURL = dsUrl + dsKey + dsLoc + dsTime;

		System.out.println("TEST");
		WeatherServlet.URLConnectionReader(testURL);
		//WeatherServlet.URLConnectionReader("http://google.com");

		JsonObject json = readJsonFromUrl(testURL);
		System.out.println(json.toString());
		System.out.println(json.get("id"));

	}

	private static String readAll(Reader rd)  {
		StringBuilder sb = new StringBuilder();
		int cp;
		try {
			while ((cp = rd.read()) != -1) {
				sb.append((char) cp);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	public static JsonObject readJsonFromUrl(String url) {
		InputStream is = null;
		try {
			is = new URL(url).openStream();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JsonObject json = null;
			System.out.println("jsonText= " + jsonText);
			//JsonObject json = new JsonObject();
			return json;
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


}