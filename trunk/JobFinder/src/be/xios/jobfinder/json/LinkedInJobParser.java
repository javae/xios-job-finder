package be.xios.jobfinder.json;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.util.JsonReader;
import android.util.JsonToken;
import be.xios.jobfinder.model.LinkedInJob;

public class LinkedInJobParser {
	
	public List<LinkedInJob> readJsonStream(InputStream in) throws IOException {
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		try {
			return readJobs(reader);
		} finally {
			reader.close();
		}
	}

	public List<LinkedInJob> readJobs(JsonReader reader) throws IOException {
		List<LinkedInJob> jobs = new ArrayList<LinkedInJob>();
		
		reader.beginObject();
		while (reader.hasNext()) {
			String name = "";
			try {
				name = reader.nextName();
			} catch (Exception e) {
				reader.skipValue();
			}
			if ("jobs".equals(name))
				jobs = readJobValues(reader);
		}
		reader.endObject();
		return jobs;
	}
	
	public List<LinkedInJob> readJobValues(JsonReader reader) throws IOException {
		List<LinkedInJob> jobs = new ArrayList<LinkedInJob>();
		
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			System.out.println("name = " + name);
			if ("_count".equals(name))
				reader.nextLong();
			else if ("_start".equals(name))
				reader.nextLong();
			else if ("_total".equals(name))
				reader.nextLong();
			else if ("values".equals(name) && reader.peek() != JsonToken.NULL)
				jobs = readJobArray(reader);
		}
		reader.endObject();
		return jobs;
	}
	
	public List<LinkedInJob> readJobArray(JsonReader reader) throws IOException {
		List<LinkedInJob> jobs = new ArrayList<LinkedInJob>();
		
		reader.beginArray();
		while (reader.hasNext()) {
			jobs.add(readJob(reader));
		}
		reader.endArray();
		return jobs;
	}
	
	public LinkedInJob readJob(JsonReader reader) throws IOException {
		LinkedInJob job = new LinkedInJob();
		
		reader.beginObject();
		while (reader.hasNext()) {
			String name = "";
			try {
				name = reader.nextName();
			} catch (Exception e) {
				reader.skipValue();
			}
			System.out.println("name = " + name);
			if ("id".equals(name))
				job.setId(Integer.parseInt(reader.nextString()));
			else if ("postingTimestamp".equals(name)) {
				job.setPostingDate(new Date(Long.parseLong(reader.nextString())));
			} else if ("company".equals(name)) {
				readCompany(job, reader);
			} else if ("position".equals(name)) {
				readPosition(job, reader);
			}
		}
		reader.endObject();
		return job;
	}
	
	public void readCompany(LinkedInJob job, JsonReader reader) throws IOException {
		reader.beginObject();
		while (reader.hasNext()) {
			String name = "";
			System.out.println("name = " + name);
			try {
				name = reader.nextName();
			} catch (Exception e) {
				reader.skipValue();
			}
			if ("name".equals(name))
				job.setCompanyName(reader.nextString());
		}
		reader.endObject();
	}
	
	public void readPosition(LinkedInJob job, JsonReader reader) throws IOException {
		reader.beginObject();
		while (reader.hasNext()) {
			String name = "";
			System.out.println("name = " + name);
			try {
				name = reader.nextName();
			} catch (Exception e) {
				reader.skipValue();
			}
			if ("title".equals(name))
				job.setPositionTitle(reader.nextString());
			else if ("location".equals(name))
				readLocation(job, reader);
		}
		reader.endObject();
	}
	
	public void readLocation(LinkedInJob job, JsonReader reader) throws IOException {
		reader.beginObject();
		while (reader.hasNext()) {
			String name = "";
			System.out.println("name = " + name);
			try {
				name = reader.nextName();
			} catch (Exception e) {
				reader.skipValue();
			}
			if ("name".equals(name))
				job.setLocation(reader.nextString());
			
		}
		reader.endObject();
	}
}