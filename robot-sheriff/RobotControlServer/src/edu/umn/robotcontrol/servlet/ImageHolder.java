package edu.umn.robotcontrol.servlet;

import java.io.File;

public class ImageHolder {

	private static ImageHolder INSTANCE;
	
	public static ImageHolder getInstance(){
		if(INSTANCE == null){
			INSTANCE = new ImageHolder();
		}
		return INSTANCE;
	}
	
	private ImageHolder(){};
	
	private File currentImageFile;
	
	public File getCurrentImageFile(){
		return currentImageFile;
	}
	
	public void setCurrentImageFile(File img){
		currentImageFile = img;
	}
	
}
