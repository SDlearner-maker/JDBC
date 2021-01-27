import java.io.*;
import java.sql.*;
import oracle.jdbc.driver.*;
import oracle.sql.*;
import java.util.Scanner;

public class homework
{
	// Connect to the database
	public static Connection getConnection () {
		Connection conn = null;
		try {
		    // db parameters
		    String url = "jdbc:oracle:thin:@studora.comp.polyu.edu.hk:1521:dbms";
		    String user = "\"17087708d\"";
		    String password = "kazntdwm";
		    
		    // create a connection to the database
		    conn = DriverManager.getConnection(url, user, password);
		    // more processing here
		    // ...    
		} catch(SQLException e) {
			e.printStackTrace();
		}

        return conn;
    }
	
	// part a) Login to the system
	public static void main(String args[]) throws SQLException, IOException
	{
		// Connect to the database
		Connection conn = getConnection();
		System.out.println("1. Student");
		System.out.println("2. Administrator");
		
		// Get user input to login as student or administrator
		String mode = readEntry("Enter 1 for student and 2 for administrator: ");
		System.out.println();
				
		switch(mode) {
		// Login as student
		case "1":
			// Retrieve data from the database table
			PreparedStatement prepareQuery = conn.prepareStatement(
					"SELECT STUDENT_ID FROM STUDENTS WHERE STUDENT_ID = ? AND STUDENT_NAME = ?");
			
			// Get student id from user
			String sid = readEntry("Enter the student ID: ");
			String ssid = String.valueOf(sid);

			String sname = readEntry("Enter the password: ");
			String ssname = String.valueOf(sname);
			System.out.println();
			
			boolean valid = true;
			
			while (valid) {
				prepareQuery.setString(1, ssid);
				prepareQuery.setString(2, ssname);
				ResultSet rset = prepareQuery.executeQuery();
				
				// if student id and password is correct call student() function
				if (rset.next()) {
					student(ssid);
					valid = false;
					break;
				}
				// if student id or password is wrong, get user input again
				System.out.println("Student ID or password is wrong");
				System.out.println();
				
				sid = readEntry("Enter the student ID again: ");
				ssid = String.valueOf(sid);
				
				sname = readEntry("Enter the password again: ");
				ssname = String.valueOf(sname);
				
				System.out.println();
			}
			break;
			
		// Login as administrator
		// Administrator id = admin, password = 123456
		case "2":
			// Get admin id from user
			String aid = readEntry("Enter the administrator ID: ");
			String said = String.valueOf(aid);

			String pwd = readEntry("Enter the password: ");
			String spwd = String.valueOf(pwd);
			System.out.println();
			
			boolean valid2 = true;
			
			while (valid2) {
				// if admin id and password is correct call student() function
				if (said.equals("admin") && spwd.equals("123456")) {
					admin(said);
					valid2 = false;
					break;
				}
				// if student id or password is wrong, get user input again
				System.out.println("Administrator's ID or password is wrong");
				System.out.println();
				
				aid = readEntry("Enter the admin ID again: ");
				said = String.valueOf(aid);
				
				pwd = readEntry("Enter the password again: ");
				spwd = String.valueOf(pwd);
				
				System.out.println();
			}

			break;
		}
		conn.close();
	}

	// readEntry function -- Read input string
	static String readEntry(String prompt)
	{
		try
		{
			StringBuffer buffer = new StringBuffer();
			System.out.print(prompt);
			System.out.flush();
			int c = System.in.read();
			while (c != '\n' && c != -1)
			{
				buffer.append((char)c);
				c = System.in.read();
			}
			return buffer.toString().trim();
		}
		catch (IOException e)
		{
			return "";
		}
	}
	
	// Return to menu
	public static void returnMenu(String id) throws SQLException
	{
		// Get user input
		String choice = readEntry("Do you want to return to menu? (Y or N): ");
		String cchoice = choice.toUpperCase();
		System.out.println();
		
		if ("Y".equals(cchoice)) {
			// If the user is administrator, return to administrator's menu
			if ("admin".equals(id)) {
				admin(id);
			}
			
			// If the user is student, return to student's menu
			else {
				student(id);
			}
		}
		
		else {
			System.out.println("Thank you\n");
		}
	}
	
	// Menu for student
	public static void student(String ssid) throws SQLException
	{
		System.out.println("Menu for students");
		System.out.println("1. View all courses");
		System.out.println("2. View registered courses");
		System.out.println("3. Register course");
		System.out.println("4. Modify personal information");
		System.out.println();
		
		// Get user input to select the option
		String smode = readEntry("Enter the number: ");
		System.out.println();
		
		switch(smode) {
		case "1":
			System.out.println("1. View all courses");
			viewCourse();
			break;
		
		case "2":
			System.out.println("2. View registered courses");
			viewRegCourse(ssid);
			break;
			
		case "3":
			System.out.println("3. Register course");
			RegCourse(ssid);
			break;
			
		case "4":
			System.out.println("4. Modify personal information");
			modifyStudentInfo();
			break;
		}
		
		returnMenu(ssid);
	}
	
