package com.madalla.util.ui;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

public class CalendarUtilsTest extends TestCase{
	private Log log = LogFactory.getLog(this.getClass());

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testCreateMonthlyTree(){
		TreeModel treeModel = CalendarUtils.createMonthlyTree("Root node", createList());
		
		MutableTreeNode node = (MutableTreeNode) treeModel.getRoot();
		log.debug("root = "+node);
		
		for (Enumeration e = node.children(); e.hasMoreElements();) {
			MutableTreeNode month = (MutableTreeNode)e.nextElement();
			log.debug("Month = "+month);
			for (Enumeration e1 = month.children(); e1.hasMoreElements();){
				DefaultMutableTreeNode entry = (DefaultMutableTreeNode) e1.nextElement();
				ICalendarTreeInput treeInput = (ICalendarTreeInput) entry.getUserObject();
				log.debug(" entry = " + entry);
			}
		}
		{
			Enumeration e = node.children();
			MutableTreeNode currentMonth = (MutableTreeNode)e.nextElement();
			assertNotNull(currentMonth);
			assertEquals(3, currentMonth.getChildCount());
			
			MutableTreeNode twoMonthOld = (MutableTreeNode)e.nextElement();
			assertNotNull(twoMonthOld);
			assertEquals(2, twoMonthOld.getChildCount());

			MutableTreeNode threeMonthOld = (MutableTreeNode)e.nextElement();
			assertNotNull(threeMonthOld);
			assertEquals(2, threeMonthOld.getChildCount());

		}
	}
	
	private List<TreeEntry> createList(){
        List<TreeEntry> list = new ArrayList<TreeEntry>();
        DateTime dateTime = new DateTime();
        String description = "Description Test";
        
        list.add(new TreeEntry(dateTime,"Current","Description test"));
        
        list.add(new TreeEntry(dateTime.plusMonths(1),"future",description));
        list.add(new TreeEntry(dateTime.plusMonths(1).withDayOfMonth(1),"first of this month", description));
        list.add(new TreeEntry(dateTime.minusMonths(2),"2 month old", description));
        list.add(new TreeEntry(dateTime.minusMonths(2).withDayOfMonth(5),"another 2 month old", description));
        list.add(new TreeEntry(dateTime.minusMonths(3).withDayOfMonth(1),"3 month old - 1st day of month", description));
        list.add(new TreeEntry(dateTime.minusMonths(3).withDayOfMonth(1),"3 month old - last day of month", description));
        list.add(new TreeEntry(dateTime.minusYears(1),"1 year old", description));
        list.add(new TreeEntry(dateTime.minusYears(3),"3 years old", description));
        
        return list;
	}
	
	public class TreeEntry implements ICalendarTreeInput{
		DateTime date;
		String description;
		String title;
		
		public TreeEntry(DateTime date, String title, String description){
			this.date = date;
			this.title = title;
			this.description = description;
		}
		
		public String getDescription() {
			return description;
		}
		public String getTitle() {
			return title;
		}
		public DateTime getDateTime() {
			return date;
		}
		
	}

}
