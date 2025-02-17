package com.hormattalah.navette_autocars.util;

import jakarta.servlet.http.HttpServletRequest;

public class Utility {

    public static String getSiteURL(HttpServletRequest request){
        String siteUrl = request.getRequestURL().toString();
        return  siteUrl.replace(request.getServletPath(),"");

    }
}