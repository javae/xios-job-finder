package be.xios.jobfinder.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.text.Html;
import android.util.JsonReader;
import be.xios.jobfinder.model.LinkedInJobDetail;

public class LinkedInCompanyParser {

	private String companyDescription;
	
	public String readJobDetailStream(InputStream in) throws IOException {
		companyDescription = "";
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		
		try {
			readCompany(reader);
		} finally {
			reader.close();
		}
		
		return companyDescription;
	}

	public void readCompany(JsonReader reader) throws IOException {
		reader.beginObject();
		while (reader.hasNext()) {
			String name = "";
			try {
				name = reader.nextName();
			} catch (Exception e) {
				reader.skipValue();
			}
			
			if ("description".equals(name))
				companyDescription = Html.fromHtml(reader.nextString()).toString();
		}
		reader.endObject();
	}
	
}