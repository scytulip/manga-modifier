package gui_pack;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import core_pack.AppSettings;
import core_pack.MangaImgCell;
 
//Scrollable view area of the manga pictures 
public class ImageView extends JComponent
                               implements Scrollable,
                                          MouseMotionListener,
                                          MouseListener {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = -2643088051135434118L;
	private int maxUnitIncrement = 1;
    
	private MangaImgCell man_cell;
    private BufferedImage img_cell;
    
	private AppSettings app_set; // App config
 
    public ImageView(AppSettings aps) {
 
        //Let the user scroll by dragging to outside the window.
        setAutoscrolls(true); //enable synthetic drag events
        addMouseMotionListener(this); //handle mouse drags
        addMouseListener(this);
        
        //Initialization
        img_cell = null;
        app_set = aps;
        
        maxUnitIncrement = app_set.getIntMaxUnitIncrement();
    }
    
    public void setManga(MangaImgCell man)
    {
    	man_cell = man;
    	this.img_cell = man.getOutputImage();
    	repaint();
    }
 
    //Methods required by the MouseMotionListener interface:
    public void mouseMoved(MouseEvent e) {
    	
    }
    
    public void mouseDragged(MouseEvent e) {
        //The user is dragging us, so scroll!
        Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
        scrollRectToVisible(r);
    }
 
    public Dimension getPreferredSize() {
        if (img_cell != null) {
        	return new Dimension(img_cell.getWidth(null),
        			img_cell.getHeight(null));
        } else {
        	return new Dimension(1,1);
        }
    }
    
    public void paintComponent(Graphics g) { //Customized paint function
    	Graphics2D g2d = (Graphics2D)g;
    	if (img_cell != null) {
    		g2d.drawImage(img_cell, null, 0, 0);
    	} else {
    		g2d.setColor(Color.WHITE);
    		g2d.fillRect(0, 0, 1, 1);
    		
    	}
    }
    
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }
 
    public int getScrollableUnitIncrement(Rectangle visibleRect,
                                          int orientation,
                                          int direction) {
        //Get the current position.
        int currentPosition = 0;
        if (orientation == SwingConstants.HORIZONTAL) {
            currentPosition = visibleRect.x;
        } else {
            currentPosition = visibleRect.y;
        }
 
        //Return the number of pixels between currentPosition
        //and the nearest tick mark in the indicated direction.
        if (direction < 0) {
            int newPosition = currentPosition -
                             (currentPosition / maxUnitIncrement)
                              * maxUnitIncrement;
            return (newPosition == 0) ? maxUnitIncrement : newPosition;
        } else {
            return ((currentPosition / maxUnitIncrement) + 1)
                   * maxUnitIncrement - currentPosition;
        }
    }
 
    public int getScrollableBlockIncrement(Rectangle visibleRect,
                                           int orientation,
                                           int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width - maxUnitIncrement;
        } else {
            return visibleRect.height - maxUnitIncrement;
        }
    }
 
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }
 
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
 
    public void setMaxUnitIncrement(int pixels) {
        maxUnitIncrement = pixels;
    }

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent arg0) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

	}
	
	@Override
    public void mouseClicked(MouseEvent e) {
		final int tx = e.getX();
		final int ty = e.getY();
		Runnable runnable=new Runnable(){  
			@Override  
			public void run() {  
				if (app_set.isMarkDial()) {
					
					man_cell.addDialogueArea(
							tx, 
							ty,
							app_set.getTh_A(),
							app_set.getTh_R(),
							app_set.getTh_G(),
							app_set.getTh_B(),
							0x77ffff00
					);
					
					img_cell = man_cell.getOutputImage();
					repaint();
    			
				} else if (app_set.isMarkBG()) {
					
					man_cell.addBackgroundArea(
							tx, 
							ty,
							0x77ffff00,
							0x770000ff
					);
					
					img_cell = man_cell.getOutputImage();
	    			repaint();
	    			app_set.setBGMarked();
	    			
				}
			}
		};
		new Thread(runnable).start();
    }
	
	// Wipe Dialogue
	public void wipeDiag() {
		Runnable runnable=new Runnable(){  
			@Override  
			public void run() {
				man_cell.wipeDialogue(0xffffffff);
				img_cell = man_cell.getOutputImage();
    			repaint();
			}
		};
		new Thread(runnable).start();
	}
}