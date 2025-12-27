package com.jci.zodiac;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Main Application Class
 * JCI Danang Junior Club - Zodiac Sign HR Management System
 *
 * ♐ Built with Sagittarius Energy ♐
 * "Aim High, Lead with Optimism!"
 *
 * @author Vice President - Membership & Training
 * @version 1.0.0
 */
@EnableCaching
@SpringBootApplication
@Slf4j
public class ZodiacSignHrBeApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ZodiacSignHrBeApplication.class, args);

		logApplicationStartup(context.getEnvironment());
		printSagittariusBanner();
	}

	/**
	 * Log application startup information
	 */
	private static void logApplicationStartup(Environment env) {
		String protocol = "http";
		if (env.getProperty("server.ssl.key-store") != null) {
			protocol = "https";
		}

		String serverPort = env.getProperty("server.port");
		String contextPath = env.getProperty("server.servlet.context-path");
		if (contextPath == null || contextPath.isBlank()) {
			contextPath = "/";
		}

		String hostAddress = "localhost";
		try {
			hostAddress = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.warn("The host name could not be determined, using `localhost` as fallback");
		}

		log.info("""
            
            ----------------------------------------------------------
            🌟 JCI Danang Junior Club - Zodiac HR Management System
            ----------------------------------------------------------
            Application is running! Access URLs:
            🔗 Local:      {}://localhost:{}{}
            🔗 External:   {}://{}:{}{}
            📚 Swagger:    {}://localhost:{}{}/swagger-ui.html
            👤 Profile:    {}
            ♐ Zodiac:      Sagittarius - The Adventurer
            ----------------------------------------------------------
            """,
				protocol, serverPort, contextPath,
				protocol, hostAddress, serverPort, contextPath,
				protocol, serverPort, contextPath,
				env.getActiveProfiles().length == 0 ? env.getDefaultProfiles()[0] : env.getActiveProfiles()[0]
		);
	}

	/**
	 * Print Sagittarius-themed banner
	 */
	private static void printSagittariusBanner() {
		System.out.println("""
            
            ╔══════════════════════════════════════════════════════════════╗
            ║                  ♐ SAGITTARIUS ENERGY ♐                     ║
            ║                                                              ║
            ║        "Aim High, Lead with Optimism!"                       ║
            ║                                                              ║
            ║   🏹 Element: Fire                                           ║
            ║   ♃ Ruling Planet: Jupiter                                   ║
            ║   📅 Nov 22 - Dec 21                                         ║
            ║   🎨 Colors: Purple & Blue                                   ║
            ║                                                              ║
            ║   Built by: Vice President - Membership & Training          ║
            ║   For: JCI Danang Junior Club                                ║
            ║                                                              ║
            ║   "Managing 50-100 members with zodiac wisdom!"              ║
            ╚══════════════════════════════════════════════════════════════╝
            
            """);
	}
}