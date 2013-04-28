package edu.umn.robotcontrol.servlet;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import edu.umn.robotcontrol.domain.RobotCommand;

/**
 * @author Tyler
 * 
 */
@Path("/control")
public class RobotControlResource {
	

  @GET
  @Path("/hello")
  @Produces(MediaType.TEXT_HTML)
  public String sayHello() {
    return "Hello";
  }
  
  private double determineImageScale(int sourceWidth, int sourceHeight, int targetWidth, int targetHeight) {
	  double scalex = (double) targetWidth / sourceWidth;
	  double scaley = (double) targetHeight / sourceHeight;
	  return Math.min(scalex, scaley);
  }
  
  private BufferedImage resizeImage(BufferedImage originalImage, int type, int width, int height){
	  if (width == 0 || height == 0 || originalImage.getWidth() == 0 || originalImage.getHeight() == 0) {
		  return originalImage;
	  }
	  double scale = determineImageScale(originalImage.getWidth(), originalImage.getHeight(), width, height);
	  
	  // Only downscale the image
	  if (scale >= 1.0) {
		  return originalImage;
	  }
	  width = (int)(originalImage.getWidth()*scale);
	  height = (int)(originalImage.getHeight()*scale);
	  BufferedImage resizedImage = new BufferedImage(width, height, type);
	  Graphics2D g = resizedImage.createGraphics();
	  g.drawImage(originalImage, 0, 0, width, height, null);
	  g.dispose();
	  return resizedImage;
  }
  
  @GET
  @Path("/photo")
  @Produces("image/jpeg")
  public Response getRecentImage(@QueryParam("width") int width, @QueryParam("height") int height){
	  File imageFile = ImageHolder.getInstance().getCurrentImageFile();
	  
	  if(imageFile != null && imageFile.exists()){
		  System.out.println("Returning image " + imageFile);
		  
		  try {
			  BufferedImage original = ImageIO.read(imageFile);
			  int type = original.getType() == 0 ? BufferedImage.TYPE_INT_RGB : original.getType();
			  return Response.ok(resizeImage(original, type, width, height), "image/jpeg").build();
		  } catch (IOException e) {
			  System.out.println("Image could not be found");
			  return Response.ok().build();
		  }	  
	  }
	  if(imageFile == null){
		  System.out.println("Image file was null");
	  } else {
		  System.out.println("Image file didn't exist");
	  }
	  return Response.ok().build();
  }

  @POST
  @Path("/photo")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_OCTET_STREAM)
  public String handlePhoto(byte[] data) throws IOException {
    System.out.println("photo was posted, " + data.length + " bytes");
    File tmp = File.createTempFile("image", ".jpeg");
    FileOutputStream fos = new FileOutputStream(tmp);
    fos.write(data);
    fos.close();
    System.out.println("Wrote bytes to " + tmp);
    ImageHolder.getInstance().setCurrentImageFile(tmp);
    return " {msg:'success'}";
  }

  @POST
  @Path("/command")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public String handleCommand(String command) {
    Gson gson = new Gson();
    RobotCommand rc = gson.fromJson(command, RobotCommand.class);
    CommandHolder.getInstance().pushCommand(rc);
    System.out.println(rc);
    return "{msg:'success'}";
  }
  
  @GET
  @Path("/command")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getRecentCommand(){
	  RobotCommand rc = CommandHolder.getInstance().popCommand();
	  if(rc != null){
		Gson gson = new Gson();
		String json = gson.toJson(rc);
		return Response.ok(json).build();
	  }
	  return Response.noContent().build();
  }

}
