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
	List<boolean[]> lstDiagArea; // List of dialogue areas
	List<Point> lstDiagSrcPoint; // List of dialogue source (clicked) points
	List<boolean[]> lstBkgArea; // List of background areas
	List<Point> lstBkgSrcPoint; // List of background source (clicked) points
	boolean[] aryBkgArea; // Background area
	int[] imgPixels; 

	public MangaImgCell() {
		lstDiagArea = new ArrayList<boolean[]>();
		lstDiagSrcPoint = new ArrayList<Point>();
		lstBkgArea = new ArrayList<boolean[]>();
		lstBkgSrcPoint = new ArrayList<Point>();
	}

	public boolean setImgFile(File img_file)
	{
		try {
			imgMangaOrg = ImageIO.read(img_file);
			
			// Initialization
			wImg = imgMangaOrg.getWidth();
			hImg = imgMangaOrg.getHeight();
			wxh = wImg*hImg;
			
			imgPixels = imgMangaOrg.getRGB(0, 0, wImg, hImg, null, 0, wImg);
			
			aryBkgArea = new boolean[wxh];
			
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
	
	// Calculate dialogues' masks
	public void addDialogueArea(int x, int y,
			int th_A, int th_R, int th_G, int th_B, int diagColor) {
		
		// TODO Select & Deselect by using UnionSet
		
		boolean[] mask = new boolean[wxh];
		Queue<Point> pt_queue = new LinkedList<Point>();
		int pxColor = imgMangaOrg.getRGB(x, y); // Get clicked point color
		
		// Start from clicked point
		Point pt = new Point(x,y);
		lstDiagSrcPoint.add(pt);
		pt_queue.add(pt);
		mask[y*wImg + x] = true;
		
		// Expend from (x,y)
		int[] dx = {1, 1, 1, 0, 0, -1, -1, -1};
		int[] dy = {1, 0, -1, 1, -1, 1, 0, -1};
		int tx, ty, j, v;
		
		while (!pt_queue.isEmpty()) {
			pt = pt_queue.poll();
			
			for (j=0; j<8; j++) {
				tx = pt.x + dx[j];
				ty = pt.y + dy[j];
				if (tx>=0 && tx<wImg &&
						ty>=0 && ty<hImg && !mask[ty*wImg+tx]) {
					v = imgPixels[ty*wImg+tx];
					v = Math.abs(v-pxColor);
					if (((v & 0xff000000) >> 24) < th_A &&
						((v & 0xff0000) >> 16) < th_R &&
						((v & 0xff00) >>8) < th_G &&
						(v & 0xff) < th_B
					) {
						pt_queue.add(new Point(tx,ty));
						mask[ty*wImg+tx] = true;
					}
				}
			}
		}
		
		// Add to list
		lstDiagArea.add(mask);
		
		// Paint mask
		paintMask(diagColor,0);
		
	}
	
	// Calculate background area
	public void addBackgroundArea(int x, int y, int diagColor, int bkgColor) {
		
		boolean[] m = getCombinedMask();
		boolean[] mask = new boolean[wxh];
		Queue<Point> pt_queue = new LinkedList<Point>();
		
		// Start from clicked point
		Point pt = new Point(x,y);
		lstBkgSrcPoint.add(pt);
		pt_queue.add(pt);
		mask[y*wImg + x] = true;
		
		// Expend from (x,y)
		int[] dx = {1, 1, 1, 0, 0, -1, -1, -1};
		int[] dy = {1, 0, -1, 1, -1, 1, 0, -1};
		int tx, ty, j;
		
		while (!pt_queue.isEmpty()) {
			pt = pt_queue.poll();
			
			for (j=0; j<8; j++) {
				tx = pt.x + dx[j];
				ty = pt.y + dy[j];
				if (tx>=0 && tx<wImg &&
						ty>=0 && ty<hImg && !mask[ty*wImg+tx]) {
					if (!m[ty*wImg+tx]) {
						pt_queue.add(new Point(tx,ty));
						mask[ty*wImg+tx] = true;
					}
				}
			}
		}
		
		// Add to list
		lstBkgArea.add(mask);
		
		// Paint mask
		paintMask(diagColor, bkgColor);
		
	}
	
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
	
	
	// Combine all dialogue masks
	private boolean[] getCombinedMask() {
		boolean[] mask = new boolean[wxh];
		boolean[] m = null;
		Iterator<boolean[]> i = lstDiagArea.iterator();
		int j;
		
		while (i.hasNext()) {
			m = i.next();
			for (j=0; j<wxh; j++) {mask[j] = mask[j]|m[j];}
		}
		
		return mask;
	}
	
	// Combine all dialogue masks
	private boolean[] getCombinedBkg() {
		boolean[] mask = new boolean[wxh];
		boolean[] m = null;
		Iterator<boolean[]> i = lstBkgArea.iterator();
		int j;
		
		while (i.hasNext()) {
			m = i.next();
			for (j=0; j<wxh; j++) {mask[j] = mask[j]|m[j];}
		}
		
		return mask;
	}	
	
	// Paint a mask on target area
	private void paintMask(int diagColor, int bkColor) {
	    
	    boolean[] mask = getCombinedMask();
	    boolean[] mask_b = getCombinedBkg();
	    int[] buf_pic = new int[wxh];
	    
	    // Fill mask
	    int j;
	    for (j=0;j<wxh;j++) {
	    	if (mask[j]) {
	    		buf_pic[j] = diagColor;
	    	}
	    	if (mask_b[j]) {
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
	
	// Paint a mask on target area
	public void wipeDialogue(int fillColor) {
	    
	    boolean[] mask_b = getCombinedBkg();
	    int[] buf_pic = new int[wxh];
	    
	    // Fill mask
	    int j;
	    for (j=0;j<wxh;j++) {
	    	if (!mask_b[j]) {
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
	
}
	