	// Menu for administrator
	public static void admin(String said) throws SQLException
	{
		System.out.println("Menu for administrator");
		System.out.println("1. View all courses or students");
		System.out.println("2. Search students with department name");
		System.out.println("3. Add course or student");
		System.out.println("4. Modify course or student information");
		System.out.println("5. Delete course or student");
		System.out.println("6. Modify student's grade");
		System.out.println("7. View top 5 students who have most courses registered");
		System.out.println("8. View top 5 students with highest average grades");
		System.out.println();
		
		String admode = readEntry("Enter the number: ");
		System.out.println();
		
		switch(admode) {
		case "1":
			String adswitch = readEntry("Enter 1 for the list of courses and 2 for the list of students: ");
			
			switch(adswitch) {
				case"1":
					System.out.println("1. View all courses");
					viewCourse();
					break;
					
				case"2":
					System.out.println("1. View all students");
					viewStudent();
					break;
			}
			break;
		
		case "2":
			System.out.println("2. Search students with department name");
			System.out.println("\nChoices of department:- ");
			System.out.println("a. Computer Science(CS)");
			System.out.println("b. Electrical Engineering(EE)");
			System.out.println("c. Information Systems(IS)");
			System.out.println("d. Management Sciences(MS)");
			System.out.println("e. School of Creative Media(SCM)");				
			
			String d = readEntry("Enter the department: ");
			String dep= String.valueOf(d);
			
			System.out.println("\nStudents in the '"+ dep + "' department: ");
			viewDepartmentStudents(dep);
			
			break;
		
		
		case "3":
			System.out.println("a. Add course");
			System.out.println("b. Add student");
			
			String a_choice = readEntry("Choose the option (a or b): ");
			String a_schoice = String.valueOf(a_choice);
			System.out.println();
			
			switch(a_schoice) {
				case "a":
					System.out.println("3. Add course");
					addCourse();
					break;
					
				case "b":
					System.out.println("3. Add student");
					addStudent();
					break;
			}
			break;
			
		case "4":
			System.out.println("a. Modify course information");
			System.out.println("b. Modify student information");
			
			String d_achoice = readEntry("Choose the option (a or b): ");
			String d_adchoice = String.valueOf(d_achoice);
			System.out.println();
			
			switch( d_adchoice ){
				case "a":
					System.out.println("4. Modify course information");
					modifyCourse();
					break;
				case "b":
					System.out.println("4. Modify student information");
					modifyStudentInfo();
					break;
			}
			
			break;
			
		case "5":
			System.out.println("a. Delete course");
			System.out.println("b. Delete student");
			
			String d_choice = readEntry("Choose the option (a or b): ");
			String d_schoice = String.valueOf(d_choice);
			System.out.println();
			
			switch(d_schoice) {
				case "a":
					System.out.println("5. Delete course");
					deleteCourse();
					break;
					
				case "b":
					System.out.println("5. Delete student");
					deleteStudent();
					break;
			}
			break;
			
		case "6":
			System.out.println("6. Modify student's grade");
			modifyGrade();
			break;
			
		case "7":
			System.out.println("7. View top 5 students who have most courses registered");
			viewMostCoursesStudents();
			break;
			
		case "8":
			System.out.println("8. View top 5 students with highest average grades");
			viewToppers();
			break;
		}
		
		returnMenu(said);
	}
	
	// part b), f) View all the courses
	public static void viewCourse() throws SQLException
	{
		Connection conn = getConnection();
		
		Statement stmt = conn.createStatement();
		// Retrieve data from database table
		ResultSet rset = stmt.executeQuery("SELECT * FROM COURSES ORDER BY COURSE_ID ASC");
		
		// Print all data from COURSES table
		while (rset.next())
		{
			System.out.println(rset.getString(1) + " " + rset.getString(2) 
			+ " " + rset.getString(3) + " " + rset.getString(4));
		}
		
		System.out.println();
		conn.close();
	}
	
	// part f) View all the students (SQL all the students)
	public static void viewStudent() throws SQLException
	{
		Connection conn = getConnection();
		
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT * FROM STUDENTS ORDER BY STUDENT_ID ASC");

		while (rset.next())
		{
			System.out.println(rset.getString(1) + " " + rset.getString(2) 
			+ " " + rset.getString(3) + " " + rset.getString(4) 
			+ " " + rset.getDate(5) + " " + rset.getString(6));
		}
		
		System.out.println();
		conn.close();
	}
	
	// part c) View user/student's registered courses
	public static void viewRegCourse(String ssid) throws SQLException
	{
		Connection conn = getConnection();
		
		// Retrieve data from database tables
		PreparedStatement prepareQuery = conn.prepareStatement(
				"SELECT * from COURSES, ENROLLMENT "
				+ "where ENROLLMENT.STUDENT_ID like " + ssid
				+ "and ENROLLMENT.COURSE_ID = COURSES.COURSE_ID");

		ResultSet rset = prepareQuery.executeQuery();

		while (rset.next())
		{
			System.out.println(rset.getString(1) + " " + rset.getString(2) 
			+ " " + rset.getString(3) + " " + rset.getString(4));
		}
		
		System.out.println();
		conn.close();
	}
	
