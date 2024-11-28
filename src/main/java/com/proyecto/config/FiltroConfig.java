package com.proyecto.config;

import com.proyecto.filtros.RolFiltro;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FiltroConfig {

    @Bean
    public FilterRegistrationBean<RolFiltro> filtroRol() {
        FilterRegistrationBean<RolFiltro> registro = new FilterRegistrationBean<>();
        registro.setFilter(new RolFiltro());
        registro.addUrlPatterns("/admin/*"); // Aplicar el filtro a las rutas de admin
        registro.addUrlPatterns("/gestor/*"); // Aplicar el filtro a las rutas de admin
        registro.addUrlPatterns("/gerente/*"); // Aplicar el filtro a las rutas de admin
        registro.addUrlPatterns("/trabajador/*"); // Aplicar el filtro a las rutas de admin

        registro.setOrder(1); // Prioridad del filtro
        return registro;
    }
}
