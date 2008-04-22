package com.madalla.util.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

public class CalendarUtils {
    private static DateFormat df = new SimpleDateFormat("MMMMM yyyy");
    private static DateFormat blogFormat = DateFormat.getDateInstance();

	public static TreeModel createMonthlyTree(String rootTitle, List list){
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(rootTitle);
        TreeModel model = new DefaultTreeModel(rootNode);
        
        //Get Blogs in tree of months
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(df.format(calendar.getTime()));
        for (Iterator iter = list.iterator(); iter.hasNext();) {
			ITreeInput entry = (ITreeInput) iter.next();
			Date date = entry.getDate();
			while (date.before(calendar.getTime())){
				calendar.add(Calendar.MONTH, -1);
				node = new DefaultMutableTreeNode(df.format(calendar.getTime()));
			}
			rootNode.add(node);
			node.add(new DefaultMutableTreeNode(entry){

				public String toString() {
					ITreeInput input = (ITreeInput) getUserObject();
					return input.getTitleDisplay();
				}
				
			});
			
		}
        
        return model;
	}
	
}