	// part d) Register courses
	public static void RegCourse(String ssid) throws SQLException
	{
		Connection conn = getConnection();
		
		System.out.println("\nList of courses");
		
		// Show the course list
		viewCourse();
		
		// Retrieve data from database table
		PreparedStatement query = conn.prepareStatement(
				"SELECT * FROM ENROLLMENT WHERE STUDENT_ID = ? and COURSE_ID = ?");
		
		PreparedStatement query2 = conn.prepareStatement(
				"SELECT * FROM COURSES WHERE COURSE_ID = ?");

		System.out.println("Enter -1 to quit program");
		
		// Get course id from user
		String cid = readEntry("Enter the course ID: ");
		String scid = String.valueOf(cid).toUpperCase();

		// Repeat until user input is correct
		while (!"-1".equals(scid)) {
			query.setString(1, ssid);
			query.setString(2, scid);
			
			query2.setString(1, scid);
			
			ResultSet rs = query.executeQuery();
			ResultSet rs2 = query2.executeQuery();
			
			// if the course is already registered
			if (rs.next()) {
				System.out.println("ERROR: Course already registered");
				System.out.println();
			}
			
			// if user input is wrong
			else if (!rs2.next()) {
				System.out.println("ERROR: Wrong Course ID");
				System.out.println();
			}

			else {	
			// Insert enrollment data into database table
			PreparedStatement prepareQuery = conn.prepareStatement(
					"INSERT INTO ENROLLMENT (STUDENT_ID, COURSE_ID, REG_DATE) VALUES (?, ?, ?)");
			
			prepareQuery.setString(1, ssid);
			prepareQuery.setString(2, scid);
			prepareQuery.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
			
			prepareQuery.executeUpdate();
			
			System.out.println("Successfully registered the course!");
			System.out.println();
			}
			
			System.out.println("Enter -1 to quit program");
			
			cid = readEntry("Enter the COURSE ID: ");
			scid = String.valueOf(cid).toUpperCase();
			System.out.println();
		}
		
		System.out.println("Registered courses");
		viewRegCourse(ssid);
		
		conn.close();
	}
//	
//	// part e) Modify student's personal information
//	public static void modifyInfo(String ssid) throws SQLException
//	{
//		Connection conn= getConnection();		
//		System.out.println("Modify personal information:");
//
//		System.out.println("1. Change Department ");
//		System.out.println("2. Change Address");
//		//System.out.println("3. Change Birthdate");
//		System.out.println();
//
//		String smode = readEntry("Enter the number: ");
//		
//		switch(smode) {
//		case "1":
//			String sdept = readEntry("Enter the new department: ");
//			String ssdept = String.valueOf(sdept);
//			
//			PreparedStatement p= conn.prepareStatement(
//				"UPDATE STUDENTS SET DEPARTMENT = ? WHERE STUDENT_ID = ?");
//			p.setString(1, ssdept);
//			p.setString(2, ssid);
//
//			p.executeQuery();
//
//			break;
//		
//		case "2":
//			String sadd = readEntry("Enter the new address: ");
//			String ssadd = String.valueOf(sadd);
//
//
//			PreparedStatement q= conn.prepareStatement(
//				" UPDATE STUDENTS SET ADDRESS = ? WHERE STUDENT_ID = ? ");
//			q.setString(1, ssadd);
//			q.setString(2, ssid);
//			q.executeQuery();
//		
//			break;
//			}
//		
//		System.out.println();
//		conn.close();
//	}
	
	// part g) View all the students in the given department
	public static void viewDepartmentStudents(String dep) throws SQLException
	{
		Connection conn = getConnection();
		
		PreparedStatement prepareQuery = conn.prepareStatement(
				"SELECT * FROM STUDENTS WHERE DEPARTMENT=?");
		
		prepareQuery.setString(1, dep);
		ResultSet rset = prepareQuery.executeQuery();
		
		while (rset.next())
		{
			System.out.println(rset.getString(1) + " " + rset.getString(2) 
			+ " " + rset.getString(3) + " " + rset.getString(4) 
			+ " " + rset.getDate(5) + " " + rset.getString(6));
		}
		
		System.out.println();
		conn.close();
	}
	
