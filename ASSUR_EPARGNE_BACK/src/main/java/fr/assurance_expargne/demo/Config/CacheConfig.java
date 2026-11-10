package fr.assurance_expargne.demo.Config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Règles de cache pour le front servi par Spring Boot.
 *
 * - index.html : le navigateur doit toujours redemander la dernière version,
 *   sinon il garde l'ancienne page (et l'ancien JavaScript) après un déploiement.
 * - /assets/** : Vite met une empreinte dans le nom des fichiers (index-Ci0GFeBD.js).
 *   Un nouveau déploiement crée un nouveau nom, donc on peut les garder un an.
 *
 * On désactive Last-Modified : dans le .jar, tous les fichiers sont datés de 1980,
 * et le navigateur croirait que la page n'a jamais changé.
 */
@Configuration
public class CacheConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/index.html")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.noCache())
                .setUseLastModified(false);

        registry.addResourceHandler("/assets/**")
                .addResourceLocations("classpath:/static/assets/")
                .setCacheControl(CacheControl.maxAge(365, TimeUnit.DAYS).cachePublic())
                .setUseLastModified(false);
    }
}
