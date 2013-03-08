package be.xios.jobfinder.connector;

import java.io.InputStream;

import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.LinkedInApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import be.xios.jobfinder.model.SearchBuilder;
import be.xios.jobfinder.util.StringUtil;

public class LinkedInConnector {

	private static final String LINKED_IN_API_KEY = "98vfgp9ikj4k";
	private static final String LINKED_IN_API_SECRET = "CxMoptwPqRISkG5f";
	private static final String LINKED_IN_API_USER_TOKEN = "222277af-c1ea-4e3d-aa5e-b6b9ed4dc77b";
	private static final String LINKED_IN_API_USER_SECRET = "64bc93d2-09c8-4fbf-af6f-7a15fff71e4c";

	private ServiceBuilder serviceBuilder;
	private OAuthService authenticationService;
	private Token accessToken;

	public LinkedInConnector() {
		serviceBuilder = new ServiceBuilder();
		serviceBuilder.provider(LinkedInApi.class);
		serviceBuilder.apiKey(LINKED_IN_API_KEY);
		serviceBuilder.apiSecret(LINKED_IN_API_SECRET);
		
		authenticationService = serviceBuilder.build();
		accessToken = new Token(LINKED_IN_API_USER_TOKEN, LINKED_IN_API_USER_SECRET);
	}

	/**
	 * Sets up the request header to use the JSON format and adds the payload
	 * of the {@link JSONObject} passed to the method. After setting up the request, it is
	 * signed and sent, after which the resulting {@link InputStream} is
	 * returned.
	 * 
	 * @param verb
	 *            The HTTP request method, either Verb.GET or Verb.POST.
	 * @param requestUrl
	 *            The request URL directing to the webservice.
	 * @param requestObject
	 *            The {@link JSONObject} to be added to the request.
	 * @return The {@link InputStream} from which JSON data can be parsed.
	 */
	public InputStream sendRequest(Verb verb, String requestUrl, SearchBuilder searchBuilder) {
		OAuthRequest request = new OAuthRequest(verb, requestUrl);
		request.addHeader("x-li-format", "json");
		request.addHeader("content-type", "application/json");
		
		if (StringUtil.isNotBlank(searchBuilder.getKeywords()))
			request.addQuerystringParameter("keywords", searchBuilder.getKeywords());
		if (StringUtil.isNotBlank(searchBuilder.getJobTitle()))
			request.addQuerystringParameter("job-title", searchBuilder.getJobTitle());
		if (StringUtil.isNotBlank(searchBuilder.getCountryCode()))
			request.addQuerystringParameter("country-code", searchBuilder.getCountryCode());
		if (StringUtil.isNotBlank(searchBuilder.getPostalCode()))
			request.addQuerystringParameter("postal-code", searchBuilder.getPostalCode());
		if (searchBuilder.getDistance() > 0)
			request.addQuerystringParameter("distance", "" + searchBuilder.getDistance());
		
		String facetsValue = "";
		if (StringUtil.isNotBlank(searchBuilder.getIndustry()))
			facetsValue += "industry";
		if (StringUtil.isNotBlank(searchBuilder.getJobFunction())) {
			facetsValue += !facetsValue.isEmpty() ? "," : "";
			facetsValue += "job-function";
		}

		if (StringUtil.isNotBlank(facetsValue))
			request.addQuerystringParameter("facets", facetsValue);
		
		if (StringUtil.isNotBlank(searchBuilder.getIndustry()))
			request.addQuerystringParameter("facet", "industry," + searchBuilder.getIndustry());
		if (StringUtil.isNotBlank(searchBuilder.getJobFunction()))
			request.addQuerystringParameter("facet", "job-function," + searchBuilder.getJobFunction());
		
		request.addQuerystringParameter("count", "20");

		authenticationService.signRequest(accessToken, request);

		Response response = request.send();
		return response.getStream();
	}
	
	
	public InputStream sendRequest(Verb verb, String requestUrl) {
		OAuthRequest request = new OAuthRequest(verb, requestUrl);
		request.addHeader("x-li-format", "json");
		request.addHeader("content-type", "application/json");
		authenticationService.signRequest(accessToken, request);
		
		Response response = request.send();
		return response.getStream();
	}
	
}