	// part h) Add the course
	public static void addCourse() throws SQLException
	{
		Connection conn = getConnection();
		
		// Retrieve data from database table
		PreparedStatement query = conn.prepareStatement(
				"SELECT * FROM COURSES WHERE COURSE_ID = ?");
		
		System.out.println("Enter -1 to quit program");

		// Get user input
		String c_id = readEntry("Enter the course ID: ");
		String add_cid = String.valueOf(c_id).toUpperCase();
		System.out.println();

		while (!"-1".equals(add_cid)) {
			query.setString(1, add_cid);
			
			ResultSet rs = query.executeQuery();

			// if the student is already registered
			if (rs.next()) {
				System.out.println("ERROR: Course already exists");
				System.out.println();
			}

			else {	
				String c_title = readEntry("Enter the course title: ");
				String add_title = String.valueOf(c_title);
				System.out.println();
				
				String staff_name = readEntry("Enter the staff name: ");
				String add_staff = String.valueOf(staff_name);
				System.out.println();
				
				String section = readEntry("Enter the section: ");
				String add_section = String.valueOf(section).toUpperCase();
				System.out.println();
				
				// Insert course data into database table
				PreparedStatement prepareQuery = conn.prepareStatement(
						"INSERT INTO COURSES (COURSE_ID, COURSE_TITLE, STAFF_NAME, SECTION) VALUES (?, ?, ?, ?)");
				
				prepareQuery.setString(1, add_cid);
				prepareQuery.setString(2, add_title);
				prepareQuery.setString(3, add_staff);
				prepareQuery.setString(4, add_section);
				
				prepareQuery.executeUpdate();
				
				System.out.println("Successfully added the course!");
				System.out.println();
				
				System.out.println("List of courses");
				viewCourse();
			}
			
			System.out.println("Enter -1 to quit program");
			
			c_id = readEntry("Enter the course ID: ");
			add_cid = String.valueOf(c_id).toUpperCase();
			System.out.println();
		}
		
		System.out.println();
		conn.close();
	}
	
	// part h) Add the student
	public static void addStudent() throws SQLException
	{
		Connection conn = getConnection();
		
		// Retrieve data from database table
		PreparedStatement query = conn.prepareStatement(
				"SELECT * FROM STUDENTS WHERE STUDENT_ID = ?");
		
		System.out.println("Enter -1 to quit program");
		
		// Get user input
		String s_id = readEntry("Enter the student ID: ");
		String add_sid = String.valueOf(s_id);
		System.out.println();
		
		while (!"-1".equals(add_sid)) {
			query.setString(1, add_sid);
			
			ResultSet rs = query.executeQuery();

			// if the student is already registered
			if (rs.next()) {
				System.out.println("ERROR: Student already exists");
				System.out.println();
			}

			else {	
				String stu_name = readEntry("Enter the student name: ");
				String add_sname = String.valueOf(stu_name);
				System.out.println();
				
				System.out.println("Choices of department:- ");
				System.out.println("a. Computer Science(CS)");
				System.out.println("b. Electrical Engineering(EE)");
				System.out.println("c. Information Systems(IS)");
				System.out.println("d. Management Sciences(MS)");
				System.out.println("e. School of Creative Media(SCM)");
				System.out.println("f. New department which isn't metioned above");
				System.out.println();

				String doption = readEntry("Choose the option for department (a or b or c or d or e or f): ");
				
				String department = "";
				switch(doption){
				case "a":
					department = "CS";
					break;
					
				case "b":
					department = "EE";
					break;
					
				case "c":
					department = "IS";
					break;
					
				case "d":
					department = "MS";
					break;
					
				case "e":
					department = "SCM";
					break;
				
				case "f":
					department = readEntry("Enter the new department: ");
					break;
				
				}

				String add_depart = String.valueOf(department);
				System.out.println();
				
				System.out.println("Choices of address:- ");
				System.out.println("i. Department of Computer Science, City University of Hong Kong");
				System.out.println("ii. Department of Electronic Engineering, City University of Hong Kong");
				System.out.println("iii. Department of Information Systems, City University of Hong Kong");
				System.out.println("iv. Department of Management Sciences, City University of Hong Kong");
				System.out.println("v. School of Creative Media, City University of Hong Kong");
				System.out.println("vi. Independent address outside the student halls mentioned above");
				System.out.println();
				

				String aoption = readEntry("Choose the option for address: ");
				String address="";

				switch(aoption){
				case "i":
					address= "Department of Computer Science, City University of Hong Kong";
					break;
				case "ii":
					address= "Department of Electronic Engineering, City University of Hong Kong";
					break;
				case "iii":
					address= "Department of Information Systems, City University of Hong Kong";
					break;
				case "iv":
					address= "Department of Management Sciences, City University of Hong Kong";
					break;
				case "v":
					address= "School of Creative Media, City University of Hong Kong";
					break;
				case "vi":
					address= readEntry("Enter the new address: ");
					break;
				}

				String add_addr = String.valueOf(address);
				System.out.println();
				
				String birthdate = readEntry("Enter the birthdate in this format (YYYY-MM-DD): ");
				String add_birth = String.valueOf(birthdate);
				System.out.println();
				
				System.out.println("Choices of gender:- ");
				System.out.println("a. Male");
				System.out.println("b. Female");
				System.out.println();
				
				String goption = readEntry("Choose the option for gender (a or b): ");
				String gender = "";
				
				switch(goption) {
				case "a":
					gender = "MALE";
					break;
					
				case "b":
					gender = "FEMALE";
					break;
				}

				String add_gender = String.valueOf(gender);
				System.out.println();
				
				// Insert student data into database table
				PreparedStatement prepareQuery = conn.prepareStatement(
						"INSERT INTO STUDENTS (STUDENT_ID, STUDENT_NAME, DEPARTMENT, ADDRESS, BIRTHDATE, GENDER) VALUES (?, ?, ?, ?, ?, ?)");
				
				prepareQuery.setString(1, add_sid);
				prepareQuery.setString(2, add_sname);
				prepareQuery.setString(3, add_depart);
				prepareQuery.setString(4, add_addr);
				prepareQuery.setDate(5, java.sql.Date.valueOf(add_birth));
				prepareQuery.setString(6, add_gender);
				
				prepareQuery.executeUpdate();
				
				System.out.println("Successfully added the student!");
				System.out.println();
				
				System.out.println("List of students");
				viewStudent();
			}
			
			System.out.println("Enter -1 to quit program");
			
			s_id = readEntry("Enter the student ID: ");
			add_sid = String.valueOf(s_id);
			System.out.println();
		}
		
		System.out.println();
		conn.close();
	}
	
