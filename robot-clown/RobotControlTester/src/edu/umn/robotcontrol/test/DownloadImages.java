package edu.umn.robotcontrol.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadImages {

  /**
   * @param args
   * @throws IOException 
   * @throws MalformedURLException 
   */
  public static void main(String[] args)  {
    int lastChecksum = -1;
    while(true){
      int checksum = 0;
      try{
        URLConnection con = new URL(
            "http://robot-control.elasticbeanstalk.com/rest/control/photo")
        .openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(
            con.getInputStream()));
        File f= new File("/Users/tyler/Desktop/photos/" + System.currentTimeMillis() + ".jpg");
        FileWriter writer = new FileWriter(f);
        int val;
        while((val = in.read()) != -1){
          writer.write(val);
          checksum += val;
        }
        writer.close();
        in.close();
        System.out.println("Took Photo " + f);
        if(lastChecksum == checksum){
//          System.out.println("Deleting redundant photo " + f);
          f.delete();
        } else {
          lastChecksum = checksum;
        }
      } catch (Exception e){
        System.out.println(e.getMessage());
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

}
