//This program is a project management system built to assist a project management firm

import java.sql.*;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.io.*;
import java.util.Date;

public class ProjectManager {

    // This method will be called when adding a new project to the database
    public static void newProject() {

        try {

            // We will automatically generate a new project number to avoid duplicates
            int projNumber = 1;  // The first project will always be 1
            int highestNum = 0;  // There will be 0 projects to start with

            // connecting to the "PoisePMS" database, which contains the relevant tables
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PoisePMS?useSSL=false",
                    "bootCamp",
                    "hypDevz!!");

            Statement statement = connection.createStatement();
            ResultSet results;


            Scanner input = new Scanner(System.in);

            // Getting the architect's information
            System.out.println("Enter the architect's FIRST NAME: ");
            String archFirstName = input.next();
            System.out.println("Enter the architect's SURNAME: ");
            String archSurName = input.next();
            String archName = archFirstName + " " + archSurName;  // combining both strings to get the full name

            System.out.println("Enter the architect's phone number: ");
            String archTelephone = input.next();

            System.out.println("Enter the architect's email: ");
            String archEmail = input.next();

            System.out.println("Enter the architect's address: ");
            String archAddress = input.next();
            archAddress += input.nextLine();  // for strings with a space


            // Getting the contractor's information
            System.out.println("Enter the contractor's FIRST NAME: ");
            String contFirstName = input.next();
            System.out.println("Enter the contractor's SURNAME: ");
            String contSurName = input.next();
            String contName = contFirstName + " " + contSurName;

            System.out.println("Enter the contractor's phone number: ");
            String contTelephone = input.next();

            System.out.println("Enter the contractor's email: ");
            String contEmail = input.next();

            System.out.println("Enter the contractor's address: ");
            String contAddress = input.next();
            contAddress += input.nextLine();


            // Getting the customer's information
            System.out.println("Enter the customer's FIRST NAME: ");
            String firstName = input.next();
            firstName += input.nextLine();
            System.out.println("Enter the customer's SURNAME: ");
            String surName = input.next();
            String customerName = firstName + " " + surName;

            System.out.println("Enter the customer's phone number: ");
            String customerTel = input.next();
            customerTel += input.nextLine();

            System.out.println("Enter the customer's email: ");
            String customerEmail = input.next();

            System.out.println("Enter the customer's address: ");
            String customerAddress = input.next();
            customerAddress += input.nextLine();


            // Getting the details of the actual project
            System.out.println("Enter the building type: ");
            String projBuilding = input.next();
            projBuilding += input.nextLine();

        /*
        The user has an option to select whether this project has a name.
        If the project does not have a name, a placeholder name will be given.
         */
            int option = 0;
            String projName = "";
            while (option != 1 || option != 2) {
                System.out.println("Please select either 1 or 2.");
                System.out.println(
                        "Does this project have a name?\n" +
                                "(1)YES     (2)NO");
                option = input.nextInt();

                // we have a project name
                if (option == 1) {
                    System.out.println("Enter the project name: ");
                    projName = input.next();
                    projName += input.nextLine();
                    break;
                }

                // no project name
                else if (option ==2) {
                    projName += projBuilding + " " + surName;
                    System.out.println("The project has been given a placeholder name.");
                    System.out.println("");
                    break;
                }
            }

            // getting the project number
            results = statement.executeQuery("SELECT MAX(number) from Projects");
            while (results.next()) {

                highestNum = results.getInt("max(number)");
            }
            // Finding the largest number and adding 1 to get the new ID for the next entry
            projNumber += highestNum;


            System.out.println("Enter the project address: ");
            String projAddress = input.next();
            projAddress += input.nextLine();

            System.out.println("Enter the project ERF number: ");
            String projErf = input.next();

            System.out.println("Enter the project fee: ");
            double projFee = input.nextDouble();

            System.out.println("Enter the amount paid so far: ");
            double paid = input.nextDouble();

            // Getting the deadline date
            System.out.println("Enter the project deadline (YYYY-MM-DD): ");
            String date1 = input.next();
            date1 += input.nextLine();

            // adding architect details into the architect table in our database

            statement.executeUpdate("INSERT INTO architect VALUES( '" + archName + "', " + archTelephone + ", '"
                    + archEmail + "', '" + archAddress + "', " + projNumber + ")");

            // adding contractor details into the contractor table in our database

            statement.executeUpdate("INSERT INTO contractor VALUES( '" + contName + "', " + contTelephone + ", '"
                    + contEmail + "', '" + contAddress + "', " + projNumber + ")");

            // adding customer details into the customer table in our database

            statement.executeUpdate("INSERT INTO customer VALUES( '" + customerName + "', " + customerTel + ", '"
                    + customerEmail + "', '" + customerAddress + "', " + projNumber + ")");


            // storing the entire query as a string
            String query = "'" + projName + "', " + projNumber + ", '"
                    + projBuilding + "', '" + projAddress + "', '" + projErf + "', "
                    + projFee + ", " + paid + ", '" + date1 + "', '" + archName + "', '" + customerName + "', '"
                    + contName + "')";

            // adding string query to sql statement & adding into projects table
            statement.executeUpdate("INSERT INTO Projects VALUES(" + query);

            // adding project into incomplete projects table
            statement.executeUpdate("INSERT INTO Incomplete VALUES(" + query);



            System.out.println("The project has been successfully added");
            System.out.println("");

            // close up our connections
            results.close();
            statement.close();
            connection.close();


        } catch (SQLException e) {
            System.out.println("Data not found");
        }

    }
    // This method will be called when a user wants to edit a project
    public static void editProj()  {

        try {

            Scanner input = new Scanner(System.in);

            // This will be used to format all figure amounts ( 10,000 as instead of 10000)
            NumberFormat myFormat = NumberFormat.getInstance();
            myFormat.setGroupingUsed(true);

            // for formatting my date
            DateFormat format2 = new SimpleDateFormat("MMMMM dd yyyy");
            String dateString = "";

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PoisePMS?useSSL=false",
                    "bootCamp",
                    "hypDevz!!");

            Statement statement = connection.createStatement();
            ResultSet results;

            // displaying all the projects to the user after the edit a project option has been selected
            results = statement.executeQuery("SELECT * FROM projects");

            while (results.next()) {
                System.out.println("Project Number - " + results.getInt("number") + "\n" +
                        "Project Name - " + results.getString("name") + "\n" +
                        "Building Type - " + results.getString("building") + "\n" +
                        "Building Address - " + results.getString("address") + "\n" +
                        "ERF Number - " + results.getString("erf") + "\n" +
                        "Project Fee - $" + myFormat.format(results.getInt("fee")) + "\n" +
                        "Amount Paid - $" + myFormat.format(results.getInt("paid")) + "\n" +
                        "Project Deadline - " + format2.format(results.getDate("deadline")) + "\n" +
                        "Customer Name - " + results.getString("customer") + "\n" +
                        "Architect Name - " + results.getString("architect") + "\n" +
                        "Contractor Name - " + results.getString("contractor") + "\n");
            }

            System.out.println("Enter the project number for the project you would like to edit: ");
            int projNum = input.nextInt();

            // using the project number to select the project which the user wants to edit
            results= statement.executeQuery("SELECT * FROM projects WHERE number = " + projNum);

            // displaying the project
            System.out.println("Selected Project:");
            while (results.next()) {
                System.out.println("Project Number - " + results.getInt("number") + "\n" +
                        "Project Name - " + results.getString("name") + "\n" +
                        "Building Type - " + results.getString("building") + "\n" +
                        "Building Address - " + results.getString("address") + "\n" +
                        "ERF Number - " + results.getString("erf") + "\n" +
                        "Project Fee - $" + myFormat.format(results.getInt("fee")) + "\n" +
                        "Amount Paid - $" + myFormat.format(results.getInt("paid")) + "\n" +
                        "Project Deadline - " + format2.format(results.getDate("deadline")) + "\n" +
                        "Customer Name - " + results.getString("customer") + "\n" +
                        "Architect Name - " + results.getString("architect") + "\n" +
                        "Contractor Name - " + results.getString("contractor") + "\n");
            }

            // the user will enter a number which corresponds with an option to edit a part of the project
            System.out.println("Select the aspect of the project you would like to edit: " + "\n" +
                    "(1)Project Name    (2)Building Type    (3)Address    (4)Project Fee    (5)Amount Paid" + "\n" +
                    "(6)Deadline    (7)Customer Details    (8)Architect Details    (9)Contractor Details");

            int option = input.nextInt();

            // editing project name
            if (option == 1) {
                System.out.println("Enter the new project name: ");
                String newProjName = input.next();
                newProjName += input.nextLine();

                statement.executeUpdate("UPDATE projects set name = '" + newProjName + "' where number = " + projNum);
                statement.executeUpdate("UPDATE incomplete set name = '" + newProjName + "' where number = " + projNum);
                System.out.println("Project number " + projNum + "'s name has been successfully updated to " + newProjName);
            }

            // editing building type
            else if (option == 2) {
                System.out.println("Enter the new building type: ");
                String newBuildingType = input.next();
                newBuildingType += input.nextLine();

                statement.executeUpdate("UPDATE projects set building = '" + newBuildingType + "' where number = " + projNum);
                statement.executeUpdate("UPDATE incomplete set building = '" + newBuildingType + "' where number = " + projNum);
                System.out.println("Project number " + projNum + "'s building type has been successfully updated to " + newBuildingType);
            }

            // editing address
            else if (option == 3) {
                System.out.println("Enter the new address: ");
                String newAddress = input.next();
                newAddress += input.nextLine();

                statement.executeUpdate("UPDATE projects set address = '" + newAddress + "' where number = " + projNum);
                statement.executeUpdate("UPDATE incomplete set address = '" + newAddress + "' where number = " + projNum);
                System.out.println("Project number " + projNum + "'s address has been successfully updated to " + newAddress);
            }

            // editing project fee
            else if (option == 4) {
                System.out.println("Enter the new project fee: ");
                int newProjFee = input.nextInt();

                statement.executeUpdate("UPDATE projects set fee = " + newProjFee + " where number = " + projNum);
                statement.executeUpdate("UPDATE incomplete set fee = " + newProjFee + " where number = " + projNum);
                System.out.println("Project number " + projNum + "'s fee has been successfully updated to $" + myFormat.format(newProjFee));
            }

            // editing paid amount
            else if (option == 5) {
                System.out.println("Enter how much has been paid: ");
                int newPaid = input.nextInt();

                statement.executeUpdate("UPDATE projects set paid = " + newPaid + " where number = " + projNum);
                statement.executeUpdate("UPDATE incomplete set paid = " + newPaid + " where number = " + projNum);
                System.out.println("Project number " + projNum + "'s fee has been successfully updated to $" + myFormat.format(newPaid));
            }

            // editing deadline
            else if (option == 6) {
                System.out.println("Enter the new deadline (YYYY-MM-DD): ");
                String date1 = input.next();
                date1 += input.nextLine();

                statement.executeUpdate("UPDATE projects set deadline = '" + date1 + "' where number = " + projNum);
                statement.executeUpdate("UPDATE incomplete set name = '" + date1 + "' where number = " + projNum);
                System.out.println("Project number " + projNum + "'s fee has been successfully updated to $" + format2.format(date1));
            }

            // editing customer info
            else if (option == 7) {
                editCustomer(projNum);
            }

            // editing architect details
            else if (option == 8) {
                editArchitect(projNum);
            }

            // editing contractor details
            else if (option == 9) {
                editContractor(projNum);
            }

            // close up our connections
            results.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Data not found");
        }

    }

    // This method will be called when a user wants to edit details of a customer
    // The method takes in the project number as a parameter
    public static void editCustomer(int projNum) {

        try {

            Scanner input = new Scanner(System.in);

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PoisePMS?useSSL=false",
                    "bootCamp",
                    "hypDevz!!");

            Statement statement = connection.createStatement();
            ResultSet results;

            results = statement.executeQuery("SELECT * FROM customer WHERE proj_num =  " + projNum);

            System.out.println("Customer Details for project number " + projNum + " -----> ");
            while (results.next()) {
                System.out.println("Customer Name - " + results.getString("name") + "\n" +
                        "Customer Telephone - " + results.getString("telephone") + "\n" +
                        "Customer Email - " + results.getString("email") + "\n" +
                        "Customer Address - " + results.getString("address") + "\n");
            }

            // the user will select a number which corresponds with an option to edit
            System.out.println("Select which aspect of the customer you would like to edit: \n" +
                    "(1)Name    (2)Telephone    (3)Email    (4)Address.  ");
            int option = input.nextInt();

            // editing name
            if (option == 1) {

                System.out.println("Enter the new customer's FIRST NAME: ");
                String firstName = input.next();
                firstName += input.nextLine();
                System.out.println("Enter the new customer's SURNAME: ");
                String surName = input.next();
                String customerName = firstName + " " + surName;

                statement.executeUpdate("update customer set name = '" + customerName + "' where proj_num = " + projNum);
                statement.executeUpdate("update projects set customer = '" + customerName + "' where number = " + projNum);
                statement.executeUpdate("update incomplete set customer = '" + customerName + "' where number = " + projNum);

                System.out.println("Project number " + projNum + "'s customer has been successfully updated to " + customerName);
            }

            // editing telephone
            else if (option == 2) {

                System.out.println("Enter the customer's new phone number: ");
                String customerTel = input.next();
                customerTel += input.nextLine();

                statement.executeUpdate("update customer set telephone = '" + customerTel + "' where proj_num = " + projNum);
                System.out.println("The customer's telephone has been updated to " + customerTel);
            }

            // editing email
            else if (option == 3) {
                System.out.println("Enter the new customer's email: ");
                String email = input.next();
                email += input.nextLine();

                statement.executeUpdate("update customer set email = '" + email + "' where proj_num = " + projNum);
                System.out.println("The customer's email has been updated to " + email);
            }

            // editing address
            else if (option == 4) {
                System.out.println("Enter the new customer address: ");
                String address = input.next();
                address += input.nextLine();

                statement.executeUpdate("update customer set address = '" + address + "' where proj_num = " + projNum);
                System.out.println("The customer's address has been updated to " + address);
            }

            // close up our connections
            results.close();
            statement.close();
            connection.close();


        } catch (SQLException e) {
            System.out.println("Data not found");
        }
    }

    // This method will be called when a user wants to edit details of an architect
    // The method takes in the project number as a parameter
    public static void editArchitect(int projNum) {

        try {

            Scanner input = new Scanner(System.in);

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PoisePMS?useSSL=false",
                    "bootCamp",
                    "hypDevz!!");

            Statement statement = connection.createStatement();
            ResultSet results;

            results = statement.executeQuery("SELECT * FROM architect WHERE proj_num =  " + projNum);

            System.out.println("Architect Details for project number " + projNum + " -----> ");
            while (results.next()) {
                System.out.println("Architect Name - " + results.getString("name") + "\n" +
                        "Architect Telephone - " + results.getString("telephone") + "\n" +
                        "Architect Email - " + results.getString("email") + "\n" +
                        "Architect Address - " + results.getString("address") + "\n");
            }
            System.out.println("Select which aspect of the architect you would like to edit: \n" +
                    "(1)Name    (2)Telephone    (3)Email    (4)Address.  ");
            int option = input.nextInt();

            // editing name
            if (option == 1) {

                System.out.println("Enter the new architect's FIRST NAME: ");
                String firstName = input.next();
                firstName += input.nextLine();
                System.out.println("Enter the new architect's SURNAME: ");
                String surName = input.next();
                String archName = firstName + " " + surName;

                statement.executeUpdate("update architect set name = '" + archName + "' where proj_num = " + projNum);
                statement.executeUpdate("update projects set architect = '" + archName + "' where number = " + projNum);
                statement.executeUpdate("update incomplete set architect = '" + archName + "' where number = " + projNum);

                System.out.println("Project number " + projNum + "'s architect has been successfully updated to " + archName);
            }

            // editing telephone
            else if (option == 2) {

                System.out.println("Enter the architect's new phone number: ");
                String archTel = input.next();
                archTel += input.nextLine();

                statement.executeUpdate("update architect set telephone = '" + archTel + "' where proj_num = " + projNum);
                System.out.println("The architect's telephone has been updated to " + archTel);
            }

            // editing email
            else if (option == 3) {
                System.out.println("Enter the new architect's email: ");
                String email = input.next();
                email += input.nextLine();

                statement.executeUpdate("update architect set email = '" + email + "' where proj_num = " + projNum);
                System.out.println("The architect's email has been updated to " + email);
            }

            // editing address
            else if (option == 4) {
                System.out.println("Enter the new architect address: ");
                String address = input.next();
                address += input.nextLine();

                statement.executeUpdate("update architect set address = '" + address + "' where proj_num = " + projNum);
                System.out.println("The architect's address has been updated to " + address);
            }

            // close up our connections
            results.close();
            statement.close();
            connection.close();

        } catch (SQLException e) {
            System.out.println("Data not found");
        }
    }

    // This method will be called when a user wants to edit details of a contractor
    // The method takes in the project number as a parameter
    public static void editContractor(int projNum) {

        try {

            Scanner input = new Scanner(System.in);

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PoisePMS?useSSL=false",
                    "bootCamp",
                    "hypDevz!!");

            Statement statement = connection.createStatement();
            ResultSet results;

            results = statement.executeQuery("SELECT * FROM contractor WHERE proj_num =  " + projNum);

            System.out.println("Contractor Details for project number " + projNum + " -----> ");
            while (results.next()) {
                System.out.println("Contractor Name - " + results.getString("name") + "\n" +
                        "Contractor Telephone - " + results.getString("telephone") + "\n" +
                        "Contractor Email - " + results.getString("email") + "\n" +
                        "Contractor Address - " + results.getString("address") + "\n");
            }
            System.out.println("Select which aspect of the contractor you would like to edit: \n" +
                    "(1)Name    (2)Telephone    (3)Email    (4)Address.  ");
            int option = input.nextInt();

            // editing name
            if (option == 1) {

                System.out.println("Enter the new contractor's FIRST NAME: ");
                String firstName = input.next();
                firstName += input.nextLine();
                System.out.println("Enter the new contractor's SURNAME: ");
                String surName = input.next();
                String contrName = firstName + " " + surName;

                statement.executeUpdate("update contractor set name = '" + contrName + "' where proj_num = " + projNum);
                statement.executeUpdate("update projects set contractor = '" + contrName + "' where number = " + projNum);
                statement.executeUpdate("update incomplete set contractor = '" + contrName + "' where number = " + projNum);

                System.out.println("Project number " + projNum + "'s contractor has been successfully updated to " + contrName);
            }

            // editing telephone
            else if (option == 2) {

                System.out.println("Enter the contractor's new phone number: ");
                String contrTel = input.next();
                contrTel += input.nextLine();

                statement.executeUpdate("update contractor set telephone = '" + contrTel + "' where proj_num = " + projNum);
                System.out.println("The contractor's telephone has been updated to " + contrTel);
            }

            // editing email
            else if (option == 3) {
                System.out.println("Enter the new contractor's email: ");
                String email = input.next();
                email += input.nextLine();

                statement.executeUpdate("update contractor set email = '" + email + "' where proj_num = " + projNum);
                System.out.println("The contractor's email has been updated to " + email);
            }

            // editing address
            else if (option == 4) {
                System.out.println("Enter the new architect address: ");
                String address = input.next();
                address += input.nextLine();

                statement.executeUpdate("update contractor set address = '" + address + "' where proj_num = " + projNum);
                System.out.println("The contractor's address has been updated to " + address);
            }

            // close up our connections
            results.close();
            statement.close();
            connection.close();


        } catch (SQLException e) {
            System.out.println("Data not found");
        }
    }

    // This method will be called when a user wants to view all the projects which are late
    public static void lateProjects() {

        try {

            Scanner input = new Scanner(System.in);

            // for formatting money figures
            NumberFormat myFormat = NumberFormat.getInstance();
            myFormat.setGroupingUsed(true);

            // for formatting my date
            DateFormat format2 = new SimpleDateFormat("MMMMM dd yyyy");
            String dateString = "";

            LocalDate d1 = LocalDate.now(); // today's date
            String today2 = "";  // empty string I will store the date in
            today2 += d1;
            Date today = new  SimpleDateFormat("yyyy-MM-dd").parse(today2);  // changing the date to match my Date format

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PoisePMS?useSSL=false",
                    "bootCamp",
                    "hypDevz!!");

            Statement statement = connection.createStatement();
            ResultSet results;

            // to show all the projects which are late
            results = statement.executeQuery("SELECT * FROM projects WHERE deadline < curdate()");

            System.out.println("-----PROJECTS PAST THEIR DUE DATE-----\n");
            while (results.next()) {
                System.out.println("Project Number - " + results.getInt("number") + "\n" +
                        "Project Name - " + results.getString("name") + "\n" +
                        "Building Type - " + results.getString("building") + "\n" +
                        "Building Address - " + results.getString("address") + "\n" +
                        "ERF Number - " + results.getString("erf") + "\n" +
                        "Project Fee - $" + myFormat.format(results.getInt("fee")) + "\n" +
                        "Amount Paid - $" + myFormat.format(results.getInt("paid")) + "\n" +
                        "Project Deadline - " + format2.format(results.getDate("deadline")) + "\n" +
                        "Customer Name - " + results.getString("customer") + "\n" +
                        "Architect Name - " + results.getString("architect") + "\n" +
                        "Contractor Name - " + results.getString("contractor") + "\n");
            }

            // close up our connections
            results.close();
            statement.close();
            connection.close();


        } catch (SQLException | ParseException e) {
            System.out.println("Data not found");
        }
    }

    // This method will be called when a user wants to view all the projects which have been registered
    public static void viewAll() {

        try {

            Scanner input = new Scanner(System.in);

            // for formatting my money figures
            NumberFormat myFormat = NumberFormat.getInstance();
            myFormat.setGroupingUsed(true);

            // for formatting my date
            DateFormat format2 = new SimpleDateFormat("MMMMM dd yyyy");
            String dateString = "";

            LocalDate d1 = LocalDate.now(); // today's date
            String today2 = "";  // empty string I will store the date in
            today2 += d1;
            Date today = new  SimpleDateFormat("yyyy-MM-dd").parse(today2);  // changing the date to match my Date format

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PoisePMS?useSSL=false",
                    "bootCamp",
                    "hypDevz!!");

            Statement statement = connection.createStatement();
            ResultSet results;

            results = statement.executeQuery("select * from projects");

            while (results.next()) {
                System.out.println("Project Number - " + results.getInt("number") + "\n" +
                        "Project Name - " + results.getString("name") + "\n" +
                        "Building Type - " + results.getString("building") + "\n" +
                        "Building Address - " + results.getString("address") + "\n" +
                        "ERF Number - " + results.getString("erf") + "\n" +
                        "Project Fee - $" + myFormat.format(results.getInt("fee")) + "\n" +
                        "Amount Paid - $" + myFormat.format(results.getInt("paid")) + "\n" +
                        "Project Deadline - " + format2.format(results.getDate("deadline")) + "\n" +
                        "Customer Name - " + results.getString("customer") + "\n" +
                        "Architect Name - " + results.getString("architect") + "\n" +
                        "Contractor Name - " + results.getString("contractor") + "\n");
            }

            // close up our connections
            results.close();
            statement.close();
            connection.close();

        } catch (SQLException | ParseException e) {
            System.out.println("Data not found");
        }
    }

    // This method will be called when a user wants to view all the incomplete projects
    public static void viewIncomplete() {

        try {

            Scanner input = new Scanner(System.in);

            // for formatting my money figures
            NumberFormat myFormat = NumberFormat.getInstance();
            myFormat.setGroupingUsed(true);

            // for formatting my date
            DateFormat format2 = new SimpleDateFormat("MMMMM dd yyyy");
            String dateString = "";

            LocalDate d1 = LocalDate.now(); // today's date
            String today2 = "";  // empty string I will store the date in
            today2 += d1;
            Date today = new  SimpleDateFormat("yyyy-MM-dd").parse(today2);  // changing the date to match my Date format

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PoisePMS?useSSL=false",
                    "bootCamp",
                    "hypDevz!!");

            Statement statement = connection.createStatement();
            ResultSet results;

            results = statement.executeQuery("select * from incomplete");

            while (results.next()) {
                System.out.println("Project Number - " + results.getInt("number") + "\n" +
                        "Project Name - " + results.getString("name") + "\n" +
                        "Building Type - " + results.getString("building") + "\n" +
                        "Building Address - " + results.getString("address") + "\n" +
                        "ERF Number - " + results.getString("erf") + "\n" +
                        "Project Fee - $" + myFormat.format(results.getInt("fee")) + "\n" +
                        "Amount Paid - $" + myFormat.format(results.getInt("paid")) + "\n" +
                        "Project Deadline - " + format2.format(results.getDate("deadline")) + "\n" +
                        "Customer Name - " + results.getString("customer") + "\n" +
                        "Architect Name - " + results.getString("architect") + "\n" +
                        "Contractor Name - " + results.getString("contractor") + "\n");
            }

            // close up our connections
            results.close();
            statement.close();
            connection.close();

        } catch (SQLException | ParseException e) {
            System.out.println("Data not found");
        }
    }
    // This method will be called when finalising a project. When a project is finalised, it will either be marked as complete
    // or an invoice will be generated
    public static void finaliseProject() {

        try {

            Scanner input = new Scanner(System.in);

            double paid = 0;
            double fee = 0;
            double owed = 0;

            // for formatting my paid & amount figures
            NumberFormat myFormat = NumberFormat.getInstance();
            myFormat.setGroupingUsed(true);

            // for formatting my date
            DateFormat format2 = new SimpleDateFormat("MMMMM dd yyyy");
            String dateString = "";

            LocalDate d1 = LocalDate.now(); // today's date
            String today2 = "";  // empty string I will store the date in
            today2 += d1;
            Date today = new  SimpleDateFormat("yyyy-MM-dd").parse(today2);  // changing the date to match my Date format

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PoisePMS?useSSL=false",
                    "bootCamp",
                    "hypDevz!!");

            Statement statement = connection.createStatement();
            ResultSet results;

            results = statement.executeQuery("select * from projects");

            while (results.next()) {
                System.out.println("Project Number - " + results.getInt("number") + "\n" +
                        "Project Name - " + results.getString("name") + "\n" +
                        "Building Type - " + results.getString("building") + "\n" +
                        "Building Address - " + results.getString("address") + "\n" +
                        "ERF Number - " + results.getString("erf") + "\n" +
                        "Project Fee - $" + myFormat.format(results.getInt("fee")) + "\n" +
                        "Amount Paid - $" + myFormat.format(results.getInt("paid")) + "\n" +
                        "Project Deadline - " + format2.format(results.getDate("deadline")) + "\n" +
                        "Customer Name - " + results.getString("customer") + "\n" +
                        "Architect Name - " + results.getString("architect") + "\n" +
                        "Contractor Name - " + results.getString("contractor") + "\n");
            }

            System.out.println("Enter the project number for the project you would like to finalise: ");
            int projNum = input.nextInt();

            // getting the project fee to see how much is owed in total
            results = statement.executeQuery("SELECT fee FROM projects WHERE number = " + projNum);

            while (results.next()) {
                fee += results.getDouble("fee");
            }


            // getting amount paid
            results = statement.executeQuery("SELECT paid FROM projects WHERE number = " + projNum);
            while (results.next()) {

                paid += results.getDouble("paid");
            }


            // calculating how much is still owed
            owed = fee - paid;

            // generate invoice if the customer still has not paid the project in full
            if (owed > 0 ) {
                results = statement.executeQuery("SELECT * FROM customer WHERE proj_num = " + projNum);

                while (results.next()) {

                    System.out.println("        -----Customer Invoice-----\n" +
                            "NAME - " + results.getString("name") + "|| TEL - " + results.getString("telephone") + "\n" +
                            "EMAIL - " + results.getString("email") + "|| ADDRESS - " + results.getString("address") + "\n" +
                            "-------------------------------------------------------------------------------\n" +
                            "TOTAL COST - $" + myFormat.format(fee) + "\n" +
                            "AMOUNT PAID - $" + myFormat.format(paid) + "\n" +
                            "AMOUNT STILL OWED -----> $" + myFormat.format(owed));
                }
                System.out.println("");
                System.out.println("An invoice has been generated");
            }

            // amount paid in full, add project to the completed projects table
            else if (owed <= 0) {
                results = statement.executeQuery("SELECT * FROM projects WHERE number = " + projNum);
                // initialising empty strings to store project information
                String name = "";
                String building = "";
                String address = "";
                String erf = "";
                fee += 0;
                String customer = "";
                String architect = "";
                String contractor = "";

                // storing the project info into the appropriate variables
                while (results.next()) {
                    name += results.getString("name");
                    building +=  results.getString("building");
                    address += results.getString("address");
                    erf += results.getString("erf");
                    fee += results.getDouble("fee");
                    today = results.getDate("deadline");
                    customer += results.getString("customer");
                    architect += results.getString("architect");
                    contractor += results.getString("contractor");

                }
                // putting project into the completed projects table, along with the date it was finalised and completed
                statement.executeUpdate("insert into completed values(curdate(), '" + name + "', '" +
                        building + "', '" + address + "', '" + erf + "', " + fee + ", '" + today + "', '" + customer + "', '" +
                        architect + "', '" + contractor + "' )");

                // removing finalised project/customer/architect/contractor info from all other tables
                statement.executeUpdate("DELETE FROM projects WHERE number = " + projNum);
                statement.executeUpdate("DELETE FROM incomplete WHERE number = " + projNum);
                statement.executeUpdate("DELETE FROM customer WHERE proj_num = " + projNum);
                statement.executeUpdate("DELETE FROM architect WHERE proj_num = " + projNum);
                statement.executeUpdate("DELETE FROM contractor WHERE proj_num = " + projNum);

                System.out.println("The project has been finalised and completed :) ");
            }

            // close up our connections
            results.close();
            statement.close();
            connection.close();


        } catch (SQLException | ParseException e) {
            System.out.println("Data not found");
        }
    }

    // This method will be called to view all the completed projects
    public static void viewCompleted() {

        try {

            Scanner input = new Scanner(System.in);

            // for formatting my paid & amount figures
            NumberFormat myFormat = NumberFormat.getInstance();
            myFormat.setGroupingUsed(true);

            // for formatting my date
            DateFormat format2 = new SimpleDateFormat("MMMMM dd yyyy");
            String dateString = "";

            LocalDate d1 = LocalDate.now(); // today's date
            String today2 = "";  // empty string I will store the date in
            today2 += d1;
            Date today = new  SimpleDateFormat("yyyy-MM-dd").parse(today2);  // changing the date to match my Date format

            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PoisePMS?useSSL=false",
                    "bootCamp",
                    "hypDevz!!");

            Statement statement = connection.createStatement();
            ResultSet results;

            results = statement.executeQuery("select * from completed");

            System.out.println("FINALISED PROJECTS -----> \n");
            System.out.println("");

            while (results.next()) {
                System.out.println("Date Finalised - " + format2.format(results.getDate("finalDate")) + "\n" +
                        "Project Name - " + results.getString("name") + "\n" +
                        "Building Type - " + results.getString("building") + "\n" +
                        "Project Address - " + results.getString("address") + "\n" +
                        "ERF Number - " + results.getString("erf") + "\n" +
                        "Project Fee - " + myFormat.format(results.getDouble("fee")) + "\n" +
                        "Project Deadline - " + format2.format(results.getDate("deadline")) + "\n" +
                        "Customer Name - " + results.getString("customer") + "\n" +
                        "Architect Name - " + results.getString("architect") + "\n" +
                        "Contractor Name - " + results.getString("contractor") + "\n");
            }

            // close up our connections
            results.close();
            statement.close();
            connection.close();

        } catch (SQLException | ParseException e) {
            System.out.println("Data not found");
        }

    }

    public static void main(String args[]) {

        Scanner input = new Scanner(System.in);
        boolean logged = true;

        while (logged) {
            System.out.println("");
            System.out.println("----Welcome, please select a menu item----\n" +
                    "a -     Add a new project\n" +
                    "ed -     Edit a project\n" +
                    "la -    View all late projects\n" +
                    "f -     Finalize projects\n" +
                    "va -    View all projects\n" +
                    "vi -    View all incomplete projects\n" +
                    "vc -    View completed projects\n" +
                    "e -     Exit program");

            String option = input.next().toLowerCase();

            // adding a new project
            if (Objects.equals(option, "a")) {
                newProject();
            }

            // editing a project
            else if (Objects.equals(option, "ed")) {
                editProj();
            }

            // viewing incomplete projects
            else if (Objects.equals(option, "vi")) {
                viewIncomplete();
            }

            // viewing overdue projects
            else if (Objects.equals(option, "la")) {
                lateProjects();
            }

            // finalizing a project
            else if (Objects.equals(option, "f")) {
                finaliseProject();
            }

            // displaying all projects
            else if (Objects.equals(option, "va")) {
                viewAll();
            }

            // displaying completed projects
            else if (Objects.equals(option, "vc")) {
                viewCompleted();
            }

            //exiting program
            else if (Objects.equals(option, "e")) {
                System.out.println("Goodbye :)");
                int e = 0;
                System.exit(e);
            }

        }

    }
}
