package com.me.ttrpg.tracker.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.me.gui.swing.GuiSwingSpringConfig;
import com.me.io.JsonIo;
import com.me.io.google.GoogleDriveIo;
import com.me.io.google.GoogleSheetsIo;
import com.me.util.spring.UtilSpringConfig;

@Configuration
@Import({UtilSpringConfig.class, GuiSwingSpringConfig.class})
@ComponentScan(basePackages = {"com.me.ttrpg.tracker"})
public class SpringConfig {
	@Bean
	public JsonIo jsonIo() {
		return new JsonIo();
	}

	@Bean
	public GoogleDriveIo googleDriveIo() {
		return new GoogleDriveIo();
	}

	@Bean
	public String title() {
		return "TTRPG Tracker";
	}

	@Bean
	public GoogleSheetsIo googleSheetsIo() {
		return new GoogleSheetsIo();
	}
}
