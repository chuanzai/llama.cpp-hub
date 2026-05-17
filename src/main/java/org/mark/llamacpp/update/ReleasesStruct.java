package org.mark.llamacpp.update;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * 	用来记录Github返回的release信息。
 */
public class ReleasesStruct {

	@SerializedName("tag_name")
	private String tagName;

	private String name;

	@SerializedName("html_url")
	private String htmlUrl;

	@SerializedName("published_at")
	private String publishedAt;

	private String body;

	private boolean prerelease;

	private List<ReleaseAsset> assets;

	private ReleaseAuthor author;

	public ReleasesStruct() {
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHtmlUrl() {
		return htmlUrl;
	}

	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}

	public String getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(String publishedAt) {
		this.publishedAt = publishedAt;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public boolean isPrerelease() {
		return prerelease;
	}

	public void setPrerelease(boolean prerelease) {
		this.prerelease = prerelease;
	}

	public List<ReleaseAsset> getAssets() {
		return assets;
	}

	public void setAssets(List<ReleaseAsset> assets) {
		this.assets = assets;
	}

	public ReleaseAuthor getAuthor() {
		return author;
	}

	public void setAuthor(ReleaseAuthor author) {
		this.author = author;
	}

	public static class ReleaseAsset {

		private String name;
		private long size;

		@SerializedName("content_type")
		private String contentType;

		@SerializedName("browser_download_url")
		private String browserDownloadUrl;

		@SerializedName("download_count")
		private int downloadCount;

		public ReleaseAsset() {
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public long getSize() {
			return size;
		}

		public void setSize(long size) {
			this.size = size;
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}

		public String getBrowserDownloadUrl() {
			return browserDownloadUrl;
		}

		public void setBrowserDownloadUrl(String browserDownloadUrl) {
			this.browserDownloadUrl = browserDownloadUrl;
		}

		public int getDownloadCount() {
			return downloadCount;
		}

		public void setDownloadCount(int downloadCount) {
			this.downloadCount = downloadCount;
		}
	}

	public static class ReleaseAuthor {

		private String login;
		private long id;

		@SerializedName("avatar_url")
		private String avatarUrl;

		@SerializedName("html_url")
		private String htmlUrl;

		public ReleaseAuthor() {
		}

		public String getLogin() {
			return login;
		}

		public void setLogin(String login) {
			this.login = login;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getAvatarUrl() {
			return avatarUrl;
		}

		public void setAvatarUrl(String avatarUrl) {
			this.avatarUrl = avatarUrl;
		}

		public String getHtmlUrl() {
			return htmlUrl;
		}

		public void setHtmlUrl(String htmlUrl) {
			this.htmlUrl = htmlUrl;
		}
	}
}
