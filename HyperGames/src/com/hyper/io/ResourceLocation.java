package com.hyper.io;

import java.io.InputStream;

public class ResourceLocation {
	public static final String RESOURCE_FILE = "/com/hyper/resources/";
	private String path;
	
	public ResourceLocation(String file) {
		this.path = file;
	}
	
	public InputStream getAsStream() {
		return ResourceLocation.class.getResourceAsStream(RESOURCE_FILE + path);
	}
}
