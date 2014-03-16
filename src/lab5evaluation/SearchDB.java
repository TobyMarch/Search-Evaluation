/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

//new//

package lab5evaluation;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.lang.String;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 * @author TQ
 */
public class SearchDB {

    	private Connection connection;
	private Statement statement;


public SearchDB(String dbLocation) {
		loadDriver();

		init(dbLocation);

		loadDB();

	}
public SearchDB() {
		this("default.db");
	}


private void loadDriver() {
	    try {


                    Class.forName("org.sqlite.JDBC");


            } catch (ClassNotFoundException e1) {
			System.out.println("Could not load SQLite JDBC Driver");
			System.exit(0);
		}
	}



private void init(String dbLocation) {
    	try {

                connection = DriverManager.getConnection("jdbc:sqlite:" + dbLocation);
        	statement = connection.createStatement();
		statement.setQueryTimeout(30);



            } catch (SQLException e) {
			System.out.println("Could not initialize the connection and/or statement.");
			e.printStackTrace();
		}
	}

private void loadDB() {
    	try {

            /*statement.executeUpdate("CREATE TABLE IF NOT EXISTS item");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS url");
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS urltoitem");*/
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS item (id INTEGER PRIMARY KEY, name VARCHAR, type VARCHAR)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS url (id INTEGER PRIMARY KEY,address VARCHAR, docType VARCHAR, title VARCHAR)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS urltoitem (id INTEGER PRIMARY KEY, urlid INTEGER, itemid INTEGER, FOREIGN KEY (urlid) REFERENCES url(id), FOREIGN KEY (itemid) REFERENCES item(id)) " );


                    //FOREIGN KEY(urlID) REFERENCES url(id), FOREIGN KEY(itemID) REFERENCES item(id)"
                    //+ "AS(SELECT address, docType, title FROM url"
                     //+ "SELECT item.name, item.type FROM item"
                     //+ "INNER JOIN urltoitem on item.id = urltoitem.itemid )");

                } catch (SQLException e) {
			e.printStackTrace();
		}
	}

public void insertURL(String address, String docType, String title) {

            ResultSet r;

        try {

            PreparedStatement prep = connection.prepareStatement("INSERT INTO url (address, docType, title) VALUES(?,?,?);");
            prep.setString(1, address);
            prep.setString(2,docType);
            prep.setString(3, title);
            prep.addBatch();
            prep.executeBatch();

            r = statement.executeQuery("SELECT id FROM url");

            //statement.executeUpdate("INSERT INTO url (address, docType, title) VALUES (" + address + "," + docType + "," + title + ")");
                  //  +"INSERT INTO url (docType) VALUES('docType')"

                    //+"INSERT INTO url (title) VALUES('title')"
                    //+"INSERT INTO url (id) VALUES(2)");




        } catch (SQLException e) {
			e.printStackTrace();
		}

            
}

/*public String geturlid() {

    ResultSet r;
    String urlID = "";

    try {

        r = statement.executeQuery("SELECT id FROM url");
        urlID = r.toString();
    }

    catch (SQLException e)  {
        e.printStackTrace();
    }
    return urlID;

}*/

public String getUrl(String urlin) {
        
    String urlidnum = "";


    try{
        String str = ("SELECT id FROM url WHERE address='" + urlin + "'");

        System.out.println(str);
        ResultSet rs = statement.executeQuery(str);
        //rs.next();
        while(rs.next()){
            System.out.println(rs.getString("id"));
            urlidnum = rs.getString("id");
        }
        


    }

    catch (SQLException e) {
        e.printStackTrace();
    }

    return urlidnum;



}


/*public ResultSet findURL_byURL(int idnum) {

        ResultSet r;


        try {


         r = statement.executeQuery("SELECT id FROM url"
            + "WHERE id=?', idnum");


        } catch (SQLException e) {
            e.printStackTrace();
        }1

        return r;
}*/

public void findURL_byID(int urlidnum) {

        ResultSet r;
        ResultSet r2;

        try {

            PreparedStatement prep = connection.prepareStatement("SELECT address, docType, title FROM url WHERE id=(?)");
            prep.setInt(1, urlidnum);
            //prep.addBatch();
            //prep.executeBatch();

            r = prep.executeQuery();

            System.out.println("TITLE: " + r.getString("title"));
            System.out.println("    ADDRESS: " + r.getString("address"));

            //PreparedStatement prep2 = connection.prepareStatement("SELECT name, type FROM item INNER JOIN urltoitem on item.id = urltoitem.itemid WHERE urltoitem.urlid = (?)");
            PreparedStatement prep2 = connection.prepareStatement("SELECT name, type FROM item AS I JOIN urltoitem AS U ON I.id = U.itemid WHERE U.urlid = (?)");
            prep2.setInt(1, urlidnum);
            //prep2.addBatch();
            //prep2.executeBatch();

            r2 = prep2.executeQuery();

            System.out.println("    TYPE: " + r2.getString("type"));
            System.out.println("    NAME: " + r2.getString("name"));
            //System.out.println(r2.getString("name"))

             //r = statement.executeQuery("SELECT * FROM urltoitem"

                    //+ "WHERE urlid=?, urlidnum");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        //return r;
}

/*public void findURL_byID (int urlidnum) {

        try {

                String str = ("SELECT address, docType, title FROM url WHERE id= '" + urlidnum + "'");
                ResultSet r = statement.executeQuery(str);

                System.out.println("TITLE: " + r.getString("title"));
                System.out.println("    ADDRESS: " + r.getString("address"));



                String str2 = ("SSLECT name, type, FROM item AS I JOIN urltoitem AS U ON I.id = U.itemid WHERE U. urlid = '" + urlidnum + "'");
                ResultSet r2 = statement.executeQuery(str2);

                System.out.println("    TYPE: " + r2.getString("type"));
                System.out.println("    NAME: " + r2.getString("name"));

            }

        catch(SQLException e) {
            e.printStackTrace();
            }
}
 * 
 */

public void insertItem(String itemname, String type) {

            try {

            PreparedStatement prep = connection.prepareStatement("INSERT INTO item (name, type) VALUES(?,?);");
            prep.setString(1, itemname);
            prep.setString(2, type);
            prep.addBatch();
            prep.executeBatch();
            //statement.executeUpdate("INSERT INTO item (name, type) VALUES('itemname', 'type')");
            //statement.params.itemname = itemname;
            //statement.params.type = type;
            //statement.executeAsync();


        } catch (SQLException e) {
			e.printStackTrace();
		}

        }

/*public ResultSet findItem_byName(String idname) {

    ResultSet r;

    try {
        r = statement.executeQuery("SELECT id FROM item"
                + "WHERE id.name=?', idname");
    }
    catch (SQLException e) {
        e.printStackTrace();
    }
    return r;
}*/

public List findItems_byType(String type) {

        List listout = new ArrayList();

        try {

            ResultSet r = statement.executeQuery( "SELECT id,id.name FROM item"
                    +"WHERE type=?', type");
                    while (r.next()){
                        listout.add(r);
                    }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return listout;
}

public void insertUrltoItem(int item) {
		try {

            PreparedStatement prep = connection.prepareStatement("INSERT INTO urltoitem (itemid, urlid) VALUES(?, (SELECT MAX(id) + 1 FROM urltoitem));");
            prep.setInt(1, item);
            //prep.setInt(2, (;
            prep.addBatch();
            prep.executeBatch();

                                //statement.executeQuery("SELECT address, docType, title FROM url");
                                //+ "SELECT name, type FROM item INNER JOIN urltoitem on Item.id = urltoitem.itemid");





                } catch (SQLException e) {
			e.printStackTrace();
		}
	}

public String numOfDocs(){
    String docNum = "";

    ResultSet r;
        try {
            r = statement.executeQuery("SELECT MAX(id) FROM URL");
            docNum = r.getString("MAX(id)");
        } catch (SQLException ex) {
            Logger.getLogger(SearchDB.class.getName()).log(Level.SEVERE, null, ex);
        }

    return docNum;
}
public String getSearchTerm_ByDocTitle (String title) {
        String urlidnum = "";
        String searchTerm = "";

        try {
            String str = ("SELECT id FROM url WHERE title='" + title + "'");
            //System.out.println(str);
            ResultSet rs = statement.executeQuery(str);
            while(rs.next()){
                urlidnum = rs.getString("id");
            }

            String str2 = ("SELECT name FROM item AS I JOIN urltoitem AS U ON I.id = U.itemid WHERE U.urlid ='" + urlidnum + "'");
            //System.out.println(str2);
            ResultSet rs2 = statement.executeQuery(str2);
            while(rs2.next()) {
                searchTerm = rs2.getString("name");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return searchTerm;

}

public String getSearchTerm_ByDocID (String urlidnum) {
        //String urlidnum = "";
        String searchTerm = "";

        try {

            String str2 = ("SELECT name FROM item AS I JOIN urltoitem AS U ON I.id = U.itemid WHERE U.urlid ='" + urlidnum + "'");
            //System.out.println(str2);
            ResultSet rs2 = statement.executeQuery(str2);
            while(rs2.next()) {
                searchTerm = rs2.getString("name");
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return searchTerm;

}


public List getSearchTerms () {

        List<String> listOut = new ArrayList<String>();

        try{

            String str = ("SELECT name from ITEM");

            ResultSet r = statement.executeQuery(str);

            while(r.next()){

                listOut.add(r.getString("name").replaceAll("'", "\\'"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(listOut);
        return listOut;
}



public List getSearchResults (String title) {

        List<String> resultList = new ArrayList<String>();

        String num = "";

        try{


            String str2 = ("SELECT id FROM item WHERE name =\"" + title + "\"");

            ResultSet r2 = statement.executeQuery(str2);

            while(r2.next()){
                num = r2.getString("id");
            }


            String str = ("SELECT id FROM urltoitem WHERE itemid =\"" + num + "\"");

            ResultSet r = statement.executeQuery(str);

            while(r.next()){
                resultList.add(r.getString("id"));
            }


        }
        catch (SQLException e){
            e.printStackTrace();
        }

        return resultList;

}

}
