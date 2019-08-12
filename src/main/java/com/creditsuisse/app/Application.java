package com.creditsuisse.app;

import java.io.File;
import java.security.InvalidParameterException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		if (args.length == 0) {
			log.error("Json file path not provided.");
		} else {
			String logFilePath = args[0];
			File inFile = new File(logFilePath);
			if (!inFile.exists()) {
				log.error("Json file does not exist. Provided Path:" + logFilePath);
				throw new InvalidParameterException("Missing log filePath argument.");
			}
			log.info("PATH:" + logFilePath);
			System.setProperty("logFilePath", logFilePath);
		}
		
		SpringApplication.run(Application.class, args);
	}

}
