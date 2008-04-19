package com.madalla.util.ui;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import junit.framework.TestCase;

public class CalendarUtilsTest extends TestCase{
	private Log log = LogFactory.getLog(this.getClass());

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testCreateMonthlyTree(){
		List list = createList();
		TreeModel treeModel = CalendarUtils.createMonthlyTree("Root node", list);
		
		MutableTreeNode node = (MutableTreeNode) treeModel.getRoot();
		log.debug("root = "+node);
		
		for (Enumeration e = node.children(); e.hasMoreElements();) {
			MutableTreeNode month = (MutableTreeNode)e.nextElement();
			log.debug("Month = "+month);
			for (Enumeration e1 = month.children(); e1.hasMoreElements();){
				DefaultMutableTreeNode entry = (DefaultMutableTreeNode) e1.nextElement();
				ITreeInput treeInput = (ITreeInput) entry.getUserObject();
				log.debug(" entry = " + entry);
			}
		}
		
	}
	
	private List createList(){
        List list = new ArrayList();
        Calendar calendar = Calendar.getInstance();
        String description = "Description Test";
        
        list.add(new TreeEntry(calendar.getTime(),"Current","Description test"));
        
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        list.add(new TreeEntry(calendar.getTime(),"future",description));
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        list.add(new TreeEntry(calendar.getTime(),"first of this month", description));
        
        calendar.add(Calendar.MONTH, -2);
        calendar.set(Calendar.DAY_OF_MONTH, 5);
        
        
        return list;
	}
	
	public class TreeEntry implements ITreeInput{
		private DateFormat df = DateFormat.getDateInstance();
		private Date date;
		private String description;
		private String title;
		
		public TreeEntry(Date date, String description, String title){
			this.date = date;
			this.description = description;
			this.title = title;
		}
		
		public Date getDate() {
			return date;
		}

		public String getDescription() {
			return description;
		}

		public String getTitle() {
			return title;
		}
		
		public String getURL(){
			return "http://www.ecmalan.com";
		}
		
		public String getTitleDisplay(){
			return df.format(date);
		}
		
	}

}