	// part i) Modify the course information
	public static void modifyCourse() throws SQLException
	{
		Connection conn = getConnection();
	
		PreparedStatement query = conn.prepareStatement(
					"SELECT * FROM COURSES WHERE COURSE_ID = ?");
		
		System.out.println("Enter -1 to quit program");

		String c_id = readEntry("Enter the course ID: ");
		String cidToBeMod = String.valueOf(c_id).toUpperCase();
		System.out.println();
	
		while (!"-1".equals(cidToBeMod)) {
			query.setString(1, cidToBeMod);
			
			ResultSet rs = query.executeQuery();
			
			if(rs.next()){
				System.out.println("1. Modify course id");
				System.out.println("2. Modify course title");
				System.out.println("3. Modify staff name");
				System.out.println("4. Modify course section");
				
				String ad_choice = readEntry("Enter the number (1 or 2 or 3 or 4): ");
				
				switch( ad_choice ){
				case"1":
					String newc_id = readEntry("Enter the new course ID: ");
					String modc_id = String.valueOf(newc_id);
		
					PreparedStatement p1 = conn.prepareStatement(
						"UPDATE COURSES SET COURSE_ID = ? WHERE COURSE_ID = ?");
					p1.setString(1, modc_id);
					p1.setString(2, cidToBeMod);

					p1.executeQuery();
					
					System.out.println();
					viewCourseInfo(modc_id);
					
					break;
					
				case"2":
					String newCT = readEntry("Enter the new course title: ");
					String modCT = String.valueOf(newCT);
		
					PreparedStatement p2 = conn.prepareStatement(
						"UPDATE COURSES SET COURSE_TITLE = ? WHERE COURSE_ID = ?");
					p2.setString(1, modCT);
					p2.setString(2, cidToBeMod);

					p2.executeQuery();
					
					System.out.println();
					viewCourseInfo(cidToBeMod);
					
					break;
					
				case"3":
					String newSN = readEntry("Enter the new staff name: ");
					String modSN = String.valueOf(newSN);
		
					PreparedStatement p3 = conn.prepareStatement(
						"UPDATE COURSES SET STAFF_NAME = ? WHERE COURSE_ID = ?");
					p3.setString(1, modSN);
					p3.setString(2, cidToBeMod);

					p3.executeQuery();
					
					System.out.println();
					viewCourseInfo(cidToBeMod);
					
					break;
					
				case"4":
					String newSec = readEntry("Enter the new section: ");
					String modSec = String.valueOf(newSec);
		
					PreparedStatement p4 = conn.prepareStatement(
						"UPDATE COURSES SET SECTION = ? WHERE COURSE_ID = ?");
					p4.setString(1, modSec);
					p4.setString(2, cidToBeMod);

					p4.executeQuery();
					
					System.out.println();
					viewCourseInfo(cidToBeMod);
					
					break;
				}
			
			}
			
			else {
				System.out.println("ERROR: Course doesn't exist");
				System.out.println();
			}
			
			System.out.println("Enter -1 to quit program");
			
			c_id = readEntry("Enter the course ID: ");
			cidToBeMod = String.valueOf(c_id).toUpperCase();
			System.out.println();
		}
		
		System.out.println();
		conn.close();

	}

