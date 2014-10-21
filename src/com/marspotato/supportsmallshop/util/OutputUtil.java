package com.marspotato.supportsmallshop.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;


public class OutputUtil {
	public static void response(HttpServletResponse r, int responseCode, String message) throws IOException {
		r.setContentType("application/json");
		r.setCharacterEncoding("UTF-8");
		r.setStatus(responseCode);
		r.getWriter().write(message);
		r.getWriter().close();
	}
}
