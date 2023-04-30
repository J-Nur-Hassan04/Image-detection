package com.indegene.vem.service;

import java.io.File;
import java.util.Map;

public interface HomeService {
	public Map<String, Object> getLabels(String imageSource);
	public void separateImagesInPdf(File file);

}
