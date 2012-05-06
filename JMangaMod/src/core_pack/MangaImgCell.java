package core_pack;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class MangaImgCell {
	
	float numZoomFactor = 1;
	int wImg = 0, hImg = 0, wxh = 0;
	BufferedImage imgMangaOrg = null; // Original image
	BufferedImage imgMangaMod = null; // Output canvas
	
	List<Point> lstDiagSrcPoint; // List of dialogue source (clicked) points
	byte[] aryDiagArea; // Marked dialogue area
	
	List<Point> lstBkgSrcPoint; // List of background source (clicked) points
	byte[] aryBkgArea; // Marked background area
	boolean bavBkg; // Background area is available? false if dialogues are marked again after marking background 
	
	int[] imgPixels; 
	
	public MangaImgCell() {
		lstDiagSrcPoint = new ArrayList<Point>();
		lstBkgSrcPoint = new ArrayList<Point>();
	}

	/**
	 * Set the image file handle and read it into buffer.
	 * @param img_file File object for read
	 * @return Boolean value indicates successfuly file operation
	 */
	public boolean setImgFile(File img_file)
	{

		try {
			imgMangaOrg = ImageIO.read(img_file);
			
			// Initialization
			wImg = imgMangaOrg.getWidth();
			hImg = imgMangaOrg.getHeight();
			wxh = wImg*hImg;
			
			imgPixels = imgMangaOrg.getRGB(0, 0, wImg, hImg, null, 0, wImg);
			
			aryBkgArea = new byte[wxh];
			aryDiagArea = new byte[wxh];
			
			imgMangaMod = new BufferedImage(wImg, hImg,	BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D)imgMangaMod.getGraphics();
			g.drawImage(imgMangaOrg, 0, 0, null);
			
			
			return true;
			
		} catch (IOException ex) {
			imgMangaOrg = null;
			JOptionPane.showMessageDialog(
					null, "File operation failed!", 
					"Error", JOptionPane.ERROR_MESSAGE
			);
			return false;
		} finally {
		}	
		
	}
	
	/**
	 * Calculate the manga dialogues' masks using region-filling algorithm
	 * @param x X value of the clicked point
	 * @param y Y value of the clicked point
	 * @param th_A Alpha value tolerance
	 * @param th_R Red value tolerance
	 * @param th_G Green value tolerance
	 * @param th_B Blue value tolerance
	 * @param diagColor Color for filling dialogue masks
	 */
	public void addDialogueArea(int x, int y,
			int th_A, int th_R, int th_G, int th_B, int diagColor) {
		
		byte idx = aryDiagArea[y*wImg+x];
		int j = 0;
		int[] dx = {1, 1, 1, 0, 0, -1, -1, -1};
		int[] dy = {1, 0, -1, 1, -1, 1, 0, -1};
		int tx, ty, v;
		
		if (bavBkg) {
			bavBkg = false;
			lstBkgSrcPoint.clear();
			aryBkgArea = new byte[wxh];
		}
		
		// Toggle marked/unmarked
		if (idx>0)
		{
			lstDiagSrcPoint.remove(idx);
			for (j=0;j<wxh;j++) {
				if (aryDiagArea[j]==idx) {aryDiagArea[j] = 0;} 
			}
			for (j=0;j<wxh;j++) {
				if (aryDiagArea[j]==idx) {aryDiagArea[j]--;}
			}
			
		} else if (idx==255){
			JOptionPane.showMessageDialog(
					null, "Max 255 dialogues marked", 
					"Error", JOptionPane.ERROR_MESSAGE
			);
			
		} else
		{
			idx = (byte) (lstDiagSrcPoint.size()+1);
			
			int pxColor = imgMangaOrg.getRGB(x, y); // Get clicked point color
			
			// Start from clicked point
			Queue<Point> pt_queue = new LinkedList<Point>();
			Point pt = new Point(x,y);
			lstDiagSrcPoint.add(pt);
			pt_queue.add(pt);
			aryDiagArea[y*wImg + x] = idx;
			
			// Expend from (x,y)
			while (!pt_queue.isEmpty()) {
				pt = pt_queue.poll();
				
				for (j=0; j<8; j++) {
					tx = pt.x + dx[j];
					ty = pt.y + dy[j];
					if (tx>=0 && tx<wImg &&
							ty>=0 && ty<hImg && aryDiagArea[ty*wImg+tx]==0) {
						v = imgPixels[ty*wImg+tx];
						v = Math.abs(v-pxColor);
						if (((v & 0xff000000) >> 24) < th_A &&
							((v & 0xff0000) >> 16) < th_R &&
							((v & 0xff00) >>8) < th_G &&
							(v & 0xff) < th_B
						) {
							pt_queue.add(new Point(tx,ty));
							aryDiagArea[ty*wImg+tx] = idx;
						}
					}
				}
			}
			
			// Paint mask
			paintMask(diagColor,0);
		}
		
	}
	
	/**
	 * Calculate the manga background's masks using region-filling algorithm
	 * @param x X value of the clicked point
	 * @param y Y value of the clicked point
	 * @param diagColor Color for filling dialogue masks
	 * @param bkgColor Color for filling background masks
	 */
	public void addBackgroundArea(int x, int y, int diagColor, int bkgColor) {
		
		byte idx = aryBkgArea[y*wImg+x];
		int j = 0;			
		int[] dx = {1, 1, 1, 0, 0, -1, -1, -1};
		int[] dy = {1, 0, -1, 1, -1, 1, 0, -1};
		int tx, ty;
		
		bavBkg = true;
		
		// Toggle marked/unmarked
		if (idx>0)
		{
			lstBkgSrcPoint.remove(idx);
			for (j=0;j<wxh;j++) {
				if (aryBkgArea[j]==idx) {aryDiagArea[j] = 0;} 
			}
			for (j=0;j<wxh;j++) {
				if (aryBkgArea[j]==idx) {aryDiagArea[j]--;}
			}

		} else if (idx==255){
			JOptionPane.showMessageDialog(
					null, "Max 255 dialogues marked", 
					"Error", JOptionPane.ERROR_MESSAGE
					);

		} else {

			// Start from clicked point
			idx = (byte)(lstBkgSrcPoint.size()+1);
			Queue<Point> pt_queue = new LinkedList<Point>();
			Point pt = new Point(x,y);
			lstBkgSrcPoint.add(pt);
			pt_queue.add(pt);
			aryBkgArea[y*wImg + x] = idx;

			// Expend from (x,y)


			while (!pt_queue.isEmpty()) {
				pt = pt_queue.poll();

				for (j=0; j<8; j++) {
					tx = pt.x + dx[j];
					ty = pt.y + dy[j];
					if (tx>=0 && tx<wImg &&
							ty>=0 && ty<hImg && aryBkgArea[ty*wImg+tx]==0) {
						if (aryDiagArea[ty*wImg+tx]==0) {
							pt_queue.add(new Point(tx,ty));
							aryBkgArea[ty*wImg+tx] = idx;
						}
					}
				}
			}


			// Paint mask
			paintMask(diagColor, bkgColor);
		}
		
	}
	
	/**
	 * Draw the mask covering dialogue and background areas
	 * @param diagColor Mask color of dialogue areas
	 * @param bkColor Mask color of background areas 
	 */
	private void paintMask(int diagColor, int bkColor) {
	    

	    int[] buf_pic = new int[wxh];
	    
	    // Fill mask
	    int j;
	    for (j=0;j<wxh;j++) {
	    	if (aryDiagArea[j]!=0) {
	    		buf_pic[j] = diagColor;
	    	}
	    	if (aryBkgArea[j]!=0) {
	    		buf_pic[j] = bkColor;
	    	}
	    }
	    
	    // Convert to graphic
	    BufferedImage imgNewMask = new BufferedImage(wImg, hImg, BufferedImage.TYPE_INT_ARGB);
	    imgNewMask.setRGB(0, 0, wImg, hImg, buf_pic, 0, wImg);
	     
	    Graphics2D g2d = (Graphics2D)imgMangaMod.getGraphics();
	    g2d.clearRect(0, 0, wImg, hImg);
		g2d.drawImage(imgMangaOrg, 0, 0, null);
		g2d.drawImage(imgNewMask, 0, 0, null);
		
		
	}
	
	/**
	 * Fill dialogue areas with fillColor and clear all contents
	 * @param fillColor Color for filling
	 */
	public void wipeDialogue(int fillColor) {
	    
	    int[] buf_pic = new int[wxh];
	    
	    // Fill mask
	    int j;
	    for (j=0;j<wxh;j++) {
	    	if (aryBkgArea[j]==0) {
	    		buf_pic[j] = fillColor;
	    	}
	    }
	    
	    // Convert to graphic
	    BufferedImage imgNewMask = new BufferedImage(wImg, hImg, BufferedImage.TYPE_INT_ARGB);
	    imgNewMask.setRGB(0, 0, wImg, hImg, buf_pic, 0, wImg);
	     
	    Graphics2D g2d = (Graphics2D)imgMangaMod.getGraphics();
	    g2d.clearRect(0, 0, wImg, hImg);
		g2d.drawImage(imgMangaOrg, 0, 0, null);
		g2d.drawImage(imgNewMask, 0, 0, null);
		
		
	}
	
	/* Msc functions */
	
	// Return WxH
	public int getWidth() {
		return wImg;
	}
	public int getHeight() {
		return hImg;
	}

	// Zoom operations
	public void setZoomFactor(float zf) {
		numZoomFactor = zf;
	}
	public float getZoomFactor() {
		return numZoomFactor;
	}

	// Output modified image
	public BufferedImage getOutputImage() {
		return imgMangaMod;
	}
	
}


	