package core_pack;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;

public class MangaImgCell {
	
	String strFilePath = ""; // File path
	String strFileName = ""; // File name
	boolean bChanged; // Mark for any change on the image
	boolean bavBkg; // Background area is available? false if dialogues are marked again after marking background 

	float zoomFactor = 1F;
	int wImg = 0, hImg = 0, wxh = 0;
	BufferedImage imgMangaOrg = null; // Original image
	BufferedImage imgMangaMod = null; // Output canvas
	BufferedImage imgNewMask = null; // Mask canvas

	List<BufferedImage> lstImgRec = null; // Image lists for undo operation
	int idxlstImgRec = 0; // Index of current pic

	List<Point> lstDiagSrcPoint; // List of dialogue source (clicked) points
	byte[] aryDiagArea; // Marked dialogue area

	List<Point> lstBkgSrcPoint; // List of background source (clicked) points
	byte[] aryBkgArea; // Marked background area

	public MangaImgCell() {
		lstDiagSrcPoint = new ArrayList<Point>();
		lstBkgSrcPoint = new ArrayList<Point>();
		lstImgRec = new ArrayList<BufferedImage>();
		bChanged = false;
	}

	/* ==== File Operation ==== */
	
	/**
	 * Set the image file handle and read it into buffer.
	 * @param img_file File object for read
	 * @return Boolean value indicates successfuly file operation
	 */
	public boolean setImgFile(File img_file)
	{

		try {
			imgMangaOrg = ImageIO.read(img_file);
			
			/* Initialization */
			bChanged = false;
			strFilePath = img_file.getCanonicalPath();
			strFileName = img_file.getName();

			wImg = imgMangaOrg.getWidth();
			hImg = imgMangaOrg.getHeight();
			wxh = wImg*hImg;

			aryBkgArea = new byte[wxh];
			aryDiagArea = new byte[wxh];

			imgMangaMod = new BufferedImage(wImg, hImg,	BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = (Graphics2D)imgMangaMod.getGraphics();
			g.drawImage(imgMangaOrg, 0, 0, null);
			g.dispose();
			
			imgNewMask = new BufferedImage(wImg, hImg,	BufferedImage.TYPE_INT_ARGB);
			

			return true;

		} catch (IOException ex) {
			imgMangaOrg = null;
			return false;
			
		} finally {
		}	

	}
	
	/**
	 * Save image to file. (ATTENTION: no applied operation will not be saved)
	 * @param fp File name and path for "Save As"
	 * @throws IOException Exception of IO operation
	 */
	public void saveImgFile(String fp) throws IOException {
		
		/* Prepare file name */
		String fpath = strFilePath;
		if (fp != "") {
			fpath = fp;
		}
		
		/* Get suffix for image format */
		int mid= fpath.lastIndexOf(".");
		String fmt_name=fpath.substring(mid+1,fpath.length());
		
		/* Write image */
		ImageIO.write(imgMangaOrg, 
				fmt_name.toLowerCase(), 
				new File(fpath)
		);
		bChanged = false;
		
	}

	/* ==== Wipe Diaglogue Contents ==== */
	
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
			int th_A, int th_R, int th_G, int th_B, int diagColor)
			throws Exception
	{

		int j = 0;
		int[] dx = {1, 1, 1, 0, 0, -1, -1, -1};
		int[] dy = {1, 0, -1, 1, -1, 1, 0, -1};
		int tx, ty, v;
		
		bChanged = true;
		
		x = (int)(Math.round(x / zoomFactor)); if (x>wImg) x = wImg;
		y = (int)(Math.round(y / zoomFactor)); if (y>hImg) y = hImg;
		 
		byte idx = aryDiagArea[y*wImg+x];
		
		if (bavBkg) {
			bavBkg = false;
			lstBkgSrcPoint.clear();
			aryBkgArea = new byte[wxh];
		}

		/* Toggle marked/unmarked */
		if (idx>0)
		{
			lstDiagSrcPoint.remove(idx-1);
			for (j=0;j<wxh;j++) {
				if (aryDiagArea[j]==idx) {aryDiagArea[j] = 0;} 
			}
			for (j=0;j<wxh;j++) {
				if (aryDiagArea[j]>idx) {aryDiagArea[j]--;}
			}
			
			/* Paint mask */
			paintMask(diagColor,0);

		} else if (idx==127){
			throw new Exception("Max 127 dialogues marked");
		} else
		{
			idx = (byte) (lstDiagSrcPoint.size()+1);

			int pxColor = imgMangaOrg.getRGB(x, y); // Get clicked point color

			/* Expand from clicked point */
			Queue<Point> pt_queue = new LinkedList<Point>();
			Point pt = new Point(x,y);
			lstDiagSrcPoint.add(pt);
			pt_queue.add(pt);
			aryDiagArea[y*wImg + x] = idx;
			int[] imgPixels = imgMangaOrg.getRGB(0, 0, wImg, hImg, null, 0, wImg);

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

			/* Paint mask */
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
	public void addBackgroundArea(int x, int y, int diagColor, int bkgColor) 
		throws Exception {
	
		
		int j = 0;			
		int[] dx = {1, 1, 1, 0, 0, -1, -1, -1};
		int[] dy = {1, 0, -1, 1, -1, 1, 0, -1};
		int tx, ty;
		
		bChanged = true;

		bavBkg = true;
		
		x = (int)(Math.round(x / zoomFactor)); if (x>wImg) x = wImg;
		y = (int)(Math.round(y / zoomFactor)); if (y>hImg) y = hImg;

		byte idx = aryBkgArea[y*wImg+x];
		
		/* Toggle marked/unmarked */
		if (idx>0)
		{
			lstBkgSrcPoint.remove(idx-1);
			for (j=0;j<wxh;j++) {
				if (aryBkgArea[j]==idx) {aryDiagArea[j] = 0;} 
			}
			for (j=0;j<wxh;j++) {
				if (aryBkgArea[j]==idx) {aryDiagArea[j]--;}
			}
			
			/* Paint mask */
			paintMask(diagColor, bkgColor);

		} else if (idx==127){
			throw new Exception("Max 127 dialogues marked");
		} else {

			/* Expand from clicked point */
			idx = (byte)(lstBkgSrcPoint.size()+1);
			Queue<Point> pt_queue = new LinkedList<Point>();
			Point pt = new Point(x,y);
			lstBkgSrcPoint.add(pt);
			pt_queue.add(pt);
			aryBkgArea[y*wImg + x] = idx;

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


			/* Paint mask */
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
		
		bChanged = true;

		/* Fill mask */
		int j;
		for (j=0;j<wxh;j++) {
			if (aryDiagArea[j]!=0) {
				buf_pic[j] = diagColor;
			}
			if (aryBkgArea[j]!=0) {
				buf_pic[j] = bkColor;
			}
		}

		/* Convert to graphic */
		imgNewMask.setRGB(0, 0, wImg, hImg, buf_pic, 0, wImg);

		retrieveOrg();
		Graphics2D g2d = (Graphics2D)imgMangaMod.getGraphics();
		g2d.drawImage(imgNewMask, 0, 0, null);
		g2d.dispose();

	}
	
	/**
	 * Fill dialogue areas with fillColor and clear all contents
	 * @param fillColor Color for filling
	 */
	public void wipeDialogue(int fillColor) throws Exception {

		int[] buf_pic = new int[wxh];
		
		if (lstBkgSrcPoint.size()==0) {
			throw new Exception("Please define at least 1 background area.");
		}
		
		bChanged = true;

		/* Fill mask */
		int j;
		for (j=0;j<wxh;j++) {
			if (aryBkgArea[j]==0) {
				buf_pic[j] = fillColor;
			}
		}

		/* Convert to graphic */
		cancelAllMasks();
		retrieveOrg();
		imgNewMask.setRGB(0, 0, wImg, hImg, buf_pic, 0, wImg);
		Graphics2D g2d = (Graphics2D)imgMangaMod.getGraphics();
		g2d.drawImage(imgNewMask, 0, 0, null);
		g2d.dispose();
		updateOrg();
		

	}
	
	/* ==== Common Operation ===== */
	
	/**
	 * Update original image with modified version
	 * imgMangaOrg <- imgMangaMod
	 */
	private void updateOrg() {
		
		imgMangaOrg = new BufferedImage(wImg, hImg,	BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)imgMangaOrg.getGraphics();
		g2d.drawImage(imgMangaMod, 0, 0, null);
		g2d.dispose();
		
	}
	
	/**
	 * Retrieve original image
	 * imgMangaOrg -> imgMangaMod
	 */
	private void retrieveOrg() {
		
		imgMangaMod = new BufferedImage(wImg, hImg,	BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D)imgMangaMod.getGraphics();
		g2d.drawImage(imgMangaOrg, 0, 0, null);
		g2d.dispose();
		
	}
	
	/**
	 * Cancel all marked masks
	 */
	private void cancelAllMasks() {
		
		aryBkgArea = new byte[wxh];
		aryDiagArea = new byte[wxh];
		lstBkgSrcPoint.clear();
		lstDiagSrcPoint.clear();
		bavBkg = false;
		
	}
	
	/* ==== Image Output ==== */

	/**
	 * Return the zoomed image for output
	 * @return BufferedImage
	 */
	public BufferedImage getOutputImage() {
		
		BufferedImage imgOut = new BufferedImage(
				(int)(Math.round(zoomFactor * wImg)),
				(int)(Math.round(zoomFactor * hImg)),
				BufferedImage.TYPE_INT_ARGB
				);

		Graphics2D g2d = (Graphics2D) imgOut.getGraphics();
        g2d.scale(zoomFactor, zoomFactor);
        g2d.drawImage(imgMangaMod, 0, 0, null);
        g2d.dispose();
        
		return imgOut;
	}
	
	/**
	 * Return the preview icon of the manga image
	 * @return Image
	 */
	public Image getPreviewIcon() {
		
		float zf;
		
		if (wImg>hImg) {
			zf = 128F/wImg;
		} else 	{
			zf = 128F/hImg;
		}
		
		int w = (int)(Math.round(zf * wImg));
		int h = (int)(Math.round(zf * hImg));
        
		return imgMangaOrg.getScaledInstance(w, h, Image.SCALE_SMOOTH);
		
	}
	
	/* ==== Zoom functions ==== */
	/**
	 * Set the zoom factor
	 * @param zf Zoom factor
	 */
	public void setZoomFactor(float zf) {
		/* Zoom operations */
		zoomFactor = (zf > 4F)? 4F: ((zf < 1/8F)? 1/64F : zf);
	}
	
	/**
	 * Auto fit according to the given width and height
	 * @param w Width
	 * @param h Height
	 * @param opr Operation of the zoom action
	 * 				0 - Fit the window (height)
	 */
	public void setZoomFactorWH(int w, int h, int opr) {
		
		float zf = 0;
		
		switch (opr)
		{
			case 0:
			{
				zf = ((float) h)/((float) hImg);
				break;
			}
		}
		
		setZoomFactor(zf);
	}
	
	/**
	 * Zoom in image
	 */
	public void setZoomIn() {
		setZoomFactor(zoomFactor * 1.25F);
	}
	
	/**
	 * Zoom out image
	 */
	public void setZoomOut() {
		setZoomFactor(zoomFactor / 1.25F);
	}
	
	/**
	 * Get current zoom factor
	 * @return Zoom factor
	 */
	public float getZoomFactor() {
		return zoomFactor;
	}
	
	/* ====Msc functions==== */

	public int getWidth() {
		return wImg;
	}
	public int getHeight() {
		return hImg;
	}

	@Override
	public String toString() {
		return strFileName;
	}

	public boolean isChanged() {
		return bChanged;
	}

	public void setNull() {
		imgMangaOrg = null;
		imgMangaMod = null;
		imgNewMask = null;
		bChanged = false;
	}

}


