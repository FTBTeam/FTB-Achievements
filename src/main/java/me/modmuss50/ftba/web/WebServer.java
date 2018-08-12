package me.modmuss50.ftba.web;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import fi.iki.elonen.NanoHTTPD;
import me.modmuss50.ftba.client.ClientDataManager;
import me.modmuss50.ftba.client.RunComparison;
import me.modmuss50.ftba.client.hud.Timer;
import me.modmuss50.ftba.files.runs.RunData;
import me.modmuss50.ftba.util.TimerServerHandler;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * Created by Mark on 09/04/2017.
 */
public class WebServer extends NanoHTTPD {

	String indexData;
	HashMap<String, Pair<String, String>> streamMap = new HashMap<>();

	public static WebServer SERVER;

	public WebServer() throws IOException, URISyntaxException {
		super(7123);
		loadFiles();
		start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
		System.out.println("\nRunning! Point your browsers to http://localhost:7123/ \n");
	}

	@Override
	public Response serve(IHTTPSession session) {
		if (session.getUri().equals("/") || session.getUri().equals("/index.html")) {
			return newFixedLengthResponse(indexData);
		}
		if (session.getUri().equals("/time")) {
			String response = "<h2>" + Timer.getNiceTime() + "</h2>";
			if (RunComparison.getRunData() != null) {
				RunData runData = RunComparison.getRunData();
				response = response + "<h4>Comparing run against " + runData.userName + "</h4>" + "<p>Time to beat: " + TimerServerHandler.getNiceTimeFromLong(runData.totalTime) + "</p>";
			}
			return newFixedLengthResponse(response);
		}
		if (session.getUri().equals("/data")) {
			return newFixedLengthResponse(PageBuilder.getData(this, ClientDataManager.getConfigFormat(), ClientDataManager.getWorldFormat()));
		}
		if (streamMap.containsKey(session.getUri())) {
			Pair<String, String> responseData = streamMap.get(session.getUri());
			try {
				return newChunkedResponse(Response.Status.OK, responseData.getRight(), getInputStream(responseData.getLeft()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return newFixedLengthResponse("An error has occurred!");
	}

	public void loadFiles() throws IOException, URISyntaxException {
		streamMap.clear();
		indexData = readFile("index.html");
		streamMap.put("/bootstrap-theme.min.css", getFileResponse("bootstrap-theme.min.css"));
		streamMap.put("/bootstrap.min.css", getFileResponse("bootstrap.min.css"));
		streamMap.put("/script.js", getFileResponse("script.js"));
		streamMap.put("/style.css", getFileResponse("style.css"));
		streamMap.put("/jquery-3.2.0.min.js", getFileResponse("jquery-3.2.0.min.js"));
	}

	public String readFile(String name) throws IOException {
		return Resources.toString(WebServer.class.getResource("/assets/ftbachievements/web/" + name), Charsets.UTF_8);
	}

	public Pair<String, String> getFileResponse(String name) throws IOException, URISyntaxException {
		return Pair.of(name, getContentType(name));
	}

	public String getContentType(String name) {
		if (name.endsWith(".js")) {
			return "application/javascript";
		}
		return "text/css";
	}

	public InputStream getInputStream(String name) throws IOException {
		InputStream inputStream = WebServer.class.getResource("/assets/ftbachievements/web/" + name).openStream();
		return inputStream;
	}

}
