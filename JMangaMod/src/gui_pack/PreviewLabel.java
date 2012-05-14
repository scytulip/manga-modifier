/**
 * 
 */
package gui_pack;

import java.awt.Color;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import core_pack.MangaImgCell;


public class PreviewLabel extends JLabel 
							implements ListCellRenderer, ListSelectionListener	{
	
	/**
	 * Use JLabel as JList's cell for displaying manga preview
	 */

	private static final long serialVersionUID = -6163701364289994446L;

	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	
	JList parent;
	ImageView manga_vw;

	/**
	 * Constructor
	 * @param vw Object of the working area
	 * @param par Container JList
	 */
	public PreviewLabel(ImageView vw, JList par) {
		
		setOpaque(true);
		setHorizontalTextPosition(JLabel.CENTER);
		setVerticalTextPosition(JLabel.BOTTOM);
		setHorizontalAlignment(JLabel.CENTER);

		manga_vw = vw;
		parent = par;
	}
	
	/**
	 * Display file names and image previews
	 */
	@Override
	public Component getListCellRendererComponent(JList list,
			Object value, int index, boolean isSelected, boolean cellHasFocus) {
		
		MangaImgCell imgCell = (MangaImgCell) value; 
		setText(imgCell.toString());
		ImageIcon imgItem = new ImageIcon(imgCell.getPreviewIcon());
		setIcon(imgItem);
		setBackground(isSelected ? Color.blue : Color.white);
		setForeground(isSelected ? Color.white : Color.black);
		return this;
		
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting())
		{
			int idx = parent.getSelectedIndex();
			if (idx == -1) {
				parent.setSelectedIndex(0);
			} else
			{
				manga_vw.setManga((MangaImgCell)parent.getModel().getElementAt(idx));
			}
		}
	}

}
