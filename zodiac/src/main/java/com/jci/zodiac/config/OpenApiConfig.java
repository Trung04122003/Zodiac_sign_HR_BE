package com.jci.zodiac.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI (Swagger) Configuration
 * Provides interactive API documentation
 */
@Configuration
public class OpenApiConfig {

    @Value("${server.servlet.context-path:/api}")
    private String contextPath;

    @Bean
    public OpenAPI zodiacHrOpenAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080" + contextPath);
        localServer.setDescription("Local Development Server");

        Contact contact = new Contact();
        contact.setName("Vice President - Membership & Training");
        contact.setEmail("vp.membership@jcidanang.com");
        contact.setUrl("https://jcidanang.com");

        License license = new License();
        license.setName("MIT License");
        license.setUrl("https://opensource.org/licenses/MIT");

        Info info = new Info()
                .title("♐ Zodiac Sign HR Management API")
                .version("1.0.0")
                .description("""
                    # JCI Danang Junior Club - Zodiac HR Management System
                    
                    **Manage 50-100 members with zodiac wisdom!**
                    
                    ## Features:
                    - 👥 Member Management (CRUD operations)
                    - ♈-♓ Zodiac Profile Database (12 signs)
                    - 🤝 Compatibility Calculator (One-on-One & Team)
                    - 🏢 Department & Team Management
                    - 📊 Analytics & Dashboard
                    - 📝 Notes & Observations
                    
                    ## Zodiac Elements:
                    - 🔥 Fire: Aries, Leo, Sagittarius
                    - 🌍 Earth: Taurus, Virgo, Capricorn
                    - 💨 Air: Gemini, Libra, Aquarius
                    - 💧 Water: Cancer, Scorpio, Pisces
                    
                    ---
                    
                    **Built with Sagittarius Energy ♐**
                    
                    *"Aim High, Lead with Optimism!"*
                    """)
                .contact(contact)
                .license(license);

        return new OpenAPI()
                .info(info)
                .servers(List.of(localServer));
    }
}