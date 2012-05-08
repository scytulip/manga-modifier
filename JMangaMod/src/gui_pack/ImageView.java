package gui_pack;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ContainerListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import core_pack.AppSettings;
import core_pack.MangaImgCell;
 
/**
 * Scrollable view area of the manga pictures 
 * @author Tulip
 *
 */
public class ImageView extends JViewport
                               implements Scrollable,
                                          MouseMotionListener,
                                          MouseWheelListener,
                                          MouseListener {
 
	static final long serialVersionUID = -2643088051135434118L;
	int maxUnitIncrement = 1;
	boolean bConLock = false; //Concurrent lock
    
	MangaImgCell man_cell;
    BufferedImage img_cell;
    
	AppSettings app_set; // App config
	
	boolean isCtrlPressed = false; // Mark of Ctrl key
 
	/**
	 * Object contructor
	 * @param aps Application settings
	 */
    public ImageView(AppSettings aps) {
 
        //Let the user scroll by dragging to outside the window.
        setAutoscrolls(true); //enable synthetic drag events
        addMouseMotionListener(this); //handle mouse drags
        addMouseListener(this);
        addMouseWheelListener(this); //handle mouse wheel
        
        //Initialization
        img_cell = null;
        app_set = aps;
        
        maxUnitIncrement = app_set.getIntMaxUnitIncrement();
    }
    
    /**
     * Repaint the display area
     * @param man MangaImgCell oject
     */
    public void setManga(MangaImgCell man)
    {
    	man_cell = man;
    	this.img_cell = man.getOutputImage();
    	repaint();
    }
 
    /**
     * Action of wiping all marked dialogues
     */
	public void wipeDiag() {
		if (!bConLock) {	
			
			Runnable runnable=new Runnable(){  
				@Override  
				public void run() {
					setBusy();
					try {
						man_cell.wipeDialogue(app_set.getColorFilling());
						img_cell = man_cell.getOutputImage();
		    			repaint();
					} catch (Exception e) {
						JOptionPane.showMessageDialog(
								null, e.getMessage(), "Error", 
								JOptionPane.ERROR_MESSAGE
						);
					}
	    			unsetBusy();
				}
			};	
			new Thread(runnable).start();
		}
	}
	
	/**
	 * Execute works on the canvas
	 * @param tx X of the current position
	 * @param ty Y of the current position
	 */
	public void actComponent(final int tx, final int ty) {
		if (!bConLock)
		{
			Runnable runnable=new Runnable(){  
				@Override  
				public void run() {  
					setBusy();
					
					try {
						// Select operations according to current button status
						switch(app_set.getWkStatus())
						{
							case AppSettings.WS_MARK_DIAG:
							{
								man_cell.addDialogueArea(
										tx, ty,
										app_set.getTh_A(),
										app_set.getTh_R(),
										app_set.getTh_G(),
										app_set.getTh_B(),
										app_set.getColorDiagMask()
										);
								img_cell = man_cell.getOutputImage();
								repaint();
								break;
							}
							case AppSettings.WS_MARK_BKG:
							{
								man_cell.addBackgroundArea(
										tx, ty,
										app_set.getColorDiagMask(),
										app_set.getColorBkgMask()
										);
								img_cell = man_cell.getOutputImage();
								repaint();
								break;
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						
					}

					unsetBusy();
				}
			};
			new Thread(runnable).start();
		}
	}
	
	/* ============= Component Interface ============ */
	
    public void paintComponent(Graphics g) { //Customized paint function
    	Graphics2D g2d = (Graphics2D)g;
    	if (img_cell != null) {
    		g2d.drawImage(img_cell, null, 0, 0);
    	} else {
    		g2d.setColor(Color.WHITE);
    		g2d.fillRect(0, 0, 1, 1);
    		g2d.dispose();
    	}
    }
	
	/* ================ Mouse Interface ============== */
	
    public void mouseDragged(MouseEvent e) {
        //The user is dragging us, so scroll!
        Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
        scrollRectToVisible(r);
    }

	@Override
    public void mouseClicked(MouseEvent e) {
		final int tx = e.getX();
		final int ty = e.getY();
		switch(e.getButton())
		{
			case MouseEvent.BUTTON1: // Click button #1
			{
				actComponent(tx, ty);
				break;
			}
		}
    }
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		float zf = 1;
		
		if (!bConLock) {
			if (e.isControlDown()) {
				setBusy();

				if (e.getWheelRotation() < 0) {
					zf = 1.25F;
				} else if (e.getWheelRotation() > 0) {
					zf = 1/1.25F;
				}
				man_cell.setZoomFactor(man_cell.getZoomFactor() * zf);
				img_cell = man_cell.getOutputImage();
				setSize(img_cell.getWidth(), img_cell.getHeight());
				repaint();

				setLocation(
						new Point(0,0)
						); // TODO: Keep the point under cursor the same

				unsetBusy();
			} else {
				this.getParent().dispatchEvent(e);
			}
			
		} 
	}
	
	/* ================ Scroll Interface ============= */
	
    public Dimension getPreferredSize() {
        if (img_cell != null) {
        	return new Dimension(img_cell.getWidth(null),
        			img_cell.getHeight(null));
        } else {
        	return new Dimension(1,1);
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
    
	
	/* ============= Msc funtions ================== */
	
	/**
	 * Set concurrent lock and set cursor to waiting
	 */
	private void setBusy() {
		bConLock = true;
		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
	}
	
	/**
	 * Cancel concurrent lock and reset cursor to default
	 */
	private void unsetBusy() {
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		bConLock = false;
	}
	
	// ================ Unused ======================= //
	
	@Override
    public void mouseMoved(MouseEvent e) {
    	
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

	
}