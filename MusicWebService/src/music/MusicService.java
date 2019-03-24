package music;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MusicService extends MusicServiceSkeleton {

	  private final static String databaseHost = "mysql0.cs.stir.ac.uk";
	  private final static String databaseName = "CSCU9YW";
	  private final static String databasePassword = "mgr";
	  private final static String databaseUser = "mgr";
	  private final static String discTable = "music";


//define methods to implement the WSDL operations getByComposer() and getByDisc()

	public music.MultiTracks getByArtist(music.Artist artist) throws ErrorFault
	{

		try 
		{
			TrackDetail[] data = getByField("composer", artist.getArtist());
			
			return sendTracks(data);
		} 
		catch (Exception e) 
		{
			throw(new ErrorFault("Cannot find artist"));
		}
	}


	public music.MultiTracks getByDisc(music.Disc disc) throws ErrorFault
	{
		try
		{
			TrackDetail[] data = getByField("disc", disc.getDisc());
			
			return sendTracks(data);
		}
		catch(Exception e)
		{
			throw(new ErrorFault("Disc not there"));
		}
	
	}
	
	private MultiTracks sendTracks(TrackDetail[] tracks)
	{
		MultiTracks tracklist = new MultiTracks();
		TrackDetails retrievedTracks = new TrackDetails();
		
		retrievedTracks.setTracklist(tracks);
		tracklist.setMultiTracks(retrievedTracks);
		
		return tracklist;
	}


	private TrackDetail[] getByField(String field, String value) throws ErrorFault {
		try {
			if (value.length() == 0)
				throw (new Exception(field + " is empty"));
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			String databaseDesignation = "jdbc:mysql://" + databaseHost + "/" + databaseName + "?user=" + databaseUser
					+ "&password=" + databasePassword;
			Connection connection = DriverManager.getConnection(databaseDesignation);
			Statement statement = connection.createStatement();
			String query = "SELECT disc, track, composer, work, title " + "FROM " + discTable + " " + "WHERE " + field
					+ " LIKE '%" + value + "%'";
			ResultSet result = statement.executeQuery(query);
			result.last();
			int resultCount = result.getRow();
			if (resultCount == 0)
				throw (new Exception(field + " '" + value + "' not found"));

			TrackDetail[] trackDetails = new TrackDetail[resultCount];
			result.beforeFirst();
			int resultIndex = 0;
			while (result.next()) {
				
				TrackDetail extractedDetails = extractDetail(result);

				trackDetails[resultIndex++] = extractedDetails;
			}
			connection.close();
			return (trackDetails);
		} catch (Exception exception) {
			String errorMessage = "database access error - " + exception.getMessage();
			throw (new ErrorFault(errorMessage, exception));
		}
	}
	
	private TrackDetail extractDetail(ResultSet current) throws SQLException
	{
		TrackDetail detail = new TrackDetail();
		
		detail.setDiscNumber(current.getString(1));
		detail.setTrackNumber(current.getString(2));
		detail.setComposerName(current.getString(3));
		detail.setWorkName(current.getString(4));
		detail.setTitleName(current.getString(5));
		
		return detail;
	}
}
