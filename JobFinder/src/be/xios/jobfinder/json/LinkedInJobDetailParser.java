package be.xios.jobfinder.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.text.Html;
import android.util.JsonReader;
import be.xios.jobfinder.model.LinkedInJobDetail;

public class LinkedInJobDetailParser {
	
	/*
	public LinkedInJobDetail readJobDetailStream(InputStream in) throws IOException {
		String jobDescription = JsonPath.read(in, "$.description");
		in.reset();
		String skills = JsonPath.read(in, "$.skills-and-experience");
		
//		String jobDescription = JsonPath.read(jobDetailJSON, "$.description");
//		String skills = JsonPath.read(jobDetailJSON, "$.skills-and-experience");
		
		LinkedInJobDetail jobDetail = new LinkedInJobDetail();
		jobDetail.setJobDescription(jobDescription);
		jobDetail.setSkills(skills);
		
		return jobDetail;
	}
	
	public LinkedInJobDetail readJobDetailStream(String in) throws IOException {
		String jobDescription = JsonPath.read(in, "$.description");
		String skills = JsonPath.read(in, "$.skills-and-experience");
		
//		String jobDescription = JsonPath.read(jobDetailJSON, "$.description");
//		String skills = JsonPath.read(jobDetailJSON, "$.skills-and-experience");
		
		LinkedInJobDetail jobDetail = new LinkedInJobDetail();
		jobDetail.setJobDescription(jobDescription);
		jobDetail.setSkills(skills);
		
		return jobDetail;
	}
	*/
	
	private LinkedInJobDetail jobDetail;
	
	public LinkedInJobDetail readJobDetailStream(InputStream in) throws IOException {
		jobDetail = new LinkedInJobDetail();
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		
		try {
			readJob(reader);
		} finally {
			reader.close();
		}
		
		return jobDetail;
	}

	public void readJob(JsonReader reader) throws IOException {
		reader.beginObject();
		while (reader.hasNext()) {
			String name = "";
			try {
				name = reader.nextName();
			} catch (Exception e) {
				reader.skipValue();
			}
			
			if ("id".equals(name))
				jobDetail.setId(reader.nextInt());
			if ("description".equals(name))
				jobDetail.setJobDescription(Html.fromHtml(reader.nextString()).toString());
			if ("skillsAndExperience".equals(name))
				jobDetail.setSkills(Html.fromHtml(reader.nextString()).toString());
			if ("company".equals(name))
				readCompany(reader);
		}
		reader.endObject();
	}

	private void readCompany(JsonReader reader) throws IOException {
		reader.beginObject();
		while (reader.hasNext()) {
			String name = "";
			try {
				name = reader.nextName();
			} catch (Exception e) {
				reader.skipValue();
			}
			
			if ("id".equals(name))
				jobDetail.setCompanyId(reader.nextInt());
		}
		reader.endObject();
	}
}