	// part e) and i) Modify student information
	public static void modifyStudentInfo() throws SQLException
	{
		Connection conn = getConnection();
	
		PreparedStatement query = conn.prepareStatement(
					"SELECT * FROM STUDENTS WHERE STUDENT_ID = ?");

		System.out.println("Enter -1 to quit program");

		String std_id = readEntry("Enter the Student ID: ");
		String stdToBeMod = String.valueOf(std_id);
		System.out.println();
	
		while (!"-1".equals(stdToBeMod)) {
			query.setString(1, stdToBeMod);
			
			ResultSet rs = query.executeQuery();
			
			if(rs.next()){
				System.out.println("1. Modify Student ID");
				System.out.println("2. Modify Student Name");
				System.out.println("3. Modify Department");
				System.out.println("4. Modify Address");
				System.out.println("5. Modify Birthdate in this format (YYYY-MM-DD)");
				System.out.println("6. Modify Gender");
				
				String std_choice = readEntry("Enter the number (1 or 2 or 3 or 4 or 5 or 6): ");
				System.out.println();
				
				switch(std_choice){
					case"1":
						String newstd_id = readEntry("Enter the new Student ID: ");
						String modstd_id = String.valueOf(newstd_id);
			
						PreparedStatement p1 = conn.prepareStatement(
							"UPDATE STUDENTS SET STUDENT_ID = ? WHERE STUDENT_ID = ?");
						p1.setString(1, modstd_id);
						p1.setString(2, stdToBeMod);

						p1.executeQuery();
						
						System.out.println();
						viewPersonalInfo(modstd_id);
						
						break;
						
					case"2":
						String newStdN = readEntry("\nEnter the new Student Name: ");
						String modStdN = String.valueOf(newStdN);
			
						PreparedStatement p2 = conn.prepareStatement(
							"UPDATE STUDENTS SET STUDENT_NAME = ? WHERE STUDENT_ID = ?");
						p2.setString(1, modStdN);
						p2.setString(2, stdToBeMod);

						p2.executeQuery();
						
						System.out.println();
						viewPersonalInfo(stdToBeMod);
						
						break;
						
					case"3":
						System.out.println("\nChoices of department:- ");
						System.out.println("a. Computer Science(CS)");
						System.out.println("b. Electrical Engineering(EE)");
						System.out.println("c. Information Systems(IS)");
						System.out.println("d. Management Sciences(MS)");
						System.out.println("e. School of Creative Media(SCM)");
						
						System.out.println();

						String doption = readEntry("Choose the option for new department: ");
						String newSd ="";
						
						switch(doption){
						case "a":
							newSd = "CS";
							break;
							
						case "b":
							newSd = "EE";
							break;
							
						case "c":
							newSd = "IS";
							break;
							
						case "d":
							newSd = "MS";
							break;
							
						case "e":
							newSd = "SCM";
							break;
							
						
						}
			
						String modSd = String.valueOf(newSd);

						PreparedStatement p3 = conn.prepareStatement(
							"UPDATE STUDENTS SET DEPARTMENT = ? WHERE STUDENT_ID = ?");
						
						p3.setString(1, modSd);
						p3.setString(2, stdToBeMod);
						
						p3.executeQuery();		
						
						viewPersonalInfo(stdToBeMod);
						
						break;					

						
					case"4":
						System.out.println("\nChoices of address:- ");
						System.out.println("i. Department of Computer Science, City University of Hong Kong");
						System.out.println("ii. Department of Electronic Engineering, City University of Hong Kong");
						System.out.println("iii. Department of Information Systems, City University of Hong Kong");
						System.out.println("iv. Department of Management Sciences, City University of Hong Kong");
						System.out.println("v. School of Creative Media, City University of Hong Kong");
						System.out.println("vi. Independent address outside the student halls mentioned above");

						System.out.println();

						String aoption = readEntry("Enter the option for new address: ");
						String newadd="";

						switch(aoption){

						case "i":
							newadd= "Department of Computer Science, City University of Hong Kong";
							break;
							
						case "ii":
							newadd= "Department of Electronic Engineering, City University of Hong Kong";
							break;
							
						case "iii":
							newadd= "Department of Information Systems, City University of Hong Kong";
							break;
							
						case "iv":
							newadd= "Department of Management Sciences, City University of Hong Kong";
							break;
							
						case "v":
							newadd= "School of Creative Media, City University of Hong Kong";
							break;
							
						case "vi":
							newadd= readEntry("Enter the new address: ");
							break;
						}
						
						String modadd = String.valueOf(newadd);
			
						PreparedStatement p4 = conn.prepareStatement(
							"UPDATE STUDENTS SET ADDRESS = ? WHERE STUDENT_ID = ?");
						p4.setString(1, modadd);
						p4.setString(2, stdToBeMod);

						p4.executeQuery();
						
						viewPersonalInfo(stdToBeMod);
						
						break;
						
					case "5":
						String newbd = readEntry("Enter the new Birthdate in this format(YYYY-MM-DD): ");
						String modbd = String.valueOf(newbd);
			
						PreparedStatement p5 = conn.prepareStatement(
							"UPDATE STUDENTS SET BIRTHDATE = ? WHERE STUDENT_ID = ?");
						p5.setDate(1, java.sql.Date.valueOf(modbd));
						p5.setString(2, stdToBeMod);

						p5.executeQuery();
						
						viewPersonalInfo(stdToBeMod);
						
						break;
						
					case "6":
						System.out.println("\nChoices of gender:- ");
						System.out.println("a. Male");
						System.out.println("b. Female");
						System.out.println();
						
						String goption = readEntry("Choose the new Gender: ");
						String newgd = "";
						
						switch(goption) {
						case "a":
							newgd = "MALE";
							break;
							
						case "b":
							newgd = "FEMALE";
							break;
						}
						
						String modgd = String.valueOf(newgd);
						
						PreparedStatement p6 = conn.prepareStatement(
							"UPDATE STUDENTS SET GENDER = ? WHERE STUDENT_ID = ?");
						p6.setString(1, modgd);
						p6.setString(2, stdToBeMod);

						p6.executeQuery();
						
						viewPersonalInfo(stdToBeMod);
						
						break;
				}
			
			}
			
			else {
				System.out.println("ERROR: Student doesn't exist");
				System.out.println();
			}
			
			System.out.println("Enter -1 to quit program");
			
			std_id = readEntry("Enter the Student ID: ");
			stdToBeMod = String.valueOf(std_id);
			System.out.println();
		}
		
		System.out.println();
		conn.close();
	}
	
