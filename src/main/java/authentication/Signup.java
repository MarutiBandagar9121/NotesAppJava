package authentication;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Signup
 */

@WebServlet(
		description = "User registration servlet",
		urlPatterns = { "/signup" },
		initParams = {
				@WebInitParam(name = "databaseUrl", value = "jdbc:mysql://localhost:3306/notesappservlet"),
				@WebInitParam(name = "databaseUser", value = "root"),
				@WebInitParam(name = "password", value = "Maruti@12345")
	}
)
public class Signup extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection connection=null;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Signup() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void init() {
    	System.out.println("Signup servlet intialized");
    	
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	
    	String databaseUrl=getServletConfig().getInitParameter("databaseUrl");
		String databaseUser=getServletConfig().getInitParameter("databaseUser");
		String databasePassword=getServletConfig().getInitParameter("password");
    	
    	try {
			connection=DriverManager.getConnection(databaseUrl,databaseUser,databasePassword);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Post request executing");
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		String name=request.getParameter("name");
		int age=0;
		try {
			age=Integer.parseInt(request.getParameter("age"));
		}
		catch(NumberFormatException ne) {
			out.println("<html><head><title>Notes</title></head><body>");
			out.println("<h3>Enter an valid number in age input filed<h3>");
			out.println("<a href='/NotesApp/signup.html'>Signup page</a>");
			out.println("</body></html>");
			return;
		}
		String userName=request.getParameter("username");
		String password=request.getParameter("password");
		String confirmPassword=request.getParameter("confirm-password");
		
		if(connection !=null) {
			System.out.println("Connection Successfull");
			
			String insertQuery="insert into notesappservlet.user (name,age,username,password) values(?,?,?,?)";
			
			try {
				PreparedStatement stmt=connection.prepareStatement(insertQuery);
				stmt.setString(1, name);
				stmt.setInt(2, age);
				stmt.setString(3, userName);
				stmt.setString(4, password);
				
				if(name.length()==0) {
					out.println("<html><head><title>Notes</title></head><body>");
					out.println("<h3>Name cant be null.<h3>");
					out.println("<a href='/NotesApp/signup.html'>Signup page</a>");
					out.println("</body></html>");
				}
				else if(age<0 && age>100) {
					out.println("<html><head><title>Notes</title></head><body>");
					out.println("<h3>Age is not valid.<h3>");
					out.println("<a href='/NotesApp/signup.html'>Signup page</a>");
					out.println("</body></html>");
				}
				else if(userName.length()<8) {
					out.println("<html><head><title>Notes</title></head><body>");
					out.println("<h3>Username should be atleat of 8 chars<h3>");
					out.println("<a href='/NotesApp/signup.html'>Signup page</a>");
					out.println("</body></html>");
				}
				else if(password.length()<8) {
					out.println("<html><head><title>Notes</title></head><body>");
					out.println("<h3>Password should be atleat of 8 chars<h3>");
					out.println("<a href='/NotesApp/signup.html'>Signup page</a>");
					out.println("</body></html>");
				}
				else if(!password.equals(confirmPassword)) {
					out.println("<html><head><title>Notes</title></head><body>");
					out.println("<h3>Password dont match<h3>");
					out.println("<a href='/NotesApp/signup.html'>Signup page</a>");
					out.println("</body></html>");
				}
				
				else {
					int insertedRecords=stmt.executeUpdate();
					out.println(insertedRecords +" record inserted");
					out.println("<html><head><title>Notes</title></head><body>");
					out.println("<h3>record inserted<h3>");
					out.println("<a href='/NotesApp'>Home</a>");
					out.println("</body></html>");
				}
				
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		else {
			out.println("<html><head><title>Notes</title></head><body>");
			out.println("<h3>Database Connection Failed<h3>");
			out.println("<a href='/NotesApp'>Home</a>");
			out.println("</body></html>");
		}
		
		
	}

}
