/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.*;
/**
 *
 * @author sreenath
 */
public class CricDisplay {
    public static void main(String args[])
    {
            Properties[] matchProp;
            try {
                CricDetails cricDetails=new CricDetails();//CricDetails(PROXY_HOST,PROXY_PORT); for proxy connection
                matchProp=cricDetails.getMatchProperties();
                if(args.length==0)
                {
                    System.out.println("TODAYS MATCHES");
                    System.out.println("===============\n");
                    for(int i=0;i<matchProp.length;i++)
                    {
                        System.out.println("--------");
                        System.out.println("MATCH "+(i+1));
                        System.out.println("--------");
                        System.out.println(matchProp[i].getProperty(CricDetails.DESCRIPTION));
                        
                    }
                    
                }
               else if(args.length==1)
                {
                    //we check for a scoreboard on  the matchdescription ie, <number>'/'<number> eg:224/8
                    Pattern p = Pattern.compile("[0-9]+/[0-9]+");
                    int count=0;
                    if(args[0].compareToIgnoreCase("STARTED")==0)
                    {
                     for(int i=0;i<matchProp.length;i++)
                    {
                       
                        Matcher matcher = p.matcher(matchProp[i].getProperty(CricDetails.DESCRIPTION));
                        if(matcher.find())
                        {
                          count++;
                          System.out.println("--------");
                          System.out.println("MATCH "+count);
                          System.out.println("--------");
                          System.out.println(matchProp[i].getProperty(CricDetails.DESCRIPTION));
                          System.out.println("\t"+matchProp[i].getProperty(CricDetails.LINK));
                        }

                    }
                    }
                    else
                    {
                     for(int i=0;i<matchProp.length;i++)
                     {
                         //display match of the given country by checking whether the country string present in any match description
                        if(matchProp[i].getProperty(CricDetails.DESCRIPTION).toLowerCase().contains(args[0].toLowerCase()))
                        {
                          count++;
                          System.out.println("-----------------");
                          System.out.println(matchProp[i].getProperty(CricDetails.DESCRIPTION));
                          System.out.println("\t"+matchProp[i].getProperty(CricDetails.LINK));
                          System.out.println("-----------------");
                        }

                     }
                     if(count==0)
                     {
                         System.out.println("Sorry...No match found..");
                     }
                    }
                }
                else {
                    System.out.println("You can run the program with atmost 1 commandline arguement, STARTED or <teamname>");
                }
                System.out.println("\nusage:CricDisplay [STARTED | <teamname>]");
            } catch (IOException ex) {
                Logger.getLogger(CricDisplay.class.getName()).log(Level.SEVERE, null, ex);
            }
        
    }
}
