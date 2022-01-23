package com.meepalika.service;

import org.springframework.stereotype.Service;

import java.util.Map;

public interface CountryService {

		public Map<String, Object> getAllCountry(int page, int size);

}
