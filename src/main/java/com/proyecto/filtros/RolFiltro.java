package com.proyecto.filtros;

import com.proyecto.entidades.Usuario;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RolFiltro implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialización, si es necesaria
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Obtener el usuario de la sesión
        Usuario usuario = (Usuario) httpRequest.getSession().getAttribute("usuario");

        // Verificar que haya un usuario en sesión
        if (usuario == null) {
            httpResponse.sendRedirect("/login"); // Redirigir al login
            return;
        }

        // Verificar si tiene permisos para acceder a la ruta
        String rol = usuario.getRol().getNombre();
        String uri = httpRequest.getRequestURI();

        // Restringir el acceso a rutas específicas
        if (uri.startsWith("/admin") && !"ADMIN".equals(rol)) {
            httpResponse.sendRedirect("/"); // Redirigir al login si no es ADMIN
            return;
        }
        if (uri.startsWith("/gestor") && !"GESTOR".equals(rol)) {
            httpResponse.sendRedirect("/"); // Redirigir al login si no es GESTOR
            return;
        }
        if (uri.startsWith("/gerente") && !"GERENTE".equals(rol)) {
            httpResponse.sendRedirect("/"); // Redirigir al login si no es GESTOR
            return;
        }
        if (uri.startsWith("/trabajador") && !"TRABAJADOR".equals(rol)) {
            httpResponse.sendRedirect("/"); // Redirigir al login si no es GESTOR
            return;
        }
        // Si pasa las validaciones, continuar con la solicitud
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Limpieza, si es necesaria
    }
}
