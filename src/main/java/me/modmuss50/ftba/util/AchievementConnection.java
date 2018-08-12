package me.modmuss50.ftba.util;

import me.modmuss50.ftba.files.runs.RunData;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import reborncore.common.util.serialization.SerializationUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class AchievementConnection {


	public static String postData(RunData data) throws IOException {
		
		return "Error";
	}
}
