/**
 * 
 */
package gui_pack;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import core_pack.MangaImgCell;

/**
 * @author Tulip
 *
 */
public class PreviewLabelRender extends JLabel implements ListCellRenderer {

	private static final long serialVersionUID = -6163701364289994446L;

	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	@Override
	public Component getListCellRendererComponent(JList list,
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		
        setBackground(isSelected ? Color.white : Color.blue);
		return this;
		
	}
	
	public PreviewLabelRender(MangaImgCell imgCell) {
		
		setOpaque(true);
		setVerticalTextPosition(JLabel.BOTTOM);
		setHorizontalAlignment(JLabel.CENTER);
		setText(imgCell.toString());
		
		ImageIcon imgItem = new ImageIcon(imgCell.getPreviewIcon());
		this.setIcon(imgItem);
		
		repaint();
	}

}