	// part j) Delete the course
	public static void deleteCourse() throws SQLException
	{
		Connection conn = getConnection();
		
		// Retrieve data from database table
		PreparedStatement query = conn.prepareStatement(
				"SELECT * FROM COURSES WHERE COURSE_ID = ?");
		
		System.out.println("Enter -1 to quit program");

		// Get user input
		String c_id = readEntry("Enter the course ID: ");
		String del_cid = String.valueOf(c_id).toUpperCase();
		System.out.println();

		while (!"-1".equals(del_cid)) {
			query.setString(1, del_cid);
			
			ResultSet rs = query.executeQuery();

			// if the course exists, delete the course
			if (rs.next()) {
				PreparedStatement prepareQuery = conn.prepareStatement(
						"DELETE FROM COURSES WHERE COURSE_ID = ?");
				
				prepareQuery.setString(1, del_cid);

				prepareQuery.executeUpdate();
				
				System.out.println("Successfully deleted the course!");
				System.out.println();
				
				System.out.println("List of courses");
				viewCourse();
			}
			
			// Course doesn't exist in database table
			else {	
				System.out.println("ERROR: Course doesn't exist");
				System.out.println();
			}
			
			System.out.println("Enter -1 to quit program");
			
			c_id = readEntry("Enter the course ID: ");
			del_cid = String.valueOf(c_id).toUpperCase();
			System.out.println();
		}
		
		System.out.println();
		conn.close();
	}
	
	// part j) Delete the student
	public static void deleteStudent() throws SQLException
	{
		Connection conn = getConnection();
		
		// Retrieve data from database table
		PreparedStatement query = conn.prepareStatement(
				"SELECT * FROM STUDENTS WHERE STUDENT_ID = ?");

		System.out.println("Enter -1 to quit program");

		// Get user input
		String s_id = readEntry("Enter the student ID: ");
		String del_sid = String.valueOf(s_id);
		System.out.println();

		while (!"-1".equals(del_sid)) {
			query.setString(1, del_sid);
			
			ResultSet rs = query.executeQuery();

			// if the student exists, delete the student
			if (rs.next()) {
				PreparedStatement prepareQuery = conn.prepareStatement(
						"DELETE FROM STUDENTS WHERE STUDENT_ID = ?");
				
				prepareQuery.setString(1, del_sid);

				prepareQuery.executeUpdate();
				
				System.out.println("Successfully deleted the student!");
				System.out.println();
				
				System.out.println("List of students");
				viewStudent();
			}

			else {	
				System.out.println("ERROR: Student doesn't exist");
				System.out.println();
			}
			
			System.out.println("Enter -1 to quit program");
			
			s_id = readEntry("Enter the student ID: ");
			del_sid = String.valueOf(s_id);
			System.out.println();
		}
		
		System.out.println();
		conn.close();
	}
	
