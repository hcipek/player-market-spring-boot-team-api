package com.betbull.team.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.betbull.team.model.Currency;
import com.betbull.team.model.CurrencyDto;
import com.betbull.team.model.RestCountriesResponse;
import com.betbull.team.repository.CurrencyRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CurrencyService {
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	private String restCountriesRequestBaseURL = "https://restcountries.eu/rest/v2/";
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Currency getCurrencyByCountryName(String name) {
		
		log.debug("get currency type for country {} started ", name);
		
		List<Currency> currencyList = currencyRepository.findByNameContainingIgnoreCase(name);
		
		if(!CollectionUtils.isEmpty(currencyList)) 
			return currencyList.stream().findFirst().get();
		
		String url = restCountriesRequestBaseURL.concat("name/").concat(name);
		RestCountriesResponse[] restCountriesResponseArray = restTemplate.getForObject(url, RestCountriesResponse[].class);
		CurrencyDto currencyDto = restCountriesResponseArray[0].getCurrencies().stream().findFirst().orElse(null);
		
		if(currencyDto != null) {
			Currency currency = new Currency(currencyDto.getName(), currencyDto.getCode(), currencyDto.getSymbol());
			currency = currencyRepository.save(currency);
			return currency;
		}
		
		return null;
	}

}
