package com.risksense.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Factory class for creating instances of {@link XMLJSONConverterI}.
 */
@RestController
public final class ConverterFactory {

	
	@Autowired
	private JsonXmlConverter jsonXmlConverter;
    /**
     * You should implement this method having it return your version of
     * {@link com.risksense.converters.XMLJSONConverterI}.
     *
     * @return {@link com.risksense.converters.XMLJSONConverterI} implementation you created.
     */
	
	@PostMapping("/convertjsontxml")
	public ResponseEntity createXMLJSONConverter(@RequestParam("inputfilepath")String inputfilepath,
			@RequestParam("outputfilepath")String outputfilepath) {
		jsonXmlConverter.convert(inputfilepath,outputfilepath);
		return ResponseEntity.ok().build();

		
			
	}
}
