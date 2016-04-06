package Model;

import java.util.ArrayList;
import java.util.Arrays;

public class ComboBoxConstants {
	
	private static int HAVING = 0;
	private static int FILTER = 1;

	public static ArrayList<Column> OPTIONS_QUERY = new ArrayList<Column>(Arrays.asList(
			new Column("crop_type", "Type of Crop", FILTER),
			new Column("flood_frequency", "Flood Frequency", FILTER),
			new Column("drought", "Drought Frequency", FILTER),
			new Column("reason_for_change_in_crop", "Reason of Crop Change", FILTER),
			new Column("area_of_farm", "Farm Area", FILTER),
			new Column("low_harvest_reason", "Reason of Low Harvest", FILTER),
			new Column("other_low_harvest_reason", "Other Low Harvest Reason", FILTER),
			new Column("SUM(crop_volume)", "Crop Volume", FILTER)));
		
	public static Column findColumn(String name){
		ArrayList<Column> cols = OPTIONS_QUERY;
		for (Column c: cols){
			if (name.equals(c.getColName()) || name.equals(c.getName())){
				return c;
			}
		}
		return null;
	}
}