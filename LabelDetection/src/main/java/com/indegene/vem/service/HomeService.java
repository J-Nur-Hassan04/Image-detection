package com.indegene.vem.service;

import java.util.List;

import com.indegene.vem.dto.CommonDTO;

public interface HomeService {
	public List<CommonDTO> getLabels(String imageSource);

}
