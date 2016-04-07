package Model;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {
	
	public static final String AREA_MARINDUQUE = "MARINDUQUE";
	public static final String AREA_PALAWAN = "PALAWAN";
	public static final String AREA_BOTH = "BOTH";
	
	public static final String BQUERY_TITLE1 = "Both Read Table";
	
	public static final String MQUERY_TITLE1 = "Marinduque Read Table";
	public static final String MQUERY_TITLE2 = "Marinduque Update Table 1";
	public static final String MQUERY_TITLE3 = "Marinduque Update Table 2";
	public static final String MQUERY_TITLE4 = "Marinduque Update Table 3";
	public static final String MQUERY_TITLE5 = "Marinduque Delete";
	
	public static final String PQUERY_TITLE1 = "Palawan Read Table";    
	public static final String PQUERY_TITLE2 = "Palawan Update Table 1";
	public static final String PQUERY_TITLE3 = "Palawan Update Table 2";
	public static final String PQUERY_TITLE4 = "Palawan Update Table 3";
	public static final String PQUERY_TITLE5 = "Palawan Delete";        
	
	public static final String BQUERY_1 = "SELECT hpq_hh_id,death_line,mdeadsx, mdeadage, mdeady, mdeady_o from hpq_death ";
	
	public static final String MQUERY_1 = "SELECT hpq_hh_id,death_line,mdeadsx, mdeadage, mdeady, mdeady_o from hpq_death ";
	public static final String MQUERY_2 = "UPDATE hpq_death SET mdeadsx = '1', mdeadage = '100', mdeady = '10', mdeady_o = 'Sinakal' ";
	public static final String MQUERY_3 = "UPDATE hpq_death SET mdeadsx = '2', mdeadage = '200', mdeady = '20', mdeady_o = 'Nagutom' ";
	public static final String MQUERY_4 = "UPDATE hpq_death SET mdeadsx = '1', mdeadage = '300', mdeady = '30', mdeady_o = 'Patiwakal' ";
	public static final String MQUERY_5 = "DELETE FROM hpq_death";
	
	public static final String PQUERY_1 = "SELECT hpq_hh_id,death_line,mdeadsx, mdeadage, mdeady, mdeady_o from hpq_death ";
	public static final String PQUERY_2 = "UPDATE hpq_death SET mdeadsx = '1', mdeadage = '100', mdeady = '10', mdeady_o = 'Choked' ";
	public static final String PQUERY_3 = "UPDATE hpq_death SET mdeadsx = '2', mdeadage = '200', mdeady = '20', mdeady_o = 'Starved' ";
	public static final String PQUERY_4 = "UPDATE hpq_death SET mdeadsx = '1', mdeadage = '300', mdeady = '30', mdeady_o = 'Suicide' ";
	public static final String PQUERY_5 = "DELETE FROM hpq_death";
	
	public static final ArrayList<String> BOTH_Q_TITLES =  new ArrayList<String>(Arrays.asList(
			BQUERY_TITLE1));
	
	public static final ArrayList<String> MARINDUQUE_Q_TITLES =  new ArrayList<String>(Arrays.asList(
			MQUERY_TITLE1, 
			MQUERY_TITLE2, 
			MQUERY_TITLE3, 
			MQUERY_TITLE4, 
			MQUERY_TITLE5));
	
	public static final ArrayList<String> PALAWAN_Q_TITLES =  new ArrayList<String>(Arrays.asList(
			PQUERY_TITLE1, 
			PQUERY_TITLE2, 
			PQUERY_TITLE3, 
			PQUERY_TITLE4, 
			PQUERY_TITLE5));

}