	// part k) Modify the grade of a student for one of his/her registered courses
	public static void modifyGrade() throws SQLException{
		Connection conn = getConnection();
	
		PreparedStatement query = conn.prepareStatement(
					"SELECT * FROM ENROLLMENT WHERE STUDENT_ID = ?");
		
		System.out.println("Enter -1 to quit program");
	
		String std_id = readEntry("Enter the Student ID to modify his/her grade: ");
		String stdToBeMod = String.valueOf(std_id);
		System.out.println();
		
		while (!"-1".equals(stdToBeMod)) {
			query.setString(1, stdToBeMod);
			
			ResultSet rs = query.executeQuery();
		
			if(rs.next()){
				String modC = readEntry("Enter the Course ID to modify his/her grade: ");
				String stdC = String.valueOf(modC).toUpperCase();
				System.out.println();
				
				PreparedStatement query2 = conn.prepareStatement(
						"SELECT COURSE_ID FROM ENROLLMENT WHERE STUDENT_ID = ? AND COURSE_ID = ?");
				
				query2.setString(1, stdToBeMod);
				query2.setString(2, stdC);
				ResultSet rs2 = query2.executeQuery();
				
				if(rs2.next()){
					Scanner input = new Scanner(System.in);
					System.out.print("Enter the new Grade: ");
					
					int newG = input.nextInt();
						int modG = newG;
			
						PreparedStatement p = conn.prepareStatement(
							"UPDATE ENROLLMENT SET GRADE = ? WHERE STUDENT_ID = ? AND COURSE_ID = ?");
						p.setInt(1, modG);
						p.setString(2, stdToBeMod);
						p.setString(3, stdC);
	
						p.executeQuery();
						
						System.out.println("The student's enrollment");
						viewEnrolInfo(stdToBeMod);
						
						break;
				}
				
				else {
					System.out.println("ERROR: The Student does not take this course");
					System.out.println();
				}
			}
		
			else {
				System.out.println("ERROR: Student doesn't exist");
				System.out.println();
			}
		
			System.out.println("Enter -1 to quit program");
			
			std_id = readEntry("Enter the Student ID: ");
			stdToBeMod = String.valueOf(std_id);
			System.out.println();
		}
		
		System.out.println();
		conn.close();
	}
	
	// part l) top 5 students who have most courses registered
	public static void viewMostCoursesStudents() throws SQLException
	{
		Connection conn = getConnection();
		
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT COUNT(COURSE_ID), STUDENT_ID FROM ENROLLMENT GROUP BY STUDENT_ID ORDER BY COUNT(COURSE_ID) DESC");
		
		int count=1;
		while (rset.next())
		{
			
			System.out.println("Courses taken: "+rset.getString(1));
			viewPersonalInfo(rset.getString(2));
			count++;
			if (count>5){break;}
		}
		
		System.out.println();
		conn.close();
	}

	// part m) view top 5 students with highest average grades
	public static void viewToppers() throws SQLException
	{
		Connection conn = getConnection();
		
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT AVG(GRADE), STUDENT_ID FROM ENROLLMENT GROUP BY STUDENT_ID ORDER BY AVG(GRADE) DESC");
		
		int count=1;								
		while (rset.next())
		{
			System.out.println("Average grade: "+rset.getString(1));
			viewPersonalInfo(rset.getString(2));
			count++;
			if (count>5){break;}
		}
		
		System.out.println();
		conn.close();
	}

	// View enrollment
	public static void viewEnrollment() throws SQLException
	{
		Connection conn = getConnection();
		
		Statement stmt = conn.createStatement();
		ResultSet rset = stmt.executeQuery("SELECT * FROM ENROLLMENT ORDER BY STUDENT_ID ASC");
		
		System.out.println("View enrollment");
		
		while (rset.next())
		{
			System.out.println(rset.getString(1) + " " + rset.getString(2) 
			+ " " + rset.getString(3) + " " + rset.getString(4));
		}
		
		System.out.println();
		conn.close();
	}
	
	// View student's personal information
	public static void viewPersonalInfo(String ssid) throws SQLException
	{
		Connection conn = getConnection();
		
		PreparedStatement prepareQuery = conn.prepareStatement(
				"SELECT * FROM STUDENTS WHERE STUDENT_ID=?");
		
		prepareQuery.setString(1, ssid);
		ResultSet rset = prepareQuery.executeQuery();
		
		System.out.println("View student's personal information");
		
		while (rset.next())
		{
			System.out.println(rset.getString(1) + " " + rset.getString(2) 
			+ " " + rset.getString(3) + " " + rset.getString(4)
			+ " " + rset.getDate(5) + " " + rset.getString(6));
		}
		
		System.out.println();
		conn.close();
	}
	
	// View course information
	public static void viewCourseInfo(String id) throws SQLException
	{
		Connection conn = getConnection();
		
		PreparedStatement prepareQuery = conn.prepareStatement(
				"SELECT * FROM COURSES WHERE COURSE_ID=?");
		
		prepareQuery.setString(1, id);
		ResultSet rset = prepareQuery.executeQuery();
		
		System.out.println("View course information");
		
		while (rset.next())
		{
			System.out.println(rset.getString(1) + " " + rset.getString(2) 
			+ " " + rset.getString(3) + " " + rset.getString(4));
		}
		
		System.out.println();
		conn.close();
	}
	
	// View student's personal information
		public static void viewEnrolInfo(String ssid) throws SQLException
		{
			Connection conn = getConnection();
			
			PreparedStatement prepareQuery = conn.prepareStatement(
					"SELECT * FROM ENROLLMENT WHERE STUDENT_ID=?");
			
			prepareQuery.setString(1, ssid);
			ResultSet rset = prepareQuery.executeQuery();
			
			System.out.println("View student's enrollment");
			
			while (rset.next())
			{
				System.out.println(rset.getString(1) + " " + rset.getString(2) 
				+ " " + rset.getDate(3) + " " + rset.getString(4));
			}
			
			System.out.println();
			conn.close();
		}
		
}
