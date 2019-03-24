package music;

import music.MusicServiceStub.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame implements ActionListener {
	private final static int contentInset = 5;
	private final static int trackColumns = 130;
	private final static int trackRows = 20;
	private final static int gridLeft = GridBagConstraints.WEST;
	private final static String programTitle = "Music Album";

	private GridBagConstraints contentConstraints = new GridBagConstraints();
	private GridBagLayout contentLayout = new GridBagLayout();
	private Container contentPane = getContentPane();
	private JButton discButton = new JButton("Check");
	private JLabel discLabel = new JLabel("Disc Number:");
	private JTextField discText = new JTextField(5);
	private JButton nameButton = new JButton("Check");
	private JLabel nameLabel = new JLabel("Composer/Artiste Name:");
	private JTextField nameText = new JTextField(16);
	private Font trackFont = new Font(Font.MONOSPACED, Font.PLAIN, 12);
	private JLabel trackLabel = new JLabel("Tracks:");
	private JTextArea trackArea = new JTextArea(trackRows, trackColumns);
	private JScrollPane trackScroller = new JScrollPane(trackArea);
	private MultiTracks tracks;


// define here private variable for your Client Stub


	public Client() throws Exception  {
		contentPane.setLayout(contentLayout);
		addComponent(0, 0, gridLeft, nameLabel);
		addComponent(1, 0, gridLeft, nameText);
		addComponent(2, 0, gridLeft, nameButton);
		addComponent(0, 1, gridLeft, discLabel);
		addComponent(1, 1, gridLeft, discText);
		addComponent(2, 1, gridLeft, discButton);
		addComponent(0, 2, gridLeft, trackLabel);
		addComponent(0, 3, gridLeft, trackScroller);
		nameButton.addActionListener(this);
		discButton.addActionListener(this);
		trackArea.setFont(trackFont);
		trackArea.setEditable(false);

// instantiate your Client Stub and assign to private class variable declared above

}

	public static void main(String[] args) throws Exception {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = screenSize.width;
		int screenHeight = screenSize.height;

		Client window = new Client();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setTitle(programTitle);
		window.pack();
		int windowWidth = window.getWidth();
		int windowHeight = window.getHeight();
		int windowX = (screenWidth - windowWidth) / 2;
		int windowY = (screenHeight - windowHeight) / 2;
		window.setLocation(windowX, windowY);
		window.setVisible(true);
	}

	private void addComponent(int x, int y, int position, JComponent component) {
		Insets contentInsets = new Insets(contentInset, contentInset, contentInset, contentInset);
		contentConstraints.gridx = x;
		contentConstraints.gridy = y;
		contentConstraints.anchor = position;
		contentConstraints.insets = contentInsets;
		if (component == trackArea || component == trackLabel)
			contentConstraints.gridwidth = GridBagConstraints.REMAINDER;
		contentLayout.setConstraints(component, contentConstraints);
		contentPane.add(component);
	}

	public void actionPerformed(ActionEvent event) {
		String trackRows = "";
		TrackDetail[] tracks;
		try {
			if (event.getSource() == nameButton)
				tracks = getField("composer", nameText.getText());
			else if (event.getSource() == discButton)
				tracks = getField("disc", discText.getText());
			else
				return;
			trackRows += String.format("%4s %5s %-32s %-40s %-40s\n", "Disc", "Track", "Composer/Artist", "Work", "Title");
			for (int i = 0; i < tracks.length; i++) {
				TrackDetail trackDetail = tracks[i];
				
				trackRows += extractStrings(trackDetail);
// extract the data for a track and append to trackRows variable


			}
		} catch (Exception exception) {
			String error = exception.getMessage();
			if (error == null) error = exception.toString();
			error = "could not get tracks - " + error;
			trackRows += error;
		}
		trackArea.setText(trackRows);
	}
	
	private String extractStrings(TrackDetail track)
	{
		String title = track.getTitleName();
		String album = track.getWorkName();
		String artist = track.getComposerName();
		String discNumber = track.getDiscNumber().toString();
		String trackNumber = track.getTrackNumber().toString();
		
		String extractedTracks = String.format("%4s %5s %-32s %-40s %-40s\n", discNumber, trackNumber, artist, album, title);
		
		return extractedTracks;
	}


	private TrackDetail[] getField(String field, String value) throws Exception {
		
		try
		{
			TrackDetail[] tracklist;
			MusicServiceStub stub = new MusicServiceStub();
			
			if(field.equals("disc"))
			{
				MusicServiceStub.Disc requestedDisc = new MusicServiceStub.Disc();
				requestedDisc.setDisc(value);
				MusicServiceStub.MultiTracks response = stub.getByDisc(requestedDisc);
				tracklist = extractTracks(response);
				return tracklist;
			}
			else if(field.equals("composer"))
			{
				MusicServiceStub.Artist requestedArtist = new MusicServiceStub.Artist();
				requestedArtist.setArtist(value);
				MusicServiceStub.MultiTracks response = stub.getByArtist(requestedArtist);
				tracklist = extractTracks(response);
				return tracklist;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		return null;
// define behaviour for method getField() to call the web service methods and receive the results


	}
	
	private TrackDetail[] extractTracks(MultiTracks tracks)
	{
		TrackDetails track = tracks.getMultiTracks();
		TrackDetail[] tracklist = track.getTracklist();
		
		return tracklist;
	}

}
