package web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JsonObject;
import org.json.simple.Jsoner;

import model.UserFacade;
import model.WxUser;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet({ "/RegisterServlet", "/r" })
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB
	UserFacade uf;

	public RegisterServlet() {
		super();
	}

	public WxUser RegisterBean (String u, String p){
		WxUser r = null;
		System.out.println("Register Bean, u="+u+" p="+p);
		r = uf.registerUser(u, p);
		return r;
	}


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().println("\n\n\nRegisterServletTest\n\n\n");
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.addHeader("SERVLET_STATUS", "ok");
		response.setStatus(200);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		log(request.toString()); log(response.toString());

//		response.setContentType("application/json"); 
		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();  
		
		final Boolean useSecureCookie = false;
		final int expiryTime = 60 * 60 * 8;  // 1h in seconds
		final String cookiePath = "/";
		
		JsonObject loginJson = Jsoner.deserialize(request.getReader().readLine(), new JsonObject());

		String username = loginJson.getString("username");
		String password = loginJson.getString("password");

		username = request.getParameter("username");
		password = request.getParameter("password");

		WxUser u = RegisterBean(username, password);

		if (u != null) {

			out.print("Register Success for user: " + u);  

			Cookie tmc = new Cookie("TimeMachine_cookie", u.getUserName());
			Cookie uid = new Cookie("TImeMachine_uid", String.valueOf(u.getId()));
			tmc.setSecure(useSecureCookie); 
			tmc.setMaxAge(expiryTime); 
			tmc.setPath(cookiePath);
			uid.setSecure(useSecureCookie); uid.setMaxAge(expiryTime); uid.setPath(cookiePath);
			response.addCookie(tmc); response.addCookie(uid);

			response.getWriter().print("success");
			response.addHeader("LOGIN_STATUS", "SUCCESS");
			request.setAttribute("user", u);
			response.sendRedirect("/TimeMachine/");
			response.setStatus(200);

		} else {
			response.getWriter().append("Error Duplicate Entry").append(request.getContextPath());
			out.print("{\"success\": false, \"message\": \"Error. Username already taken. Find another.\"}");  
			response.setStatus(401);
			request.setAttribute("user", "");
			response.addHeader("LOGIN_STATUS", "FAILURE");
			response.sendRedirect("/TimeMachine/register/");
		}
	}
}
