/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author sreenath
 */
public class CricDetails {
    private boolean PROXY_SUPPORT;
    private Proxy proxy;
    private URLConnection connection;
    private URL urlCric;
    private Properties[] matchProperties=null;
    public static final String TITLE="title";
    public static final String LINK="link";
    public static final String DESCRIPTION="description";
    CricDetails() throws IOException
    {
        //for direct internet connection
        PROXY_SUPPORT=false;
        urlCric=new URL("http://static.espncricinfo.com/rss/livescores.xml");
        doProcessing();
    }
    CricDetails(String proxy_host,String port_string) throws NumberFormatException, IOException
    {
        //for internet connection through proxy
        PROXY_SUPPORT=true;
        urlCric=new URL("http://static.espncricinfo.com/rss/livescores.xml");
        int port;
        
         port = Integer.parseInt(port_string);
               
        proxy=new Proxy(Proxy.Type.HTTP,InetSocketAddress.createUnresolved(proxy_host, port));
        doProcessing();
    }
    private void doProcessing() throws IOException 
    {
            InputStream inputStream=null;
        try {
            
            //creating a connection object
            if(PROXY_SUPPORT==true)
            {   
            connection=urlCric.openConnection(proxy);
            }
            else
            {
            connection=urlCric.openConnection();   
            }
        
            //Now we open an input stream to the cricinfo live scorecard xml file.
            inputStream =  connection.getInputStream();
            //we now parse the xml file to get the details
            domParser(inputStream);

        } catch (Exception ex) {
            Logger.getLogger(CricDetails.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
        {
            inputStream.close();
           
        }
        

        
        
    }
   private void domParser(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException
    {
        //XML PARSING CODE
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    org.w3c.dom.Document doc =dBuilder.parse(inputStream);
    doc.getDocumentElement().normalize();
    NodeList nList = doc.getElementsByTagName("item");
   //we write all identifiedmatch details in to a array of property object so that we can store and access as by Key-value pairs
    matchProperties = new Properties[nList.getLength()];
    for (int temp = 0; temp < nList.getLength(); temp++) {
       Node nNode = nList.item(temp);	    
       if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 
          Element eElement = (Element) nNode;
 
          //System.out.println("Match : "  + getTagValue("description",eElement));
          matchProperties[temp]=new Properties();
          matchProperties[temp].setProperty(TITLE, getTagValue(TITLE,eElement));
          matchProperties[temp].setProperty(LINK, getTagValue(LINK,eElement));
          matchProperties[temp].setProperty(DESCRIPTION, getTagValue(DESCRIPTION,eElement));
 
        }
    }
    }

    private String getTagValue(String sTag, Element eElement) {
        
    NodeList nlList= eElement.getElementsByTagName(sTag).item(0).getChildNodes();
    Node nValue = (Node) nlList.item(0); 
 
    return nValue.getNodeValue();
    }
    public Properties[] getMatchProperties() throws IOException
    {
        //returns the match property
        doProcessing();
        return matchProperties.clone();
    }
    
}
