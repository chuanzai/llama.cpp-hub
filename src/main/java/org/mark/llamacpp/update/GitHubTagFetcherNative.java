package org.mark.llamacpp.update;

import java.io.FileWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.mark.llamacpp.server.BuildInfo;

public class GitHubTagFetcherNative {

	private static final String API_URL = "https://api.github.com/repos/IIIIIllllIIIIIlllll/llama.cpp-hub/releases/latest";
	private static final String CACHE_DIR = "cache";
	private static final String CACHE_FILE = CACHE_DIR + "/latest.json";
	private static final String CURRENT_TAG = BuildInfo.getTag();

	private final Gson gson;
	private final HttpClient client;

	public GitHubTagFetcherNative() {
		this.gson = new GsonBuilder().setPrettyPrinting().create();
		this.client = HttpClient.newHttpClient();
	}

	public static void main(String[] args) {
		GitHubTagFetcherNative fetcher = new GitHubTagFetcherNative();
		CheckResult result = fetcher.check();
		System.out.println(result.toJson());
	}

	public CheckResult check() {
		try {
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(API_URL))
					.header("Accept", "application/vnd.github+json")
					.GET()
					.build();

			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() != 200) {
				return CheckResult.error("HTTP " + response.statusCode());
			}

			String body = response.body();
			ReleasesStruct release = gson.fromJson(body, ReleasesStruct.class);

			saveToCache(release);

			String latestTag = release.getTagName();
			boolean hasUpdate = hasUpdate(latestTag);

			return CheckResult.ok(release, hasUpdate);

		} catch (Exception e) {
			return CheckResult.error(e.getMessage());
		}
	}

	private void saveToCache(ReleasesStruct release) {
		try {
			Files.createDirectories(Paths.get(CACHE_DIR));
			try (FileWriter w = new FileWriter(CACHE_FILE, StandardCharsets.UTF_8)) {
				gson.toJson(release, w);
			}
		} catch (Exception e) {
			System.err.println("saveToCache error: " + e.getMessage());
		}
	}

	private boolean hasUpdate(String latestTag) {
		if (CURRENT_TAG == null || CURRENT_TAG.isEmpty() || CURRENT_TAG.contains("{tag}")) {
			return false;
		}
		return !CURRENT_TAG.equals(latestTag);
	}

	public static class CheckResult {

		private final boolean success;
		private final ReleasesStruct release;
		private final boolean hasUpdate;
		private final String error;

		private CheckResult(boolean success, ReleasesStruct release, boolean hasUpdate, String error) {
			this.success = success;
			this.release = release;
			this.hasUpdate = hasUpdate;
			this.error = error;
		}

		public static CheckResult ok(ReleasesStruct release, boolean hasUpdate) {
			return new CheckResult(true, release, hasUpdate, null);
		}

		public static CheckResult error(String error) {
			return new CheckResult(false, null, false, error);
		}

		public boolean isSuccess() {
			return success;
		}

		public ReleasesStruct getRelease() {
			return release;
		}

		public boolean isHasUpdate() {
			return hasUpdate;
		}

		public String getError() {
			return error;
		}

		public String toJson() {
			Gson g = new GsonBuilder().setPrettyPrinting().create();
			return g.toJson(this);
		}
	}

	public static String getCurrentTag() {
		return CURRENT_TAG;
	}
}
